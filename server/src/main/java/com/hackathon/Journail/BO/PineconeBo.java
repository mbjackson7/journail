package com.hackathon.Journail.BO;

import java.util.List;

public interface PineconeBo {
    List<String> get(String query, String userId);

    void save(String textToEmbed, String userId);
}
