package ge.project.common.recommend;

import ge.project.common.repository.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<CountryRating, Long> {
    List<CountryRating> findByUserId(Long userId);
    CountryParam findByCountryId(Long countryId);
}