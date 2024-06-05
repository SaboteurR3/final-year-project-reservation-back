package ge.project.ExternalModule.recomended_hotels.controller;

import ge.project.InternalModule.hotels.controller.dto.HotelsGetDto;
import ge.project.InternalModule.hotels.service.InternalHotelService;
import ge.project.common.paginationandsort.PageAndSortCriteria;
import ge.project.common.paginationandsort.PageView;
import ge.project.common.repository.Rating;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("recommended-hotels")
@RequiredArgsConstructor
public class RecommendedHotelController {

    private final InternalHotelService service;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public PageView<HotelsGetDto> getHotels(
            PageAndSortCriteria pageAndSortCriteria,
            @RequestParam(required = false) String search
    ) {
        return PageView.of(service.getHotels(pageAndSortCriteria, search));
    }
}
