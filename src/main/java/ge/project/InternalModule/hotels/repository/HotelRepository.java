package ge.project.InternalModule.hotels.repository;

import ge.project.InternalModule.hotels.controller.dto.HotelsGetDto;
import ge.project.InternalModule.hotels.repository.entity.Hotel;
import ge.project.InternalModule.rooms.controller.dto.IdNamePhotoDto;
import ge.project.common.repository.City;
import ge.project.common.repository.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface HotelRepository extends JpaRepository<Hotel, Long> {


    @Query("""
            SELECT
                h.id                            AS id,
                h.name                          AS name,
                h.address                       AS address,
                h.city                          AS city,
                h.zipCode                       AS zipCode,
                h.phone                         AS phone,
                h.email                         AS email,
                (SELECT COALESCE(CAST(AVG(CAST(hr.rating AS float)) AS BigDecimal), 0)
                                 FROM HotelRating hr
                                 WHERE hr.hotel.id = h.id)  AS rating,
                h.coverImage                    AS coverImage,
                h.priceRangeFrom                AS priceRangeFrom,
                h.priceRangeTo                  AS priceRangeTo
            FROM Hotel h
            WHERE (:city is null or h.city = :city)
                AND (:search is null
                    or h.name ilike %:search%
                    or h.address ilike %:search%)
            """)
    Page<HotelsGetDto> getHotels(Pageable pageable,
                                 City city,
                                 String search);

    Optional<Hotel> findByEmailOrPhone(String email, String phone);

    @Query("""
    SELECT
        h.id    as id,
        h.name  as name
    FROM Hotel h
    """)
    List<IdNamePhotoDto> getHotelsForRoom();
}
