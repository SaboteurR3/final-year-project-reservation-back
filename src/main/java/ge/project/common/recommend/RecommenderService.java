package ge.project.common.recommend;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecommenderService {
    private final RatingRepository ratingRepository;
    private final UsersRepository usersRepository;
    private final CountryRepository countryRepository;

    public RecommenderService(RatingRepository ratingRepository, UsersRepository usersRepository, CountryRepository countryRepository) {
        this.ratingRepository = ratingRepository;
        this.usersRepository = usersRepository;
        this.countryRepository = countryRepository;
    }

    public List<CountryParam> recommendCountries(Long userId) {
        List<CountryRating> userRatings = ratingRepository.findByUserId(userId);
        Map<Long, Map<Long, Integer>> allUserRatings = getAllUserRatings();

        Map<Long, Integer> currentUserRatings = allUserRatings.get(userId);
        Map<Long, Double> userSimilarities = new HashMap<>();

        for (Long otherUserId : allUserRatings.keySet()) {
            if (!otherUserId.equals(userId)) {
                double similarity = computeCosineSimilarity(currentUserRatings, allUserRatings.get(otherUserId));
                userSimilarities.put(otherUserId, similarity);
            }
        }

        List<Long> similarUsers = userSimilarities.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .limit(10)
                .toList();

        Map<Long, Double> countryRecommendations = new HashMap<>();

        for (Long similarUserId : similarUsers) {
            List<CountryRating> similarUserRatings = ratingRepository.findByUserId(similarUserId);
            for (CountryRating rating : similarUserRatings) {
                CountryParam similarUserCountry = countryRepository.findByCountryId(rating.getCountryId());
                CountryParam similarUserCountryParam = new CountryParam(
                        similarUserCountry.getCountryId(),
                        similarUserCountry.getCountryEnum(),
                        similarUserCountry.getClimateData()
                );

                double climateSimilarity = 1 / (1 + similarUserCountryParam.getClimateData().euclidianDifference(ratingRepository.findByCountryId(rating.getCountryId()).getClimateData()));

                countryRecommendations.put(
                        rating.getCountryId(),
                        countryRecommendations.getOrDefault(rating.getCountryId(), 0.0) + rating.getRating() * userSimilarities.get(similarUserId) * climateSimilarity
                );
            }
        }

        return countryRepository.findAllById(countryRecommendations.keySet()).stream()
                .sorted(Comparator.comparingDouble(c -> countryRecommendations.get(c.getCountryId())))
                .limit(5)
                .toList();
    }

    private Map<Long, Map<Long, Integer>> getAllUserRatings() {
        List<CountryRating> allRatings = ratingRepository.findAll();
        Map<Long, Map<Long, Integer>> userRatings = new HashMap<>();

        for (CountryRating rating : allRatings) {
            userRatings.computeIfAbsent(rating.getUserId(), k -> new HashMap<>()).put(rating.getCountryId(), rating.getRating());
        }

        return userRatings;
    }

    public double computeCosineSimilarity(Map<Long, Integer> user1Ratings, Map<Long, Integer> user2Ratings) {
        double dotProduct = 0.0;
        double normUser1 = 0.0;
        double normUser2 = 0.0;

        for (Long countryId : user1Ratings.keySet()) {
            int rating1 = user1Ratings.get(countryId);
            int rating2 = user2Ratings.getOrDefault(countryId, 0);
            dotProduct += rating1 * rating2;
            normUser1 += Math.pow(rating1, 2);
            normUser2 += Math.pow(rating2, 2);
        }

        if (normUser1 == 0.0 || normUser2 == 0.0) {
            return 0.0;
        }

        return dotProduct / (Math.sqrt(normUser1) * Math.sqrt(normUser2));
    }
}
