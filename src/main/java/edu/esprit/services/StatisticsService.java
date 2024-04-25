package edu.esprit.services;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.esprit.entities.Post;
import edu.esprit.services.PostCRUD;

public class StatisticsService {

    // Method to calculate lost and found statistics
    public Map<String, Integer> calculateLostAndFoundStatistics() {
        Map<String, Integer> statistics = new HashMap<>();
        PostCRUD postCRUD = new PostCRUD();

        try {
            List<Post> posts = postCRUD.afficher();
            int lostCount = 0;
            int foundCount = 0;

            for (Post post : posts) {
                if (post.getType() == Post.Type.LOST) {
                    lostCount++;
                } else if (post.getType() == Post.Type.FOUND) {
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
