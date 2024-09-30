package com.hackathon.Journail.Controller.Journal;

import java.util.Objects;

public class JournalDTO {

    private String userId;
    private String time;
    private String message;
    private String query;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JournalDTO that = (JournalDTO) o;
        return Objects.equals(userId, that.userId) && Objects.equals(time, that.time) && Objects.equals(message, that.message) && Objects.equals(query, that.query);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, time, message, query);
    }
}
