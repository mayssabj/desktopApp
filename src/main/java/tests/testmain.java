package tests;

import entite.Comment;
import services.CommentCRUD;
import services.PostCRUD;
import utils.MyDB;
import entite.Post;
import entite.Post.Type;

import java.sql.SQLException;
import java.util.Date;

public class testmain {

    public static void main(String[] args) throws SQLException {
        MyDB db = MyDB.getInstance();

        // Create a new post
        Post post1 = new Post("Exemple2", "Black leather wallet lost near the park", "wallet", new Date(), Type.LOST, "Central Park");
        System.out.println("valide");

        // Instantiate PostCRUD and add the post to the database
        PostCRUD postCRUD = new PostCRUD();
        postCRUD.ajouter(post1);
        postCRUD.afficher();

        Comment comm = new Comment("Exemple comm", post1);

        // Instantiate PostCRUD and add the post to the database
        CommentCRUD CommentCRUD = new CommentCRUD();
        CommentCRUD.ajouter(comm);
    }
}
