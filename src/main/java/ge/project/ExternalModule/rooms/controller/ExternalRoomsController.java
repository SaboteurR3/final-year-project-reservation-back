package ge.project.ExternalModule.rooms.controller;

import ge.project.ExternalModule.rooms.controller.dto.ReservationDto;
import ge.project.InternalModule.rooms.controller.dto.IdNamePhotoDto;
import ge.project.InternalModule.rooms.controller.dto.RoomImagesGetDto;
import ge.project.InternalModule.rooms.controller.dto.RoomsGetDto;
import ge.project.InternalModule.rooms.repository.entity.Room;
import ge.project.InternalModule.rooms.service.RoomService;
import ge.project.common.paginationandsort.PageAndSortCriteria;
import ge.project.common.paginationandsort.PageView;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/external/rooms")
@RequiredArgsConstructor
public class ExternalRoomsController {

    private final RoomService service;

    @GetMapping("hotels")
    @PreAuthorize("hasRole('USER')")
    public List<IdNamePhotoDto> getHotels() {
        return service.getHotels();
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public PageView<RoomsGetDto> getRooms(PageAndSortCriteria pageAndSortCriteria,
                                          @RequestParam() Long hotelId,
                                          @RequestParam(required = false) Integer bed,
                                          @RequestParam(required = false) Integer floor,
                                          @RequestParam(required = false) BigDecimal priceFrom,
                                          @RequestParam(required = false) BigDecimal priceTo,
                                          @RequestParam(required = false) String search
    ) {
        return PageView.of(service.getRoomsExternal(
                pageAndSortCriteria,
                hotelId,
                bed,
                floor,
                priceFrom,
                priceTo,
                search));
    }

    @GetMapping("{id}/images")
    @PreAuthorize("hasRole('USER')")
    public RoomImagesGetDto getRoomDetails(
            @PathVariable Long id
    ) {
        return service.getRoomDetails(id);
    }

    @PatchMapping("{id}/reservation")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void roomReservation(@PathVariable Long id, @Valid @NotNull @RequestBody ReservationDto dto) {
        service.changeReservationStatusExternal(id, dto);
    }
}
