package com.hackathon.Journail.Service.Pinecone;

import com.hackathon.Journail.Model.PineconeEntry;

import java.util.List;

public interface PineconeService {
    List<PineconeEntry> get(String query, String userId);

    List<PineconeEntry> getByDate(String query, String userId, String date);

    void save(PineconeEntry newEntry);
}
