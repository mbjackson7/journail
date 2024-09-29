package com.hackathon.Journail.BO;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class PromptBOTests {
    @Test
    void getStarterQuestionTest_notNullResponse() {
        PromptBO promptBO = new PromptBO();
        String testStarterQuestion = promptBO.getStarterQuestion();
        assertThat(testStarterQuestion).isNotNull();
    }
}
