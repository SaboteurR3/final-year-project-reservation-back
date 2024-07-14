package ge.project.common.recommend;

import ge.project.common.recommend.climate.ClimateData;
import ge.project.common.repository.Country;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Entity
public class CountryParam {
    @Id
    private long countryId;
    private Country countryEnum;
    private ClimateData climateData;

    public CountryParam() {

    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public Long getCountryId() {
        return countryId;
    }
}
