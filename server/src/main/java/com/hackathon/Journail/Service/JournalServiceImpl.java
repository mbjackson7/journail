package com.hackathon.Journail.Service;
import com.hackathon.Journail.Model.JournalEntry;
import com.hackathon.Journail.Repository.JournalEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class JournalServiceImpl implements JournalService{

    private JournalEntryRepository journalEntryRepository;

    @Autowired
    public JournalServiceImpl(JournalEntryRepository journalEntryRepository) {
        this.journalEntryRepository = journalEntryRepository;
    }

    @Override
    public List<JournalEntry> getJournalEntries() {
        return journalEntryRepository.findAll();
    }

    @Override
    public JournalEntry getJournalEntryById(String id) {
        return journalEntryRepository.findById(id).orElse(null);
    }

    @Override
    public void updateJournalEntry(JournalEntry journalEntry) {
        journalEntryRepository.save(journalEntry);
    }

    @Override
    public void createJournalEntry(JournalEntry journalEntry) {
        journalEntryRepository.save(journalEntry);
    }

}