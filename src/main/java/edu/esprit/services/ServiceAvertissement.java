package edu.esprit.services;

import edu.esprit.entities.Avertissement;
import edu.esprit.utils.DBConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServiceAvertissement implements IAvertissementService<Avertissement> {
    private Connection connection = DBConnector.getCon();

    @Override
    public void ajouterAvertissement(Avertissement o) throws SQLException {
        String query = "INSERT INTO avertissement (reported_username, confirmation, screen_shot, raison) VALUES (?,?,?,?)";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, o.getReported_username());
            pst.setInt(2, o.getConfirmation());
            pst.setString(3, o.getScreenshot());
            pst.setString(4, o.getRaison());
            pst.executeUpdate();
        }
    }

    @Override
    public ObservableList<Avertissement> afficherAvertissement() throws SQLException {
        return afficherAvertissement(0, Integer.MAX_VALUE);  // Return all records if no pagination specified
    }

    public ObservableList<Avertissement> afficherAvertissement(int offset, int limit) throws SQLException {
        ObservableList<Avertissement> avertissements = FXCollections.observableArrayList();
        String query = "SELECT * FROM avertissement LIMIT ? OFFSET ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, limit);
            pst.setInt(2, offset);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Avertissement avertissement = new Avertissement();
                    avertissement.setId(rs.getInt("id"));
                    avertissement.setReported_username(rs.getString("reported_username"));
                    avertissement.setConfirmation(rs.getInt("confirmation"));
                    avertissement.setScreenshot(rs.getString("screen_shot"));
                    avertissement.setRaison(rs.getString("raison"));
                    avertissements.add(avertissement);
                }
            }
        }
        return avertissements;
    }

    @Override
    public void supprimerAvertissement(int id) throws SQLException {
        String query = "DELETE FROM avertissement WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }

    @Override
    public void updateAvertissement(Avertissement avertissement) throws SQLException {
        String query = "UPDATE avertissement SET confirmation = ? WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, avertissement.getConfirmation());
            pst.setInt(2, avertissement.getId());
            pst.executeUpdate();
        }
    }

    public int countAvertissements() {
        String query = "SELECT COUNT(*) FROM avertissement";
        try (PreparedStatement pst = connection.prepareStatement(query); ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
