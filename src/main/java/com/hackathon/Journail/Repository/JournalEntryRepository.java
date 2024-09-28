package com.hackathon.Journail.Repository;

import com.hackathon.Journail.Model.JournalEntry;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface JournalEntryRepository extends MongoRepository<JournalEntry, String> {

    List<JournalEntry> findByUserIdAndTime(String userId, String time);
    List<JournalEntry> findByUserId(String userId);
}
