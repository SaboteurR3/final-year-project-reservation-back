package ge.project.InternalModule.tours.controller;

import ge.project.InternalModule.rooms.controller.dto.IdNamePhotoDto;
import ge.project.InternalModule.tours.controller.dto.TourCreateDto;
import ge.project.InternalModule.tours.controller.dto.UsersByTourDto;
import ge.project.InternalModule.tours.service.TourService;
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

import java.time.LocalDate;

@RestController
@RequestMapping("internal/tours")
@RequiredArgsConstructor
public class TourInternalController {

    private final TourService service;

    @GetMapping
    @PreAuthorize("hasRole('INTERNAL_USER')")
    public PageView<IdNamePhotoDto> getTours(
            PageAndSortCriteria pageAndSortCriteria,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) String search
    ) {
        return PageView.of(service.getTours(pageAndSortCriteria, startDate, endDate, isActive, search));
    }

    @GetMapping("{tour-id}/users")
    @PreAuthorize("hasRole('INTERNAL_USER')")
    public PageView<UsersByTourDto> getUsersByTour(PageAndSortCriteria pageAndSortCriteria, @PathVariable("tour-id") Long id) {
        return PageView.of(service.getUsersByTour(pageAndSortCriteria, id));
    }

    @PostMapping
    @PreAuthorize("hasRole('INTERNAL_USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public void createTour(@Valid @RequestBody TourCreateDto dto) {
        service.createTour(dto);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('INTERNAL_USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateTour(
            @PathVariable Long id,
            @Valid @RequestBody TourCreateDto dto
    ) {
        service.updateTour(id, dto);
    }

    @PatchMapping("{id}/deactivate")
    @PreAuthorize("hasRole('INTERNAL_USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivateTour(
            @PathVariable Long id
    ) {
        service.deactivateTour(id);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('INTERNAL_USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTour(@PathVariable Long id) {
        service.deleteTour(id);
    }

    @DeleteMapping("{tour-id}/remove/{user-id}")
    @PreAuthorize("hasRole('INTERNAL_USER')")
    public void deleteTourReserveUser(@PathVariable("tour-id") Long tourId,
                                      @PathVariable("user-id") Long userId) {
        service.deleteTourReserveUser(tourId, userId);
    }

    @DeleteMapping("{tour-id}/image")
    @PreAuthorize("hasRole('INTERNAL_USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRoomImage(@PathVariable("tour-id") Long tourId,
                                @RequestParam() String image) {
        service.deleteTourImage(tourId, image);
    }

    @PatchMapping("{tour-id}/image")
    @PreAuthorize("hasRole('INTERNAL_USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeCoverImage(@PathVariable("tour-id") Long tourId,
                                 @RequestParam() String image) {
        service.changeCoverImage(tourId, image);
    }
}
