package com.hackathon.Journail.Repository;

import com.hackathon.Journail.Model.JournalEntry;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JournalEntryRepository extends MongoRepository<JournalEntry, String> {
}
