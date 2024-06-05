package ge.project.InternalModule.rooms.repository.entity;

import ge.project.InternalModule.hotels.repository.entity.HotelImage;
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
@Table(name = "room_image")
public class RoomImage {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_room_image")
    @SequenceGenerator(name = "seq_room_image", sequenceName = "seq_room_image", allocationSize = 1, initialValue = 1000)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    private String imageUrl;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HotelImage hotelImage)) return false;

        return this.getId() != null && this.getId().equals(hotelImage.getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
