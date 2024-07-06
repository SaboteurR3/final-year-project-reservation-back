package ge.project.common.recommend.climate;


import ge.project.common.recommend.UserPreference;
import ge.project.common.repository.Country;

import java.util.Map;

public class Recommender {

    public Country recommendCountry(MapInstance map, UserPreference userPreference) {
        Country bestMatchCountry = null;
        double smallestDistance = Double.MAX_VALUE;

        for (Map.Entry<Country, ClimateData> entry : map.climateMap.getClimateMap().entrySet()) {
            double distance = entry.getValue().euclidianDifference(userPreference.getPreferredClimate());

            if (distance < smallestDistance) {
                smallestDistance = distance;
                bestMatchCountry = entry.getKey();
            }
        }

        return bestMatchCountry;
    }
}
