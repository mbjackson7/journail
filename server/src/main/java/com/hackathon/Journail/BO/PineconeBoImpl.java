package com.hackathon.Journail.BO;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PineconeBoImpl implements PineconeBo {
    private final VectorStore pineconeStore;

    public PineconeBoImpl(VectorStore pineconeStore) {
        this.pineconeStore = pineconeStore;
    }


    @Override
    public List<String> get(String query) {
        List<Document> results = pineconeStore.similaritySearch(query);

        return results.stream().map(Document::getContent).collect(Collectors.toList());
    }

    @Override
    public void save(String textToEmbed) {
        pineconeStore.add(List.of(new Document(textToEmbed)));
    }
}
