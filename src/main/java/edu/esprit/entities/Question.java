package edu.esprit.entities;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDateTime;

public class Question {
    private int id;
    private int userId;
    private StringProperty username; // new field for username
    private StringProperty title;
    private StringProperty body;
    private LocalDateTime createdAt;
    private Answer answer;

    public Question(int id, int userId, String username, String title, String body, LocalDateTime createdAt, Answer answer) {
        this.id = id;
        this.userId = userId;
        this.username = new SimpleStringProperty(username); // initialize username
        this.title = new SimpleStringProperty(title);
        this.body = new SimpleStringProperty(body);
        this.createdAt = createdAt;
        this.answer = answer;
    }

    public String getUsername() {
        return username.get();
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public void setUsername(String username) {
        this.username.set(username);
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

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getBody() {
        return body.get();
    }

    public StringProperty bodyProperty() {
        return body;
    }

    public void setBody(String body) {
        this.body.set(body);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    // Add this method to return the answer string
    public String getAnswerBody() {
        return answer != null ? answer.getBody() : "No answer yet";
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Question Details:\n");
        sb.append("Asked by: ").append(getUsername()).append("\n"); // changed from username.toString() to getUsername()
        sb.append("Title: ").append(getTitle()).append("\n");
        sb.append("Body: ").append(getBody()).append("\n");
        sb.append("Created At: ").append(createdAt).append("\n");
        sb.append("Answer: ").append(getAnswerBody()).append("\n");
        return sb.toString();
    }


}
