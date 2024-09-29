package com.hackathon.Journail.BO;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PineconeBoImpl implements PineconeBo {
    private final String USER_ID = "userId";
    private final VectorStore pineconeStore;

    public PineconeBoImpl(VectorStore pineconeStore) {
        this.pineconeStore = pineconeStore;
    }


    @Override
    public List<String> get(String query, String userId) {
        List<Document> results = pineconeStore.similaritySearch(
                SearchRequest.defaults()
                        .withQuery(query)
                        .withFilterExpression(String.format("%s == '%s'", USER_ID, userId)));

        return results.stream().map(Document::getContent).collect(Collectors.toList());
    }

    @Override
    public void save(String textToEmbed, String userId) {
        Document toStore = new Document(textToEmbed, Map.of(USER_ID, userId));
        pineconeStore.add(List.of(toStore));
    }
}
