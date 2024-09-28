package com.hackathon.Journail.Service;

import com.hackathon.Journail.Model.JournalEntry;
import java.util.List;

public interface JournalService {

    public List<JournalEntry> getJournalEntries();

    public JournalEntry getJournalEntryById(String id);

    public void updateJournalEntry(JournalEntry journalEntry);

    public void createJournalEntry(JournalEntry journalEntry);
}
