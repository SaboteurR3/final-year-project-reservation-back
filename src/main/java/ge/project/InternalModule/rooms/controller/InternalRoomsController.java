package ge.project.InternalModule.rooms.controller;

import ge.project.InternalModule.rooms.controller.dto.IdNamePhotoDto;
import ge.project.InternalModule.rooms.controller.dto.RoomCreateDto;
import ge.project.InternalModule.rooms.controller.dto.RoomImagesGetDto;
import ge.project.InternalModule.rooms.controller.dto.RoomsGetDto;
import ge.project.InternalModule.rooms.repository.entity.Room;
import ge.project.InternalModule.rooms.service.RoomService;
import ge.project.common.paginationandsort.PageAndSortCriteria;
import ge.project.common.paginationandsort.PageView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/internal/rooms")
@RequiredArgsConstructor
public class InternalRoomsController {
    private final RoomService service;

    @GetMapping("hotels")
    @PreAuthorize("hasRole('INTERNAL_USER')")
    public List<IdNamePhotoDto> getHotels() {
        return service.getHotels();
    }

    @GetMapping
    @PreAuthorize("hasRole('INTERNAL_USER')")
    public PageView<RoomsGetDto> getRooms(PageAndSortCriteria pageAndSortCriteria,
                                           @RequestParam() Long hotelId,
                                           @RequestParam(required = false) Integer bed,
                                           @RequestParam(required = false) Integer floor,
                                           @RequestParam(required = false) BigDecimal priceFrom,
                                           @RequestParam(required = false) BigDecimal priceTo,
                                           @RequestParam(required = false) Boolean isReserved,
                                           @RequestParam(required = false) String search
    ) {
        return PageView.of(service.getRooms(
                pageAndSortCriteria,
                hotelId,
                bed,
                floor,
                priceFrom,
                priceTo,
                isReserved,
                search));
    }

    @GetMapping("{id}/images")
    @PreAuthorize("hasRole('INTERNAL_USER')")
    public RoomImagesGetDto getRoomDetails(
            @PathVariable Long id
    ) {
        return service.getRoomDetails(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('INTERNAL_USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public void createRoom(@Valid @RequestBody RoomCreateDto roomCreateDto) {
        service.createRoom(roomCreateDto);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('INTERNAL_USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateRoom(
            @PathVariable Long id,
            @Valid @RequestBody RoomCreateDto roomCreateDto
    ) {
        service.updateRoom(id, roomCreateDto);
    }

    @PatchMapping("{id}/remove-reservation")
    @PreAuthorize("hasRole('INTERNAL_USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void roomReservation(@PathVariable Long id) {
        service.removeReservation(id);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('INTERNAL_USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRoom(@PathVariable Long id) {
        service.deleteRoom(id);
    }

    @DeleteMapping("{room-id}/image")
    @PreAuthorize("hasRole('INTERNAL_USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRoomImage(@PathVariable("room-id") Long roomId,
                                 @RequestParam() String image) {
        service.deleteRoomImage(roomId, image);
    }

    @PatchMapping("{room-id}/image")
    @PreAuthorize("hasRole('INTERNAL_USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeCoverImage(@PathVariable("room-id") Long roomId,
                                 @RequestParam() String image) {
        service.changeCoverImage(roomId, image);
    }
}