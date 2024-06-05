package ge.project.InternalModule.rooms.controller.dto;

import ge.project.InternalModule.rooms.repository.entity.Room;
import lombok.Builder;

import java.util.List;

@Builder
public record RoomImagesGetDto(

        List<String> imageUrls
) {
}
