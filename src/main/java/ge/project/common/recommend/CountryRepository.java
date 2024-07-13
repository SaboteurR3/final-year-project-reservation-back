package ge.project.common.recommend;

import ge.project.common.repository.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CountryRepository extends JpaRepository<CountryParam, Long> {
    CountryParam findByCountryId(Long userId);
}
