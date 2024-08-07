package ge.project.common.recommend.climate;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Embeddable
public class ClimateData {
    private double avgTemperature;
    private double avgRainfall;
    private double avgHumidity;

    public ClimateData() {

    }

    public double euclidianDifference(ClimateData other){
        double diffsquared = Math.pow(avgTemperature - other.avgTemperature, 2)
                            + Math.pow(avgRainfall - other.avgRainfall, 2)
                            + Math.pow(avgHumidity - other.avgHumidity, 2);
        return Math.sqrt(diffsquared);
    }
}