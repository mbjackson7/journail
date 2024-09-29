package com.hackathon.Journail.Controller;

import com.hackathon.Journail.BO.PineconeBo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PineconeController {

    private final PineconeBo pineconeBo;

    public PineconeController(PineconeBo pineconeBo) {
        this.pineconeBo = pineconeBo;
    }

    @PostMapping("/save-vector")
    public void saveToVectorStore(@RequestBody String textToEmbed, @RequestParam("userId") String userId) {
        pineconeBo.save(textToEmbed, userId);
    }

    @GetMapping("/check-vector")
    public List<String> checkVectorStore(@RequestBody String query, @RequestParam("userId") String userId) {
        return pineconeBo.get(query, userId);
    }
}