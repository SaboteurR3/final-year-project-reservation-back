package ge.project.ExternalModule.tours.controller;

import ge.project.InternalModule.rooms.controller.dto.IdNamePhotoDto;
import ge.project.InternalModule.tours.controller.dto.TourDetailsGetDto;
import ge.project.InternalModule.tours.service.TourService;
import ge.project.common.paginationandsort.PageAndSortCriteria;
import ge.project.common.paginationandsort.PageView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("external/tours")
@RequiredArgsConstructor
public class TourExternalController {
    private final TourService service;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public PageView<IdNamePhotoDto> getTours(
            PageAndSortCriteria pageAndSortCriteria,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) String search
    ) {
        return PageView.of(service.getTours(pageAndSortCriteria, startDate, endDate, true, search));
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('USER')")
    public TourDetailsGetDto getToursById(@PathVariable Long id) {
        return service.getToursById(id);
    }

    @PatchMapping("{id}/reserve")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reserveTour(@PathVariable Long id) {
        service.reserveTour(id);
    }
}
