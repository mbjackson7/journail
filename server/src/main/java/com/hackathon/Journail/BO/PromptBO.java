package com.hackathon.Journail.BO;

import com.hackathon.Journail.Model.JournalEntry;
import com.hackathon.Journail.Service.ClaudeHaiku;
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

    public String getStarterQuestion(JournalEntry journalEntry) {
        List<String> defaultOpeners = getSampleQuestions("sampleopeners.txt");

        //prompt bedrock for opener based on context and random default opener here
        String defaultStarterQuestion = getRandomQuestion(defaultOpeners);
        String starterPrompt = "Give me something similar to the following question: " + defaultStarterQuestion;

        return ClaudeHaiku.converse(starterPrompt);
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

}
