package com.hackathon.Journail.Controller;


import com.hackathon.Journail.Model.JournalEntry;
import com.hackathon.Journail.Service.JournalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class JournalEndpoint {

    private final JournalService journalService;

    @Autowired
    public JournalEndpoint(JournalService journalService) {
        this.journalService = journalService;
    }

    @GetMapping("/journals")
    public ResponseEntity<List<JournalEntry>> getJournals() {
        return ResponseEntity.status(HttpStatus.OK).body(journalService.getJournalEntries());
    }

}
