package com.hackathon.Journail.Controller;


import com.hackathon.Journail.BO.PromptBO;
import com.hackathon.Journail.Model.JournalEntry;
import com.hackathon.Journail.Service.JournalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/journals")
public class JournalEndpoint {

    private final JournalService journalService;
    private final PromptBO promptBO;

    @Autowired
    public JournalEndpoint(JournalService journalService, PromptBO promptBO) {
        this.journalService = journalService;
        this.promptBO = promptBO;
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

    @PostMapping("/start-conversation")
    public ResponseEntity<String> startConversation(@RequestBody JournalDTO journalDTO) {
        if (journalDTO.getUserId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Body is missing userId");
        } else if (journalDTO.getTime() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Body is missing time");
        }

        JournalEntry journalEntry = new JournalEntry();
        journalEntry.setUserId(journalDTO.getUserId());
        journalEntry.setTime(journalDTO.getTime());

        String starterQuestion = promptBO.getStarterQuestion(journalEntry);
        journalEntry.appendConversation("[Bot] " + starterQuestion);
        journalService.createJournalEntry(journalEntry);
        return ResponseEntity.status(HttpStatus.OK).body(starterQuestion);
    }

    @PostMapping("/send-message")
    public ResponseEntity<?> sendMessage(@RequestBody JournalDTO journalDTO) {
        JournalEntry existingEntry = journalService.getJournalEntry(journalDTO.getUserId(), journalDTO.getTime());
        if (existingEntry == null) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Journal Entry does not exist. Have you called start-conversation yet?");
        }
        if (journalDTO.getMessage() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Body is missing message.");
        }

        existingEntry.appendConversation("[User] " + journalDTO.getMessage());
        String botMessage = "Send Message Test";
        existingEntry.appendConversation("[Bot] " + botMessage);
        journalService.updateJournalEntry(existingEntry);
        //CALL INTO BO LOGIC HERE
        return ResponseEntity.status(HttpStatus.OK).body(botMessage);
    }

    @PostMapping("/initiate-end-conversation")
    public ResponseEntity<?> initiateEndConversation(@RequestBody JournalDTO journalDTO) {
        if (journalDTO.getUserId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Body is missing userId");
        } else if (journalDTO.getTime() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Body is missing time");
        }

        JournalEntry existingEntry = journalService.getJournalEntry(journalDTO.getUserId(), journalDTO.getTime());
        if (existingEntry == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Journal Entry does not exist. Have you called start-conversation yet?");
        }

        // CALL INTO BO LOGIC HERE
        String closerQuestion = promptBO.getCloserQuestion(existingEntry);
        existingEntry.appendConversation("[BOT] " + closerQuestion);
        journalService.updateJournalEntry(existingEntry);

        return ResponseEntity.status(HttpStatus.OK).body(closerQuestion);
    }

    @PostMapping("/end-conversation")
    public ResponseEntity<?> endConversation(@RequestBody JournalDTO journalDTO) {
        if (journalDTO.getUserId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Body is missing userId");
        } else if (journalDTO.getTime() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Body is missing time");
        } else if (journalDTO.getMessage() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Body is missing message");
        }
        JournalEntry journalEntry = journalService.getJournalEntry(journalDTO.getUserId(), journalDTO.getTime());
        if (journalEntry == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Journal Entry not found");
        }

        journalEntry.appendConversation("[User] " + journalDTO.getMessage());
        journalService.updateJournalEntry(journalEntry);

        // CALL INTO BO LOGIC HERE>

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping("/query")
    public ResponseEntity<?> query(@RequestBody JournalDTO journalDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        String healthStatus = "{\"status\":\"UP\"}";
        return ResponseEntity.status(HttpStatus.OK).body(healthStatus);
    }
}
