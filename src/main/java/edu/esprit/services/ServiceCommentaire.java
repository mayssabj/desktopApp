package edu.esprit.services;

import edu.esprit.entities.Post_group;
import edu.esprit.entities.Postcommentaire;

import java.sql.SQLException;
import java.util.List;

public interface ServiceCommentaire <C> {
    public void ajouterCommentaire (C c) throws SQLException;
    public void modifierCommentaire (C c) throws SQLException;
    public void supprimerCommentaire (int id) throws SQLException;
    public List<Postcommentaire> afficherCommentaire () throws SQLException;
}
