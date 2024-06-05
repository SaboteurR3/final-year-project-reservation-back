package ge.project.InternalModule.hotels.controller;

import ge.project.InternalModule.hotels.controller.dto.HotelCreateDto;
import ge.project.InternalModule.hotels.controller.dto.HotelImagesGetDto;
import ge.project.InternalModule.hotels.controller.dto.HotelsGetDto;
import ge.project.InternalModule.hotels.service.InternalHotelService;
import ge.project.common.paginationandsort.PageAndSortCriteria;
import ge.project.common.paginationandsort.PageView;
import ge.project.common.repository.City;
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

@RestController
@RequestMapping("/internal/hotels")
@RequiredArgsConstructor
public class InternalHotelController {
    private final InternalHotelService service;

    @GetMapping("cities")
    @PreAuthorize("hasRole('INTERNAL_USER')")
    public City[] getCities() {
        return City.values();
    }

    @GetMapping
    @PreAuthorize("hasRole('INTERNAL_USER')")
    public PageView<HotelsGetDto> getHotels(
            PageAndSortCriteria pageAndSortCriteria,
            @RequestParam(required = false) City city,
            @RequestParam(required = false) String search
    ) {
        return PageView.of(service.getHotels(pageAndSortCriteria, city, search));
    }

    @GetMapping("{id}/images")
    @PreAuthorize("hasRole('INTERNAL_USER')")
    public HotelImagesGetDto getHotelDetails(
            @PathVariable Long id
    ) {
        return service.getHotelDetails(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('INTERNAL_USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public void createHotel(@Valid @RequestBody HotelCreateDto hotelCreateDto) {
        service.createHotel(hotelCreateDto);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('INTERNAL_USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateHotel(
            @PathVariable Long id,
            @Valid @RequestBody HotelCreateDto hotelCreateDto
    ) {
        service.updateHotel(id, hotelCreateDto);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('INTERNAL_USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteHotel(@PathVariable Long id) {
        service.deleteHotel(id);
    }

    @PostMapping("{hotel-id}/rate")
    @PreAuthorize("hasRole('INTERNAL_USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public void rateHotel(@PathVariable("hotel-id") Long hotelId,
                          @RequestParam() Integer rating) {
        service.rateHotel(hotelId, rating);
    }

    @DeleteMapping("{hotel-id}/image")
    @PreAuthorize("hasRole('INTERNAL_USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteHotelImage(@PathVariable("hotel-id") Long hotelId,
                                 @RequestParam() String image) {
        service.deleteHotelImage(hotelId, image);
    }

    @PatchMapping("{hotel-id}/image")
    @PreAuthorize("hasRole('INTERNAL_USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeCoverImage(@PathVariable("hotel-id") Long hotelId,
                                 @RequestParam() String image) {
        service.changeCoverImage(hotelId, image);
    }
}