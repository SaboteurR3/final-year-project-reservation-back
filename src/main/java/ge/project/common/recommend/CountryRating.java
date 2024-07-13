package ge.project.common.recommend;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CountryRating {
    private long countryId;
    private int rating;
    private long userId;
}
