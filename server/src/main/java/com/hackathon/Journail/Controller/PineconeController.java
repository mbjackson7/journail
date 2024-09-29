package com.hackathon.Journail.Controller;

import com.hackathon.Journail.BO.PineconeBo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PineconeController {

    private final PineconeBo pineconeBo;

    public PineconeController(PineconeBo pineconeBo) {
        this.pineconeBo = pineconeBo;
    }

    @PostMapping("/save-vector")
    public void saveToVectorStore(@RequestBody String textToEmbed) {
        pineconeBo.save(textToEmbed);
    }

    @GetMapping("/check-vector")
    public List<String> checkVectorStore(@RequestBody String query) {
        return pineconeBo.get(query);
    }
}