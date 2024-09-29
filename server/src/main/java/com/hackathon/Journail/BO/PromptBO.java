package com.hackathon.Journail.BO;

import com.hackathon.Journail.Model.JournalEntry;
import com.hackathon.Journail.Service.ClaudeHaiku;
import com.hackathon.Journail.Service.JournalServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class PromptBO {

    private final ClaudeHaiku claudeHaiku;

    private final JournalServiceImpl journalService;

    @Autowired
    PromptBO(ClaudeHaiku claudeHaiku, JournalServiceImpl journalService) {
        this.claudeHaiku = claudeHaiku;
        this.journalService = journalService;
    }

    public String getStarterQuestion(JournalEntry journalEntry) {
        List<String> defaultOpeners = getSampleQuestions("sampleopeners.txt");

        String context = buildContext(journalService.getJournalEntries());

        //prompt bedrock for opener based on context and random default opener here
        String defaultStarterQuestion = getRandomQuestion(defaultOpeners);
        String starterPrompt =
                "You are an Ai chatbot for a journal ai application. Your purpose is to have a conversation with the user to facilitate journal entries via conversation with you." +
                        "Here is some previous context of past conversations. I want you to include this context when relevant in the following conversation we are going to have. The [Bot] lines were lines stated by you, while the [User] lines were stated by the user." +
                        context + "\n\n" +
                        "Give a similar question to the question that is listed in the following prompt, without revealing that you are replying to me. Just give me a similar question only. Remember the question should still pertain to today, recently, or near future. " +
                        "Do not say anything like \"Here's a similar question:\" Just give me the question only. Only one question.\n\n" +
                        "Question:" + defaultStarterQuestion;

        return claudeHaiku.converse(starterPrompt);
    }

    public String getCloserQuestion(JournalEntry journalEntry) {
        List<String> defaultClosers = getSampleQuestions("sampleclosingquestions.txt");

        //prompt bedrock for closer based on context and random default closer here

        return getRandomQuestion(defaultClosers);
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
            sb.append(journalEntry.getConversation()).append("\n\n");
        }

        return sb.toString();
    }

}
