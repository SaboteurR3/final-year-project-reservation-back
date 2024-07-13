package ge.project.common.recommend;

import ge.project.common.recommend.climate.ClimateData;
import ge.project.common.repository.Country;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CountryParam {
    private long countryId;
    private Country countryEnum;
    private ClimateData climateData;
}
