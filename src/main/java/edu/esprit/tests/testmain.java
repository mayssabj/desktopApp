package edu.esprit.tests;



import edu.esprit.services.PostgroupService;
import edu.esprit.services.SponsoringService;
import edu.esprit.entities.Post_group;
import edu.esprit.entities.Sponsoring;
import edu.esprit.entities.Sponsoring.TypeSpon;
import edu.esprit.entities.Sponsoring.Duration;
import edu.esprit.utils.mydb;

import java.sql.SQLException;
import java.util.Date;

public class testmain {


    public static void main(String[] args) throws SQLException {
        mydb db = mydb.getInstance();
        ;
        // Create a new sponsor
        Sponsoring sponsor1 = new Sponsoring("esprit","ecole privé","hhh",new Date(12/04/2022), Duration.TREE_YEARS, TypeSpon.ACTIVE);
        System.out.println("valide");

        // Instantiate PostCRUD and add the sponsoring to the database
        SponsoringService sponsoring = new SponsoringService();
        // sponsoring.ajouterSponsoring(sponsor1);
        //sponsoring.supprimerSponsoring(5);
        sponsoring.afficherSponsoring();



    }
}
