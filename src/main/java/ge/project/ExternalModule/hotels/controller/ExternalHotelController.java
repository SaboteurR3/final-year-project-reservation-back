package ge.project.ExternalModule.hotels.controller;

import ge.project.InternalModule.hotels.controller.dto.HotelImagesGetDto;
import ge.project.InternalModule.hotels.controller.dto.HotelsGetDto;
import ge.project.InternalModule.hotels.service.InternalHotelService;
import ge.project.common.paginationandsort.PageAndSortCriteria;
import ge.project.common.paginationandsort.PageView;
import ge.project.common.repository.City;
import ge.project.common.repository.Rating;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("external/hotels")
@RequiredArgsConstructor
public class ExternalHotelController {

    private final InternalHotelService service;

    @GetMapping("cities")
    @PreAuthorize("hasRole('USER')")
    public City[] getCities() {
        return City.values();
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public PageView<HotelsGetDto> getHotels(
            PageAndSortCriteria pageAndSortCriteria,
            @RequestParam(required = false) City city,
            @RequestParam(required = false) String search
    ) {
        return PageView.of(service.getHotels(pageAndSortCriteria, city, search));
    }

    @GetMapping("{id}/images")
    @PreAuthorize("hasRole('USER')")
    public HotelImagesGetDto getHotelDetails(
            @PathVariable Long id
    ) {
        return service.getHotelDetails(id);
    }

    @PostMapping("{hotel-id}/rate")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public void rateHotel(@PathVariable("hotel-id") Long hotelId,
                          @RequestParam() Integer rating) {
        service.rateHotel(hotelId, rating);
    }
}
