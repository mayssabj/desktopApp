package edu.esprit.services;



import edu.esprit.entities.Reclamation;
import edu.esprit.utils.DBConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServiceReclamation implements IReclamationService<Reclamation>{

    private Connection connection = DBConnector.getCon();

  

    @Override
    public void ajouterReclamation(Reclamation reclamation) throws SQLException {
        String query = "INSERT INTO reclamation(subject,description,reported_username,type_reclamation,screen_shot) VALUES(?,?,?,?,?)";
        String queryAvertissement = "INSERT INTO avertissement(reported_username, confirmation, screen_shot, raison) VALUES(?,?,?,?)";
        try (PreparedStatement st = connection.prepareStatement(query);
        PreparedStatement stAvertissement = connection.prepareStatement(queryAvertissement)) {
            st.setString(1, reclamation.getSubjuct());
            st.setString(2, reclamation.getDescription());
            st.setString(3, reclamation.getReported_username());
            st.setString(4, reclamation.getType_reclamation());
            st.setString(5, reclamation.getScreenshot());
            st.executeUpdate();
            //ajout avertissement
            stAvertissement.setString(1, reclamation.getReported_username());
            stAvertissement.setInt(2, 0); // confirmation
            stAvertissement.setString(3, reclamation.getScreenshot());
            stAvertissement.setString(4, reclamation.getType_reclamation());
            stAvertissement.executeUpdate();
        }


    }

    @Override
    public void modifierReclamation(Reclamation reclamation) throws SQLException {
        String query = "UPDATE reclamation SET subject=?, description=?, reported_username=?, type_reclamation=?, screen_shot=? WHERE id=?";
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1, reclamation.getSubjuct());
            st.setString(2, reclamation.getDescription());
            st.setString(3, reclamation.getReported_username());
            st.setString(4, reclamation.getType_reclamation());
            st.setString(5, reclamation.getScreenshot());
            st.setInt(6, reclamation.getId());
            st.executeUpdate();
        }

    }

    @Override
    public void supprimerReclamation(int id) throws SQLException {
        String query = "DELETE FROM reclamation WHERE id=?";
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setInt(1, id);
            st.executeUpdate();
        }

    }

    @Override
    public ObservableList<Reclamation> afficherReclamation() throws SQLException {
        ObservableList<Reclamation> reclamations = FXCollections.observableArrayList();
        String query = "SELECT * FROM reclamation";
        try (PreparedStatement st = connection.prepareStatement(query); ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                Reclamation reclamation = new Reclamation();
                // Set attributes of reclamation from ResultSet

                reclamation.setId(rs.getInt("id"));
                reclamation.setSubjuct(rs.getString("subject"));
                reclamation.setDescription(rs.getString("description"));
                reclamation.setReported_username(rs.getString("reported_username"));
                reclamation.setType_reclamation(rs.getString("type_reclamation"));
                reclamation.setScreenshot(rs.getString("screen_shot"));


                reclamations.add(reclamation);
            }
        }
        return reclamations;
    }




}
