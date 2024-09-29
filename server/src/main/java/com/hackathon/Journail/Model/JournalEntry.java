package com.hackathon.Journail.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Objects;

@Document(collection="journal-entries")
public class JournalEntry {

    @Id
    private String id;
    private String userId;
    private String time;
    private String conversation;
    private String summary;
    private String shortSummary;
    private List<String> keywords;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getConversation() {
        return conversation;
    }

    public void setConversation(String conversation) {
        if (this.conversation == null) {
            this.conversation = "";
        }
        this.conversation += conversation;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getShortSummary() {
        return shortSummary;
    }

    public void setShortSummary(String shortSummary) {
        this.shortSummary = shortSummary;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public void appendConversation(String message) {
        if (this.conversation == null) {
            this.conversation = "";
        }
        this.conversation += message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JournalEntry that = (JournalEntry) o;
        return Objects.equals(id, that.id) && Objects.equals(userId, that.userId) && Objects.equals(time, that.time) && Objects.equals(conversation, that.conversation) && Objects.equals(summary, that.summary) && Objects.equals(shortSummary, that.shortSummary) && Objects.equals(keywords, that.keywords);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, time, conversation, summary, shortSummary, keywords);
    }


}
