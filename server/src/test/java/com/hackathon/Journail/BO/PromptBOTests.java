package com.hackathon.Journail.BO;

import com.hackathon.Journail.Model.JournalEntry;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class PromptBOTests {

    @MockBean
    private JournalEntry journalEntry;

    @Test
    void getStarterQuestionTest_notNullResponse() {
        PromptBO promptBO = new PromptBO();
        String testStarterQuestion = promptBO.getStarterQuestion(journalEntry);
        assertThat(testStarterQuestion).isNotNull();
    }

    @Test
    void getCloserQuestionTest_notNullResponse() {
        PromptBO promptBO = new PromptBO();
        String testStarterQuestion = promptBO.getCloserQuestion(journalEntry);
        assertThat(testStarterQuestion).isNotNull();
    }
}
