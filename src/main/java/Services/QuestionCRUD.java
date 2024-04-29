package Services;

import entities.Answer;
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

    public void ajouterQuestion(Question question) {
        String req = "INSERT INTO `question` (`userId`, `title`, `body`, `createdAt`) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setInt(1, question.getUserId());
            pst.setString(2, question.getTitle());
            pst.setString(3, question.getBody());
            pst.setTimestamp(4, Timestamp.valueOf(question.getCreatedAt()));
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void supprimer(int id) throws SQLException {
        String req = "DELETE FROM `question` WHERE `id`=?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Question getQuestionById(int id) {
        String query = "SELECT q.*, u.username, a.body as answerBody FROM question q INNER JOIN user u ON q.userId = u.id LEFT JOIN answer a ON q.answerId = a.id WHERE q.id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("userId");
                String username = rs.getString("username");
                String title = rs.getString("title");
                String body = rs.getString("body");
                LocalDateTime createdAt = rs.getTimestamp("createdAt").toLocalDateTime();
                Answer answer = null;
                if (rs.getInt("answerId") != 0) {
                    answer = new Answer(rs.getInt("answerId"), rs.getString("answerBody"));
                }
                return new Question(id, userId, username, title, body, createdAt, answer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // return null if there was an error or the question was not found
    }



    public List<Question> afficher() throws SQLException {
        List<Question> questions = new ArrayList<>();
        String query = "SELECT q.*, u.username, a.body as answerBody FROM question q INNER JOIN user u ON q.userId = u.id LEFT JOIN answer a ON q.answerId = a.id";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            int id = rs.getInt("id");
            int userId = rs.getInt("userId");
            String username = rs.getString("username");
            String title = rs.getString("title");
            String body = rs.getString("body");
            LocalDateTime createdAt = rs.getTimestamp("createdAt").toLocalDateTime();
            Answer answer = null;
            if (rs.getInt("answerId") != 0) {
                answer = new Answer(rs.getInt("answerId"), rs.getString("answerBody"));
            }
            questions.add(new Question(id, userId, username, title, body, createdAt, answer));
        }

        return questions;
    }
}
