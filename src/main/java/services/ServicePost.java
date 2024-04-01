package services;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public interface ServicePost<T>{

    public void ajouter(T t) throws SQLException;
    public void modifier(T t) throws SQLException;
    public  void  supprimer(int id) throws SQLException;

    void initialize(URL location, ResourceBundle resources);


    List<T> afficher() throws SQLException;

}
