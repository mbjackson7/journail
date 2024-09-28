package com.hackathon.Journail.Controller;


import com.hackathon.Journail.Model.JournalEntry;
import com.hackathon.Journail.Service.JournalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/journals")
public class JournalEndpoint {

    private final JournalService journalService;

    @Autowired
    public JournalEndpoint(JournalService journalService) {
        this.journalService = journalService;
    }

    @GetMapping()
    public ResponseEntity<List<JournalEntry>> getJournals() {
        List<JournalEntry> journalEntries = journalService.getJournalEntries();
        if (journalEntries == null || journalEntries.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(journalService.getJournalEntries());
    }

    @PutMapping()
    public ResponseEntity<JournalEntry> updateJournal(@RequestBody JournalEntry journalEntry) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(journalService.updateJournalEntry(journalEntry));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping()
    public ResponseEntity<JournalEntry> createJournal(@RequestBody JournalEntry journalEntry) {
        try {
            journalService.createJournalEntry(journalEntry);
            return ResponseEntity.status(HttpStatus.CREATED).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{userId}/{time}")
    public ResponseEntity<JournalEntry> getJournalByUserIdAndTime(@PathVariable("userId") String userId,
                                                       @PathVariable("time") String time) {
        JournalEntry journalEntry = journalService.getJournalEntry(userId, time);
        if (journalEntry == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(journalEntry);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<JournalEntry>> getJournalsByUserId(@PathVariable("userId") String userId) {
        List<JournalEntry> journalEntries = journalService.getJournalEntries(userId);
        if (journalEntries == null || journalEntries.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(journalEntries);
    }

    @PostMapping("/send-message")
    public ResponseEntity<?> sendMessage(@RequestBody JournalDTO journalDTO) {
        //CALL INTO BO LOGIC HERE
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping("/end-conversation")
    public ResponseEntity<?> endConversation(@RequestBody JournalDTO journalDTO) {
        // CALL INTO BO LOGIC HERE>
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping("/query")
    public ResponseEntity<?> query(@RequestBody JournalDTO journalDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
