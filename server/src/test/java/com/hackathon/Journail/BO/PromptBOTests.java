package com.hackathon.Journail.BO;

import com.hackathon.Journail.Model.JournalEntry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class PromptBOTests {

    @MockBean
    private JournalEntry journalEntry;

    @InjectMocks
    private PromptBO promptBO;


    @Test
    void getStarterQuestionTest_notNullResponse() {
        String testStarterQuestion = promptBO.getStarterQuestion(journalEntry);
        assertThat(testStarterQuestion).isNotNull();
    }

    @Test
    void getCloserQuestionTest_notNullResponse() {
        String testCloserQuestion = promptBO.getCloserQuestion(journalEntry);
        assertThat(testCloserQuestion).isNotNull();
    }
}
