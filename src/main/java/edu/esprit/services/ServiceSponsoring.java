package edu.esprit.services;

import edu.esprit.entities.Sponsoring;

import java.sql.SQLException;
import java.util.List;

public interface ServiceSponsoring <S> {
    public void ajouterSponsoring (S s) throws SQLException;
    public Sponsoring modifierSponsoring (S s) throws SQLException;
    public void supprimerSponsoring (int id) throws SQLException;
    public List<Sponsoring> afficherSponsoring () throws SQLException;


}
