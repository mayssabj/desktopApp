package edu.esprit.services;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.esprit.entities.Post;
import edu.esprit.entities.Sponsoring;
import edu.esprit.services.PostCRUD;
public class StatisticsService {

    public Map<String, Integer> calculateActiveDesactiveStatistics() {
        Map<String, Integer> statistics = new HashMap<>();
        SponsoringService sponsoringService = new SponsoringService();

        try {
            List<Sponsoring> sponsors = sponsoringService.afficherSponsoring(); // Utilisation de l'instance sponsoringService
            int activeCount = 0;
            int desactiveCount = 0;

            for (Sponsoring sponsor : sponsors) {
                if (sponsor.getType() == Sponsoring.TypeSpon.ACTIVE) {
                    activeCount++;
                } else if (sponsor.getType() == Sponsoring.TypeSpon.DESACTIVE) { // Correction de la condition
                    desactiveCount++;
                }
            }

            statistics.put("ACTIVE", activeCount);
            statistics.put("DESACTIVE", desactiveCount);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return statistics;
    }


     public Map<String, Integer> calculateLostAndFoundStatistics() {
        Map<String, Integer> statistics = new HashMap<>();
        PostCRUD postCRUD = new PostCRUD();

        try {
            List<Post> posts = postCRUD.afficher();
            int lostCount = 0;
            int foundCount = 0;

            for (Post post : posts) {
                if (post.getType() == Post.Type.Lost) {
                    lostCount++;
                } else if (post.getType() == Post.Type.Found) {
                    foundCount++;
                }
            }

            statistics.put("Lost", lostCount);
            statistics.put("Found", foundCount);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return statistics;
    }
}