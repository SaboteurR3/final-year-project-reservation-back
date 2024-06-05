package ge.project.InternalModule.tours.repository;

import ge.project.InternalModule.rooms.controller.dto.IdNamePhotoDto;
import ge.project.InternalModule.tours.repository.entity.Tour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

public interface TourRepository extends JpaRepository<Tour, Long> {
    @Query("""
    SELECT
        t.id            AS id,
        t.name          AS name,
        t.coverImage    AS coverImage
    FROM Tour t
    WHERE (cast(:startDate as timestamp) is null or t.startDate >= :startDate)
        AND (cast(:endDate as timestamp) is null or t.endDate <= :endDate)
        AND (:isActive is null or t.isActive = :isActive)
        AND (:search is null or t.name ilike %:search%)
    """)
    Page<IdNamePhotoDto> getTours(Pageable pageable,
                                        LocalDate startDate,
                                        LocalDate endDate,
                                        Boolean isActive,
                                        String search);

    Optional<Tour> findByName(String name);
}
