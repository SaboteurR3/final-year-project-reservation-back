package ge.project.InternalModule.tours.repository;

import ge.project.InternalModule.tours.controller.dto.UsersByTourDto;
import ge.project.InternalModule.tours.repository.entity.Tour;
import ge.project.InternalModule.tours.repository.entity.TourReserveUser;
import ge.project.security.user.repository.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TourReserveUserRepository extends JpaRepository<TourReserveUser, Long> {

    Optional<TourReserveUser> findByTourAndAndReservedBy(Tour tour, User user);

    @Query("""
    SELECT
        u.id            as id,
        u.firstName     as firstName,
        u.lastName      as lastName,
        u.email         as email,
        u.mobileNumber  as mobileNumber,
        u.country       as country
    FROM TourReserveUser tru
    LEFT JOIN tru.reservedBy u
    WHERE tru.tour = :tour
    """)
    Page<UsersByTourDto> getUsersByTour(Pageable pageable, Tour tour);
}
