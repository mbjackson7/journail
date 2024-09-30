package com.hackathon.Journail.Bo;

import com.hackathon.Journail.Model.JournalEntry;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;


@ExtendWith(MockitoExtension.class)
public class PromptBOTests {

    @MockBean
    private JournalEntry journalEntry;

    @InjectMocks
    private PromptBO promptBO;
}
