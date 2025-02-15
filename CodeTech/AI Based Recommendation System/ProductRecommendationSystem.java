import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.*;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.slopeone.SlopeOneRecommender;
import org.apache.mahout.cf.taste.model.*;
import org.apache.mahout.cf.taste.recommender.*;
import org.apache.mahout.cf.taste.neighborhood.*;
import org.apache.mahout.cf.taste.similarity.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ProductRecommendationSystem {

    public static void main(String[] args) {
        try {
            // Load the user-item rating data
            File dataFile = new File("user_ratings.csv"); // Path to your CSV file
            DataModel model = new FileDataModel(dataFile);

            // Create a similarity measure (for example, UserSimilarity based on Pearson correlation)
            UserSimilarity similarity = new PearsonCorrelationSimilarity(model);

            // Create a neighborhood (users with similar preferences)
            UserNeighborhood neighborhood = new NearestNUserNeighborhood(3, similarity, model);

            // Create a recommender using the model, similarity, and neighborhood
            Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);

            // Example: Get recommendations for User 1 (UserID 1)
            List<RecommendedItem> recommendations = recommender.recommend(1, 3); // Top 3 recommendations

            // Print out the recommended items
            System.out.println("Recommendations for User 1:");
            for (RecommendedItem recommendation : recommendations) {
                System.out.println("ItemID: " + recommendation.getItemID() + ", Estimated Preference: " + recommendation.getValue());
            }

        } catch (IOException | TasteException e) {
            e.printStackTrace();
        }
    }
}
