package ge.project.InternalModule.tours.repository.entity;

import ge.project.InternalModule.rooms.repository.entity.RoomImage;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@Table(name = "tour")
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tour")
    @SequenceGenerator(name = "seq_tour", sequenceName = "seq_tour", allocationSize = 1, initialValue = 1000)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "total_seats")
    private Integer totalSeats;

    @Column(name = "reserved_seats")
    private Integer reservedSeats;

    @Column(name = "is_active")
    private Boolean isActive;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)
    private List<TourReserveUser> reservedUsers;

    @Column(name = "image")
    private String coverImage;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TourImage> images = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tour tour)) return false;

        return this.getId() != null && this.getId().equals(tour.getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
