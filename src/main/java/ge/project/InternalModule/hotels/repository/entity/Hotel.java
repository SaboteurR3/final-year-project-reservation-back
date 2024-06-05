package ge.project.InternalModule.hotels.repository.entity;

import ge.project.InternalModule.rooms.repository.entity.Room;
import ge.project.common.repository.City;
import ge.project.common.repository.Rating;
import ge.project.security.user.repository.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "hotel")
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_hotel")
    @SequenceGenerator(name = "seq_hotel", sequenceName = "seq_hotel", allocationSize = 1, initialValue = 1000)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "city")
    private City city;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
    private List<Room> rooms;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HotelRating> ratings = new ArrayList<>();

    @Column(name = "price_range_from")
    private BigDecimal priceRangeFrom;

    @Column(name = "price_range_to")
    private BigDecimal priceRangeTo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sec_user_id")
    private User user;

    @Column(name = "image")
    private String coverImage;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HotelImage> images = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hotel hotel)) return false;

        return this.getId() != null && this.getId().equals(hotel.getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }
}