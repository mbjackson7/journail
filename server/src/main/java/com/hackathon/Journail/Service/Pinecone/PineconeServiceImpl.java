package com.hackathon.Journail.Service.Pinecone;

import com.hackathon.Journail.Model.PineconeEntry;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PineconeServiceImpl implements PineconeService {
    private final String USER_ID = "userId";
    private final String DATE = "date";
    private final double SIMILARITY_THRESHOLD = 0.4;
    private final VectorStore pineconeStore;

    @Autowired
    public PineconeServiceImpl(VectorStore pineconeStore) {
        this.pineconeStore = pineconeStore;
    }


    @Override
    public List<PineconeEntry> get(String query, String userId) {
        List<Document> results = pineconeStore.similaritySearch(
                SearchRequest.defaults()
                        .withQuery(query)
                        .withSimilarityThreshold(SIMILARITY_THRESHOLD)
                        .withFilterExpression(String.format("%s == '%s'", USER_ID, userId)));

        return results.stream().map(this::documentToEntry).collect(Collectors.toList());
    }

    @Override
    public List<PineconeEntry> getByDate(String query, String userId, String date) {
        List<Document> results = pineconeStore.similaritySearch(
                SearchRequest.defaults()
                        .withQuery(query)
                        .withSimilarityThreshold(SIMILARITY_THRESHOLD)
                        .withFilterExpression(String.format(
                                "%s == '%s' && %s == '%s'",
                            USER_ID, userId, DATE, date)));

        return results.stream().map(this::documentToEntry).collect(Collectors.toList());
    }

    @Override
    public void save(PineconeEntry newEntry) {
        Document toStore = new Document(newEntry.getContent(), Map.of(
                USER_ID, newEntry.getUserId(),
                DATE, newEntry.getDate()));
        pineconeStore.add(List.of(toStore));
    }

    private PineconeEntry documentToEntry(Document document) {
        Map<String, Object> metadata = document.getMetadata();

        return new PineconeEntry(
                metadata.get(USER_ID).toString(),
                document.getContent(),
                metadata.get(DATE).toString());
    }
}
