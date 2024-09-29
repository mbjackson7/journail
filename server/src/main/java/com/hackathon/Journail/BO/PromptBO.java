package com.hackathon.Journail.BO;

import com.hackathon.Journail.Model.JournalEntry;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class PromptBO {

    public String getStarterQuestion(JournalEntry journalEntry) {
        List<String> defaultOpeners = getSampleQuestions("sampleopeners.txt");

        //prompt bedrock for opener based on context and random default opener here
        String starterQuestion = "HOW ARE YOUR BALLS TODAY?!?";

        return starterQuestion;
    }

    public String getCloserQuestion(JournalEntry journalEntry) {
        List<String> defaultClosers = getSampleQuestions("sampleclosingquestions.txt");

        //prompt bedrock for closer based on context and random default closer here
        String closerQuestion = "WILL YOU PLEASE BRING ME GALAXY GAS NEXT TIME?!?!";

        return closerQuestion;
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

}
