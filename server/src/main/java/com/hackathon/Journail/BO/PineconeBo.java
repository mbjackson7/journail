package com.hackathon.Journail.BO;

import java.util.List;

public interface PineconeBo {
    List<PineconeEntry> get(String query, String userId);

    List<PineconeEntry> getByDate(String query, String userId, String date);

    void save(PineconeEntry newEntry);
}
