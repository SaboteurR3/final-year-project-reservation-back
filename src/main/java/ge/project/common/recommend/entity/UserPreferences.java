package ge.project.common.recommend.entity;

import ge.project.common.repository.Country;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserPreferences {
    private double preferredTemperature;
    private double preferredRainfall;
    private double preferredHumidity;

    //wishSimilar determines how similar they want a country to be to their own
    public UserPreferences(Country homeCountry, float wishSimilar){

    }
}
