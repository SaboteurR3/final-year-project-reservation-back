package ge.project.InternalModule.tours.repository.entity;

import ge.project.InternalModule.hotels.repository.entity.Hotel;
import ge.project.InternalModule.rooms.repository.entity.Room;
import ge.project.security.user.repository.entity.User;
import jakarta.persistence.Entity;
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
@Table(name = "tour_reserve_user")
public class TourReserveUser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tour_reserve_user")
    @SequenceGenerator(name = "seq_tour_reserve_user", sequenceName = "seq_tour_reserve_user", allocationSize = 1, initialValue = 1000)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_id")
    private Tour tour;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sec_user_id")
    private User reservedBy;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TourReserveUser tourReserveUser)) return false;

        return this.getId() != null && this.getId().equals(tourReserveUser.getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }
}