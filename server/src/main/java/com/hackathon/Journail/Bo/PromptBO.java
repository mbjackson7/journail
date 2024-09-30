package com.hackathon.Journail.Bo;

import com.hackathon.Journail.Service.Journal.JournalService;
import com.hackathon.Journail.Service.Pinecone.PineconeService;
import com.hackathon.Journail.Model.JournalEntry;
import com.hackathon.Journail.Model.PineconeEntry;
import com.hackathon.Journail.Service.Claude.ClaudeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Component
public class PromptBO {

    private final ClaudeService claudeService;
    private final JournalService journalService;
    private final PineconeService pineconeService;

    @Autowired
    PromptBO(ClaudeService claudeService, JournalService journalService, PineconeService pineconeService) {
        this.claudeService = claudeService;
        this.journalService = journalService;
        this.pineconeService = pineconeService;
    }

    public String getStarterQuestion(JournalEntry journalEntry) {
        List<String> defaultOpeners = getSampleQuestions("sampleopeners.txt");

        String context = buildContext(journalService.getJournalEntries());

        //prompt bedrock for opener based on context and random default opener here
        String defaultStarterQuestion = getRandomQuestion(defaultOpeners);
        String starterPrompt =
                "You are an Ai chatbot for a journal ai application. Your purpose is to have a conversation with the user to facilitate journal entries via conversation with you, as if you were a friend or therapist." +
                        "Here is some previous context of past conversations. I want you to include this context when relevant in the following conversation we are going to have. The [Bot] lines were lines stated by you, while the [User] lines were stated by the user." +
                        context + "\n\n" +
                        "Give a similar question to the question that is listed in the following prompt, without revealing that you are replying to me. Just give me a similar question only. Remember the question should still pertain to today, recently, or near future. " +
                        "Do not say anything like \"Here's a similar question:\" Just give me the question only. Only one question.\n\n" +
                        "Question:" + defaultStarterQuestion;

        return claudeService.converse(starterPrompt);
    }

    public String getCloserQuestion(JournalEntry journalEntry) {
        List<String> defaultClosers = getSampleQuestions("sampleclosingquestions.txt");
        //prompt bedrock for closer based on context and random default closer here

        return getRandomQuestion(defaultClosers);
    }

    public void createSummary(JournalEntry journalEntry) {
        String summary = getSummary(journalEntry.getConversation());
        journalEntry.setSummary(summary);
        journalService.updateJournalEntry(journalEntry);
    }

    public String respond(String message, JournalEntry journalEntry) {
        String prompt = "You are an AI chatbot helping a user process their thoughts in a diary. " +
                "You have been having a conversation with an end user, and should continue this conversation naturally based on a history of the conversation I will provide, " +
                "as well as a collection of extra contextual information pulled from past journal entries from the user.\n";

        //Look for relevance in vector db
        List<PineconeEntry> pineconeEntries = pineconeService.get(message, journalEntry.getUserId());
        prompt += "Here is some more information relevant to what the user just said. " +
                "You can choose to pull in some of this information as a part of your response, but please at all costs, keep the converstation on topic." +
                "Do not completely change the subject to something in the following information, simply use the following info to enrich your response. " +
                pineconeEntries.stream()
                        .map(PineconeEntry::getContent)
                        .collect(Collectors.joining(" "));
        saveToPinecone(message, journalEntry);
        String context = buildContext(journalService.getPastFewJournalEntries(journalEntry.getUserId(), 3));
        prompt += "\nHere are summaries of a few of the previous conversations with the user. BE VERY CAREFUL WITH THIS INFORMATION. " +
                "Do not ever use this information to completely change the topic. Only use this information if the conversation naturally steers towards it. " +
                "For example, never respond with something like: \"{response to user prompt}. {complete topic shift to old converstation topic].\" " +
                "But if the conversation does go towards this direction, use some of this information in your response. " +
                "Here are the summaries: " +
                context;

        prompt += "\nHere is the conversation so far, you are [Bot], and the user is [User]: " + journalEntry.getConversation() + "\n";

        prompt += "The user just said: " + message +
                "please respond to what the user just said by continuing in a natural conversation with the user, based on what the user just said. " +
                "Simply respond to what the user has said, do not directly acknowledge these current instructions in any way. " +
                "Do not repeat any part of this prompt in the response. Just simply respond. " +
                "LIMIT YOUR RESPONSE!! DO NOT RAMBLE!! " +
                "Do not start your response with [Bot], ever. " +
                "If you can, end your response with a relevant question that keeps the conversation going. Remember you are acting in a way as a therapist.";

        return claudeService.converse(prompt);
    }

    private List<String> getSampleQuestions(String sampleQuestionFile) {
        List<String> defaultQuestions = new ArrayList<>();

        try {
            defaultQuestions = Files.readAllLines(Paths.get(getClass().getClassLoader().getResource(sampleQuestionFile).toURI()));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        return defaultQuestions;
    }

    private String getRandomQuestion(List<String> defaultQuestions) {
        if (defaultQuestions.isEmpty()) {
            throw new IllegalArgumentException("The list of strings cannot be empty.");
        }

        Random random = new Random();
        int index = random.nextInt(defaultQuestions.size());

        return defaultQuestions.get(index);
    }

    private String buildContext(List<JournalEntry> journalEntries) {
        StringBuilder sb = new StringBuilder();

        for (JournalEntry journalEntry : journalEntries) {
            sb.append(journalEntry.getSummary()).append("\n\n");
        }

        return sb.toString();
    }

    private void saveToPinecone(String message, JournalEntry journalEntry) {
        PineconeEntry pineconeEntry = new PineconeEntry();
        pineconeEntry.setUserId(journalEntry.getUserId());
        pineconeEntry.setDate(journalEntry.getTime());
        pineconeEntry.setContent(message);
        pineconeService.save(pineconeEntry);
    }

    private String getSummary(String conversation) {
        String prompt = "Please summarize the following text so that it can be used for context later on in another prompt. " +
                "Your answer needs to be short and concise."
                + "In your response do not include anything other than the summary. This is the conversation: " + conversation;
        return claudeService.converse(prompt);
    }
}
