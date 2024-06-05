package ge.project.InternalModule.tours.repository.entity;

import jakarta.persistence.Entity;
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
@Table(name = "tour_image")
public class TourImage {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tour_image")
    @SequenceGenerator(name = "seq_tour_image", sequenceName = "seq_tour_image", allocationSize = 1, initialValue = 1000)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour;

    private String imageUrl;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TourImage tourImage)) return false;

        return this.getId() != null && this.getId().equals(tourImage.getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
