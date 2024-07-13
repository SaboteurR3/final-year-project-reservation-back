package ge.project.common.recommend;

import ge.project.common.repository.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<CountryParam, Long> {}
