package Services;

import entities.Answer;
import utils.Db;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class AnswerCRUD {

    private Connection connection;

    public Answer ajouterAnswer(Answer answer) {
        String req = "INSERT INTO `answer` (`userId`, `questionId`, `body`, `createdAt`) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setInt(1, answer.getUserId());
            pst.setInt(2, answer.getQuestionId());
            pst.setString(3, answer.getBody());
            pst.setTimestamp(4, Timestamp.valueOf(answer.getCreatedAt()));
            pst.executeUpdate();
            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                answer.setId(rs.getInt(1)); // set the generated ID on the Answer object
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return answer;
    }


    public void modifier(Answer answer) throws SQLException {
        String req = "UPDATE `answer` SET `userId`=?, `questionId`=?, `body`=?, `createdAt`=? WHERE `id`=?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setInt(1, answer.getUserId());
            pst.setInt(2, answer.getQuestionId());
            pst.setString(3, answer.getBody());
            pst.setTimestamp(4, Timestamp.valueOf(answer.getCreatedAt()));
            pst.setInt(5, answer.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void supprimer(int id) throws SQLException {
        String req = "DELETE FROM `answer` WHERE `id`=?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Answer> afficher() throws SQLException {
        List<Answer> answers = new ArrayList<>();
        String query = "SELECT * FROM answer";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            int id = rs.getInt("id");
            int userId = rs.getInt("userId");
            int questionId = rs.getInt("questionId");
            String body = rs.getString("body");
            LocalDateTime createdAt = rs.getTimestamp("createdAt").toLocalDateTime();
            answers.add(new Answer(id, userId, questionId, body, createdAt));
        }

        return answers;
    }
}
