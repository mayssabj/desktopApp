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
        PreparedStatement pst = connection.prepareStatement(query);
        Avertissement avertissement = (Avertissement) o;
        pst.setString(1, avertissement.getReported_username());
        pst.setInt(2, avertissement.getConfirmation());
        pst.setString(3, avertissement.getScreenshot());
        pst.setString(4, avertissement.getRaison());
        pst.executeUpdate();
    }

    @Override
    public ObservableList<Avertissement> afficherAvertissement() throws SQLException {
        ObservableList<Avertissement> avertissements = FXCollections.observableArrayList();
        String query = "SELECT * FROM avertissement";
        try (PreparedStatement st = connection.prepareStatement(query); ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                Avertissement avertissement = new Avertissement();
                avertissement.setId(rs.getInt("id")); // Ajoutez cette ligne pour récupérer l'ID
                avertissement.setReported_username(rs.getString("reported_username"));
                avertissement.setConfirmation(rs.getInt("confirmation"));
                avertissement.setScreenshot(rs.getString("screen_shot"));
                avertissement.setRaison(rs.getString("raison"));

                avertissements.add(avertissement);
            }
        }
        return avertissements;
    }

    @Override
    public void supprimerAvertissement(int id) throws SQLException {
        String query = "DELETE FROM avertissement WHERE id = ?";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setInt(1, id);
        pst.executeUpdate();
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
}





