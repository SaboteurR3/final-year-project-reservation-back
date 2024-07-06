package ge.project.common.recommend.climate;

import ge.project.common.repository.Country;
import lombok.Getter;

import java.util.List;

@Getter
public class MapInstance {
    public static ClimateMap climateMap;

    private ClimateDataService climateDataService;

    public MapInstance() {
        climateMap = new ClimateMap();

        List<ClimateDataRow> climateDataRows = climateDataService.getAllClimateData();
        for (ClimateDataRow row : climateDataRows) {
            ClimateData climateData = new ClimateData(row.getAvgTemperature(), row.getAvgRainfall(), row.getAvgHumidity());
            Country country = Country.fromString(row.getCountry());
            climateMap.addCountry(country, climateData);
        }
    }
}
