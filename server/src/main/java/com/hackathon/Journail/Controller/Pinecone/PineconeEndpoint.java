package com.hackathon.Journail.Controller.Pinecone;

import com.hackathon.Journail.Service.Pinecone.PineconeService;
import com.hackathon.Journail.Model.PineconeEntry;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RestController
@RequestMapping("/embed")
public class PineconeEndpoint {

    private final PineconeService pineconeBo;

    public PineconeEndpoint(PineconeService pineconeBo) {
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