package com.hackathon.Journail.Controller;

import com.hackathon.Journail.BO.PineconeBo;
import com.hackathon.Journail.BO.PineconeEntry;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.List;

@RestController
@RequestMapping("/embed")
public class PineconeController {

    private final PineconeBo pineconeBo;

    public PineconeController(PineconeBo pineconeBo) {
        this.pineconeBo = pineconeBo;
    }

    @PostMapping("/save-vector")
    public void saveToVectorStore(@RequestBody PineconeEntry newEntry) {
        pineconeBo.save(newEntry);
    }

    @PostMapping("/check-vector")
    public List<PineconeEntry> checkVectorStore(@RequestBody String query, @RequestParam("userId") String userId) {
        return pineconeBo.get(query, userId);
    }

    @PostMapping("/check-vector-by-date")
    public List<PineconeEntry> checkVectorStoreByDate(@RequestBody String query, @RequestParam("userId") String userId, @RequestParam("date") String date) {
        return pineconeBo.getByDate(query, userId, date);
    }
}