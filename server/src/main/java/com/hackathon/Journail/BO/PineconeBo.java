package com.hackathon.Journail.BO;

import java.util.List;

public interface PineconeBo {
    List<String> get(String query);

    void save(String textToEmbed);
}
