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
        List<String> defaultOpeners = new ArrayList<>();
        String sampleOpeners = "sampleopeners.txt";
        try {
            defaultOpeners = Files.readAllLines(Paths.get(getClass().getClassLoader().getResource(sampleOpeners).toURI()));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        //prompt bedrock for opener based on context
        String starterQuestion = "HOW ARE YOUR BALLS TODAY?!?";

        return starterQuestion;
    }

}
