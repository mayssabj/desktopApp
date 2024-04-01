package edu.esprit.services;



import edu.esprit.entities.Sponsoring;
import edu.esprit.utils.mydb;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.sql.DriverManager.getConnection;

public class SponsoringService implements ServiceSponsoring<Sponsoring> {
    private Connection con ;
    Statement ste ;
    public SponsoringService (){
        con = mydb.getInstance().getCon();
    }
    @Override
    public void ajouterSponsoring (Sponsoring sponsoring) throws SQLException {
        String req = "INSERT INTO `sponsoring`(`name`, `description`, `image`, `date`, `contrat`, `type`) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement sp = con.prepareStatement(req)) {
            sp.setString(1, sponsoring.getName());
            sp.setString(2, sponsoring.getDescription());
            sp.setString(3, sponsoring.getImage());
            sp.setDate(4, new java.sql.Date(sponsoring.getDate().getTime()));
            sp.setString(5, sponsoring.getContrat().toString());
            sp.setString(6, sponsoring.getType().toString());
            sp.executeUpdate();
        }
    }

    @Override
    public Sponsoring modifierSponsoring (Sponsoring sponsoring) throws SQLException {
        String req ="UPDATE `sponsoring` SET `name`=?,`description`=?,`image`=?,`date`= ?,`contrat`=?,`type`=? WHERE `id`=?";
        try (PreparedStatement sp = con.prepareStatement(req)) {
            sp.setString(1, sponsoring.getName());
            sp.setString(2, sponsoring.getDescription());
            sp.setString(3, sponsoring.getImage());
            sp.setDate(4, new java.sql.Date(sponsoring.getDate().getTime()));
            sp.setString(5, sponsoring.getContrat().toString());
            sp.setString(6, sponsoring.getType().toString());
            sp.setInt(7, sponsoring.getId());
            sp.executeUpdate();
            return sponsoring; // Return the modified sponsoring
        }
    }


    @Override
    public void supprimerSponsoring (int id) throws SQLException {
        String req = "DELETE FROM `sponsoring` WHERE `id`=? " ;
        try (PreparedStatement sp = con.prepareStatement(req)) {
            sp.setInt(1,id);
            sp.executeUpdate();
        }
    }

    @Override
    public List<Sponsoring> afficherSponsoring() throws SQLException {
        List<Sponsoring> sponsoringList = new ArrayList<>();
        String req = "SELECT * FROM `sponsoring`";
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(req)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                String image = rs.getString("image");
                Date date = rs.getDate("date");
                Sponsoring.Duration contrat = Sponsoring.Duration.valueOf(rs.getString("contrat"));
                Sponsoring.TypeSpon type = Sponsoring.TypeSpon.valueOf(rs.getString("type"));
                Sponsoring sponsoring = new Sponsoring(id, name, description, image, date, contrat, type);
                sponsoringList.add(sponsoring);
            }
        }
        return sponsoringList;
    }
    public Sponsoring getById(int id) throws SQLException {
        String req = "SELECT * FROM `sponsoring` WHERE `id`=?";
        try (PreparedStatement sp = con.prepareStatement(req)) {
            sp.setInt(1, id);
            try (ResultSet rs = sp.executeQuery()) {
                if (rs.next()) {
                    int sponsoringId = rs.getInt("id");
                    String name = rs.getString("name");
                    String description = rs.getString("description");
                    String image = rs.getString("image");
                    Date date = rs.getDate("date");
                    Sponsoring.Duration contrat = Sponsoring.Duration.valueOf(rs.getString("contrat"));
                    Sponsoring.TypeSpon type = Sponsoring.TypeSpon.valueOf(rs.getString("type"));
                    return new Sponsoring(sponsoringId, name, description, image, date, contrat, type);
                }
            }
        }
        return null;
    }
}





