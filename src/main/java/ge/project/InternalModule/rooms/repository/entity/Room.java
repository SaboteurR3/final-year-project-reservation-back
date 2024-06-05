package ge.project.InternalModule.rooms.repository.entity;

import ge.project.InternalModule.hotels.repository.entity.Hotel;
import ge.project.InternalModule.hotels.repository.entity.HotelImage;
import ge.project.security.user.repository.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_room")
    @SequenceGenerator(name = "seq_room", sequenceName = "seq_room", allocationSize = 1, initialValue = 1000)
    private Long id;

    @Column(name = "number")
    private String roomNumber;

    @Column(name = "floor")
    private Integer floor;

    @Column(name = "bed")
    private Integer bed;

    @Column(name = "price_per_night")
    private BigDecimal pricePerNight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @Setter
    @Column(name = "is_reserved")
    private boolean isReserved;

    @Setter
    @Column(name = "reserved_from")
    private LocalDate reservedFrom;

    @Setter
    @Column(name = "reserved_to")
    private LocalDate reservedTo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reserved_by")
    private User reservedBy;

    @Column(name = "image")
    private String coverImage;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomImage> images = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Room room)) return false;

        return this.getId() != null && this.getId().equals(room.getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }
}