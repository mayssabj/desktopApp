package Services;

import entities.Question;
import utils.Db;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class QuestionCRUD {

    private Connection connection;

    public QuestionCRUD() {
        connection = Db.getInstance().getCnx();
    }

    public void ajouter(Question question) throws SQLException {
        String req = "INSERT INTO `question`(`userId`, `title`, `body`, `createdAt`) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setInt(1, 1); // User ID is always 1
            pst.setString(2, question.getTitle());
            pst.setString(3, question.getBody());
            pst.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now())); // Created at is always now
            pst.executeUpdate();
        }
    }

    public void modifier(Question question) throws SQLException {
        String req = "UPDATE `question` SET `userId`=?, `title`=?, `body`=?, `createdAt`=? WHERE `id`=?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setInt(1, question.getUserId());
            pst.setString(2, question.getTitle());
            pst.setString(3, question.getBody());
            pst.setTimestamp(4, Timestamp.valueOf(question.getCreatedAt()));
            pst.setInt(5, question.getId());
            pst.executeUpdate();
        }
    }

    public void supprimer(int id) throws SQLException {
        String req = "DELETE FROM `question` WHERE `id`=?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }

    public List<Question> afficher() throws SQLException {
        List<Question> questions = new ArrayList<>();
        String req = "SELECT * FROM `question`";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(req)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                int userId = rs.getInt("userId");
                String title = rs.getString("title");
                String body = rs.getString("body");
                LocalDateTime createdAt = rs.getTimestamp("createdAt").toLocalDateTime();
                questions.add(new Question(id, userId, title, body, createdAt, null));
            }
        }
        return questions;
    }
}
