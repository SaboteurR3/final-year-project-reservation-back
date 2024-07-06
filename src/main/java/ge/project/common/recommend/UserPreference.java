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

    public static double normalize(double value, double multiplicator, double min, double max) {
        if (min == max) {
            throw new IllegalArgumentException("min and max cannot be the same value");
        }
        double normalized = ((value - min) / (max - min)) * 2 - 1;
        return (max - min)*normalized*multiplicator + min;
    }
}
