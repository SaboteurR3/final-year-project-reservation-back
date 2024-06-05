package ge.project.InternalModule.rooms.repository;

import ge.project.InternalModule.hotels.repository.entity.Hotel;
import ge.project.InternalModule.rooms.controller.dto.RoomsGetDto;
import ge.project.InternalModule.rooms.repository.entity.Room;
import ge.project.security.user.repository.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findRoomByRoomNumberAndAndHotel(String roomNumber, Hotel hotel);
    Optional<Room> findRoomByReservedBy(User user);

    @Query("""
            SELECT
                r.id                AS id,
                r.roomNumber        AS roomNumber,
                r.bed               AS bed,
                r.floor             AS floor,
                r.coverImage        AS coverImage,
                r.pricePerNight     AS pricePerNight,
                r.reservedFrom      AS reservedFrom,
                r.reservedTo        AS reservedTo,
                u.email             AS email
            FROM Hotel h
            LEFT JOIN h.rooms r
            LEFT JOIN r.reservedBy u
            WHERE (:isReserved is null or r.isReserved = :isReserved)
                AND (:hotelId is null or h.id = :hotelId)
                AND (:bed is null or r.bed = :bed)
                AND (:floor is null or r.floor = :floor)
                AND (:priceFrom is null or r.pricePerNight >= :priceFrom)
                AND (:priceTo is null or r.pricePerNight <= :priceTo)
                AND (:search is null
                    or r.roomNumber ilike %:search%)
            """)
    Page<RoomsGetDto> getRoomsByHotel(Pageable pageable,
                                            Long hotelId,
                                            Integer bed,
                                            Integer floor,
                                            BigDecimal priceFrom,
                                            BigDecimal priceTo,
                                            Boolean isReserved,
                                            String search);

    @Query("""
            SELECT
                r.id                AS id,
                r.roomNumber        AS roomNumber,
                r.bed               AS bed,
                r.floor             AS floor,
                r.pricePerNight     AS pricePerNight
            FROM Hotel h
            LEFT JOIN h.rooms r
            WHERE (r.isReserved = false)
                AND (:hotelId is null or h.id = :hotelId)
                AND (:bed is null or r.bed = :bed)
                AND (:floor is null or r.floor = :floor)
                AND (:priceFrom is null or r.pricePerNight >= :priceFrom)
                AND (:priceTo is null or r.pricePerNight <= :priceTo)
                AND (:search is null
                    or r.roomNumber ilike %:search%)
            """)
    Page<RoomsGetDto> getActiveRoomsByHotelExternal(Pageable pageable,
                                            Long hotelId,
                                            Integer bed,
                                            Integer floor,
                                            BigDecimal priceFrom,
                                            BigDecimal priceTo,
                                            String search);
}
