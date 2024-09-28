package com.hackathon.Journail.BO;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Component
public class PromptBO {

    public String getStarterQuestion() {
        //pull from sampleopeners
        String fileName = "resources/sampleopeners.txt";
        try {
            List<String> defaultOpeners = Files.readAllLines(Paths.get(getClass().getClassLoader().getResource(fileName).toURI()));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        //prompt bedrock for opener based on context
        String starterQuestion = "";

        return starterQuestion;
    }

}
