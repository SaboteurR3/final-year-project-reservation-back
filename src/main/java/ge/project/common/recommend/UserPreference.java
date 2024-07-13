package ge.project.common.recommend;

import ge.project.common.recommend.climate.ClimateData;
import lombok.Getter;

@Getter
public class UserPreference {

    private ClimateData preferredClimate;

    //wishSimilar determines how similar they want a country to be to their own
    public UserPreference(ClimateData homeClimate, double wishSimilar){
        double preferredTemperature = normalize(homeClimate.getAvgTemperature(), wishSimilar,
                RecommenderGlobals.MIN_TEMPERATURE, RecommenderGlobals.MAX_TEMPERATURE);
        double preferredHumidity = normalize(homeClimate.getAvgHumidity(), wishSimilar,
                RecommenderGlobals.MIN_HUMIDITY, RecommenderGlobals.MAX_HUMIDITY);
        double preferredRainfall = normalize(homeClimate.getAvgRainfall(), wishSimilar,
                RecommenderGlobals.MIN_RAINFALL, RecommenderGlobals.MAX_RAINFALL);

        preferredClimate = new ClimateData(preferredTemperature, preferredHumidity, preferredRainfall);
    }

    public void tiltPreference(ClimateData newClimate, double rating){
        double r = normalize(rating, 1, 0, 5, 0, 1);

        double preferredTemperature = normalize(preferredClimate.getAvgTemperature() + newClimate.getAvgTemperature(), r,
                RecommenderGlobals.MIN_TEMPERATURE, RecommenderGlobals.MAX_TEMPERATURE);
        double preferredHumidity = normalize(preferredClimate.getAvgHumidity() + newClimate.getAvgHumidity(), r,
                RecommenderGlobals.MIN_HUMIDITY, RecommenderGlobals.MAX_HUMIDITY);
        double preferredRainfall = normalize(preferredClimate.getAvgRainfall() + newClimate.getAvgRainfall(), r,
                RecommenderGlobals.MIN_RAINFALL, RecommenderGlobals.MAX_RAINFALL);

        preferredClimate = new ClimateData(preferredTemperature, preferredHumidity, preferredRainfall);
    }

    //Multiplier is [0;1]
    public static double normalize(double value, double multiplier, double min, double max, double base1, double base2) {
        if (min == max) {
            throw new IllegalArgumentException("min and max cannot be the same value");
        }
        double normalized = ((value - min) / (max - min)) * (base2 - base1) - base1;
        return (max - min)*normalized*multiplier + min;
    }

    public static double normalize(double value, double multiplier, double min, double max) {
        return UserPreference.normalize(value, multiplier, min, max, -1, 1);
    }


}
