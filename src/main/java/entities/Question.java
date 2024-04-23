package entities;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDateTime;

public class Question {
    private int id;
    private int userId;
    private StringProperty title;
    private StringProperty body;
    private LocalDateTime createdAt;
    private Answer answer;

        public Question(int id, int userId, String title, String body, LocalDateTime createdAt, Answer answer) {
            this.id = id;
            this.userId = userId;
            this.title = new SimpleStringProperty(title);
            this.body = new SimpleStringProperty(body);
            this.createdAt = createdAt;
            this.answer = answer;
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

}
