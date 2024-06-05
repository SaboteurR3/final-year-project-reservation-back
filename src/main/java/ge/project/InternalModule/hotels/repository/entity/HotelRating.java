package ge.project.InternalModule.hotels.repository.entity;

import ge.project.common.repository.Rating;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "hotel_rating")
public class HotelRating {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_hotel_rating")
    @SequenceGenerator(name = "seq_hotel_rating", sequenceName = "seq_hotel_rating", allocationSize = 1, initialValue = 1000)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    @Column(name = "rating")
    private Integer rating;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HotelRating hotelRating)) return false;
        return id != null && id.equals(hotelRating.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
