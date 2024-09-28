package com.hackathon.Journail.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="journal-entries")
public class JournalEntry {

    @Id
    private String id;
    private String conversationText;


}
