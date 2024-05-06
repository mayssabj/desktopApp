package edu.esprit.entities;

import java.time.LocalDateTime;

public class Answer {
    private int id;
    private int userId;
    private int questionId;
    private String body;
    private LocalDateTime createdAt;

    public Answer(int id, int userId, int questionId, String body, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.questionId = questionId;
        this.body = body;
        this.createdAt = createdAt;
    }
    public Answer(int userId, int questionId, String body, LocalDateTime createdAt) {
        this.userId = userId;
        this.questionId = questionId;
        this.body = body;
        this.createdAt = createdAt;
    }
    public Answer(int id, String body) {
        this.id = id;
        this.body = body;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    @Override
    public String toString() {
        return  "Response: " + body;
    }

}
