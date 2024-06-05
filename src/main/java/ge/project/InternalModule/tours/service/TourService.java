package ge.project.InternalModule.tours.service;

import ge.project.InternalModule.hotels.repository.entity.HotelImage;
import ge.project.InternalModule.rooms.controller.dto.IdNamePhotoDto;
import ge.project.InternalModule.rooms.repository.entity.Room;
import ge.project.InternalModule.rooms.repository.entity.RoomImage;
import ge.project.InternalModule.tours.controller.dto.TourCreateDto;
import ge.project.InternalModule.tours.controller.dto.TourDetailsGetDto;
import ge.project.InternalModule.tours.controller.dto.UsersByTourDto;
import ge.project.InternalModule.tours.repository.TourReserveUserRepository;
import ge.project.InternalModule.tours.repository.entity.Tour;
import ge.project.InternalModule.tours.repository.TourRepository;
import ge.project.InternalModule.tours.repository.entity.TourImage;
import ge.project.InternalModule.tours.repository.entity.TourReserveUser;
import ge.project.common.paginationandsort.PageAndSortCriteria;
import ge.project.exception.BusinessException;
import ge.project.exception.SecurityViolationException;
import ge.project.security.user.repository.entity.User;
import ge.project.security.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TourService {
    private final TourRepository repository;
    private final TourReserveUserRepository tourReserveUserRepository;
    private final UserService userService;

    public Tour lookupTour(Long id) {
        return repository.findById(id).orElseThrow(SecurityViolationException::new);
    }

    public Page<IdNamePhotoDto> getTours(
            PageAndSortCriteria pageAndSortCriteria,
            LocalDate startDate,
            LocalDate endDate,
            Boolean isActive,
            String search) {
        Pageable pageable = pageAndSortCriteria.build("id");
        return repository.getTours(
                pageable,
                startDate,
                endDate,
                isActive,
                search);
    }

    public TourDetailsGetDto getToursById(Long id) {
        Tour tour = lookupTour(id);
        return TourDetailsGetDto.builder()
                .name(tour.getName())
                .description(tour.getDescription())
                .price(tour.getPrice())
                .startDate(tour.getStartDate())
                .endDate(tour.getEndDate())
                .totalSeats(tour.getTotalSeats())
                .reservedSeats(tour.getReservedSeats())
                .imageUrls(tour.getImages().stream().map(TourImage::getImageUrl).toList())
                .build();
    }

    public void createTour(TourCreateDto dto) {
        validateAttraction(dto.name());
        Tour tour = Tour.builder()
                .name(dto.name())
                .description(dto.description())
                .price(dto.price())
                .startDate(dto.startDate())
                .endDate(dto.endDate())
                .totalSeats(dto.totalSeats())
                .isActive(true)
                .build();

        List<TourImage> images = mapImageUrlsToTourImages(dto.imageUrls(), tour);
        tour.setImages(images);
        if (!images.isEmpty()) {
            tour.setCoverImage(images.get(0).getImageUrl());
        }

        repository.saveAndFlush(tour);
    }

    private List<TourImage> mapImageUrlsToTourImages(List<String> newImageUrls, Tour tour) {
        List<TourImage> existingImages = Optional.ofNullable(tour.getImages()).orElse(new ArrayList<>());

        Set<String> existingImageUrls = existingImages.stream()
                .map(TourImage::getImageUrl)
                .collect(Collectors.toSet());

        List<TourImage> newImages = newImageUrls.stream()
                .filter(imageUrl -> !existingImageUrls.contains(imageUrl))
                .map(imageUrl -> {
                    TourImage image = new TourImage();
                    image.setImageUrl(imageUrl);
                    image.setTour(tour);
                    return image;
                })
                .toList();

        existingImages.addAll(newImages);
        return existingImages;
    }


    public void updateTour(Long id, TourCreateDto dto) {
        Tour tour = lookupTour(id);
        if (!tour.getName().equals(dto.name())) {
            validateAttraction(dto.name());
        }
        tour.setName(dto.name());
        tour.setDescription(dto.description());
        tour.setPrice(dto.price());
        tour.setStartDate(dto.startDate());
        tour.setEndDate(dto.endDate());
        tour.setTotalSeats(dto.totalSeats());
        tour.setImages(mapImageUrlsToTourImages(dto.imageUrls(), tour));
        repository.saveAndFlush(tour);
    }

    public void deleteTour(Long id) {
        Tour tour = lookupTour(id);
        try {
            repository.delete(tour);
            repository.flush();
        } catch (DataIntegrityViolationException ex) {
            throw new BusinessException("cant_delete");
        }
    }

    public void deactivateTour(Long id) {
        Tour tour = lookupTour(id);
        tour.setIsActive(false);
        repository.saveAndFlush(tour);
    }

    public void reserveTour(Long id) {
        Tour tour = lookupTour(id);
        User currentUser = userService.curentUser();
        checkReserved(tour, currentUser);
        validateSeats(tour.getReservedSeats(), tour.getTotalSeats());
        tour.setReservedSeats(tour.getReservedSeats() + 1);
        saveTourReserveUser(tour, currentUser);
        repository.saveAndFlush(tour);
    }

    private void validateSeats(Integer reservedSeats, Integer totalSeats) {
        if (Objects.equals(reservedSeats, totalSeats)) {
            throw new BusinessException("all_places_are_taken");
        }
    }

    private void checkReserved(Tour tour, User currentUser) {
        Optional<TourReserveUser> byTourAndAndReservedBy = tourReserveUserRepository.findByTourAndAndReservedBy(tour, currentUser);
        if(byTourAndAndReservedBy.isPresent()) {
            throw new BusinessException("already_reserved");
        }
    }

    private void saveTourReserveUser(Tour tour, User currentUser) {
        TourReserveUser tourReserveUser = TourReserveUser.builder()
                .tour(tour)
                .reservedBy(currentUser)
                .build();
        tourReserveUserRepository.saveAndFlush(tourReserveUser);
    }

    private void validateAttraction(String name) {
        Optional<Tour> tourByName = repository.findByName(name);
        if (tourByName.isPresent()) {
            throw new BusinessException("tour_name_exists");
        }
    }

    public Page<UsersByTourDto> getUsersByTour(PageAndSortCriteria pageAndSortCriteria, Long tourId) {
        Tour tour = lookupTour(tourId);
        Pageable pageable = pageAndSortCriteria.build("id");
        return tourReserveUserRepository.getUsersByTour(pageable, tour);
    }

    public void deleteTourReserveUser(Long tourId, Long userId) {
        Tour tour = lookupTour(tourId);
        User user = userService.lookUpUserById(userId);
        Optional<TourReserveUser> byTourAndAndReservedBy = tourReserveUserRepository.findByTourAndAndReservedBy(tour, user);
        byTourAndAndReservedBy.ifPresent(tourReserveUserRepository::delete);
    }

    public void deleteTourImage(Long roomId, String image) {
        Tour tour = lookupTour(roomId);
        if(tour.getCoverImage().equals(image)) {
            tour.setCoverImage(null);
        }
        TourImage imageToRemove = tour.getImages().stream()
                .filter(currentImage -> currentImage.getImageUrl().equals(image))
                .findFirst()
                .orElseThrow(SecurityViolationException::new);
        tour.getImages().remove(imageToRemove);
        repository.saveAndFlush(tour);
    }

    public void changeCoverImage(Long tourId, String image) {
        Tour tour = lookupTour(tourId);
        tour.getImages().stream()
                .filter(currentImage -> currentImage.getImageUrl().equals(image))
                .findFirst()
                .orElseThrow(SecurityViolationException::new);
        tour.setCoverImage(image);
        repository.saveAndFlush(tour);
    }
}
