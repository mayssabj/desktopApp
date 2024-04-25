package edu.esprit.services;

import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface IAvertissementService<Avertissement> {

    void ajouterAvertissement(Avertissement avertissement) throws SQLException;
    public ObservableList<Avertissement> afficherAvertissement() throws SQLException;
    public void supprimerAvertissement(int id) throws SQLException;
    public void updateAvertissement(Avertissement avertissement) throws SQLException;
}
