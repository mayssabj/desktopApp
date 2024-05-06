package edu.esprit.services;

import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface IReclamationService<Reclamation> {


    void ajouterReclamation(Reclamation reclamation) throws SQLException;

    public void modifierReclamation(Reclamation reclamation) throws SQLException;
    public void supprimerReclamation(int id) throws SQLException;
    public ObservableList<Reclamation> afficherReclamation() throws SQLException;
}
