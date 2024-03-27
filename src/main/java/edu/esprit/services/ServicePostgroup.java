package edu.esprit.services;

import edu.esprit.entities.Post_group;

import java.sql.SQLException;
import java.util.List;

public interface ServicePostgroup <P> {
    public void ajouter (P p) throws SQLException;
    public void modifier (P p) throws SQLException;
    public void supprimer (int id) throws SQLException;
    public List<Post_group> afficher () throws SQLException;
}
