package ge.project.common.recommend;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Entity
public class CountryRating {
    @Id
    private long countryId;
    private int rating;
    private long userId;

    public CountryRating() {

    }
}
