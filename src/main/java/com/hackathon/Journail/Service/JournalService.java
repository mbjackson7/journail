package com.hackathon.Journail.Service;

import com.hackathon.Journail.Model.JournalEntry;
import java.util.List;

public interface JournalService {

    List<JournalEntry> getJournalEntries();

    JournalEntry getJournalEntryById(String id);

    JournalEntry updateJournalEntry(JournalEntry journalEntry);

    void createJournalEntry(JournalEntry journalEntry);

    List<JournalEntry> getJournalEntries(String userId);

    JournalEntry getJournalEntry(String userId, String time);

}
