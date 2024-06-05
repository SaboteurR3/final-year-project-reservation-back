package ge.project.InternalModule.hotels.service;

import ge.project.InternalModule.hotels.controller.dto.HotelCreateDto;
import ge.project.InternalModule.hotels.controller.dto.HotelImagesGetDto;
import ge.project.InternalModule.hotels.controller.dto.HotelsGetDto;
import ge.project.InternalModule.hotels.repository.HotelRepository;
import ge.project.InternalModule.hotels.repository.entity.Hotel;
import ge.project.InternalModule.hotels.repository.entity.HotelImage;
import ge.project.InternalModule.hotels.repository.entity.HotelRating;
import ge.project.InternalModule.rooms.repository.RoomRepository;
import ge.project.InternalModule.rooms.repository.entity.Room;
import ge.project.common.paginationandsort.PageAndSortCriteria;
import ge.project.common.repository.City;
import ge.project.common.repository.Rating;
import ge.project.exception.BusinessException;
import ge.project.exception.SecurityViolationException;
import ge.project.security.user.repository.entity.User;
import ge.project.security.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InternalHotelService {

    private final UserService userService;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;

    // To create realistic mappings between the countries and Georgian cities,
    // we should consider various factors such as cultural similarities, climate, tourism appeal,
    // and ease of travel.
    private static final Map<String, City> countryCityMap = new HashMap<>();

    static {
        countryCityMap.put("ALBANIA", City.Batumi);
        countryCityMap.put("ANDORRA", City.Mestia);
        countryCityMap.put("ARMENIA", City.Tbilisi);
        countryCityMap.put("AUSTRIA", City.Gudauri);
        countryCityMap.put("AZERBAIJAN", City.Tbilisi);
        countryCityMap.put("BELARUS", City.Kutaisi);
        countryCityMap.put("BELGIUM", City.Tbilisi);
        countryCityMap.put("BOSNIA_AND_HERZEGOVINA", City.Batumi);
        countryCityMap.put("BULGARIA", City.Batumi);
        countryCityMap.put("CROATIA", City.Batumi);
        countryCityMap.put("CYPRUS", City.Batumi);
        countryCityMap.put("CZECH_REPUBLIC", City.Tbilisi);
        countryCityMap.put("DENMARK", City.Tbilisi);
        countryCityMap.put("ESTONIA", City.Batumi);
        countryCityMap.put("FINLAND", City.Gudauri);
        countryCityMap.put("FRANCE", City.Tbilisi);
        countryCityMap.put("GEORGIA", City.Tbilisi);
        countryCityMap.put("GERMANY", City.Tbilisi);
        countryCityMap.put("GREECE", City.Batumi);
        countryCityMap.put("HUNGARY", City.Tbilisi);
        countryCityMap.put("ICELAND", City.Mestia);
        countryCityMap.put("IRELAND", City.Tbilisi);
        countryCityMap.put("ITALY", City.Tbilisi);
        countryCityMap.put("KAZAKHSTAN", City.Tbilisi);
        countryCityMap.put("KOSOVO", City.Batumi);
        countryCityMap.put("LATVIA", City.Tbilisi);
        countryCityMap.put("LIECHTENSTEIN", City.Mestia);
        countryCityMap.put("LITHUANIA", City.Tbilisi);
        countryCityMap.put("LUXEMBOURG", City.Tbilisi);
        countryCityMap.put("MALTA", City.Batumi);
        countryCityMap.put("MOLDOVA", City.Kutaisi);
        countryCityMap.put("MONACO", City.Batumi);
        countryCityMap.put("MONTENEGRO", City.Batumi);
        countryCityMap.put("NETHERLANDS", City.Tbilisi);
        countryCityMap.put("NORTH_MACEDONIA", City.Kutaisi);
        countryCityMap.put("NORWAY", City.Gudauri);
        countryCityMap.put("POLAND", City.Tbilisi);
        countryCityMap.put("PORTUGAL", City.Batumi);
        countryCityMap.put("ROMANIA", City.Tbilisi);
        countryCityMap.put("RUSSIA", City.Tbilisi);
        countryCityMap.put("SAN_MARINO", City.Tbilisi);
        countryCityMap.put("SERBIA", City.Tbilisi);
        countryCityMap.put("SLOVAKIA", City.Tbilisi);
        countryCityMap.put("SLOVENIA", City.Tbilisi);
        countryCityMap.put("SPAIN", City.Batumi);
        countryCityMap.put("SWEDEN", City.Tbilisi);
        countryCityMap.put("SWITZERLAND", City.Gudauri);
        countryCityMap.put("TURKEY", City.Batumi);
        countryCityMap.put("UKRAINE", City.Tbilisi);
        countryCityMap.put("UNITED_KINGDOM", City.Tbilisi);
        countryCityMap.put("VATICAN_CITY", City.Tbilisi);
    }

    public Hotel lookupHotel(Long id) {
        return hotelRepository.findById(id).orElseThrow(SecurityViolationException::new);
    }

    public Page<HotelsGetDto> getHotels(
            PageAndSortCriteria pageAndSortCriteria,
            City city,
            String search) {
        Pageable pageable = pageAndSortCriteria.build("id");
        return hotelRepository.getHotels(pageable, city, search);
    }

    public Page<HotelsGetDto> getHotels(
            PageAndSortCriteria pageAndSortCriteria,
            String search) {
        Pageable pageable = pageAndSortCriteria.build("id");
        User currentUser = userService.curentUser();
        City city = countryCityMap.getOrDefault(currentUser.getCountry().name(), City.Tbilisi);
        return hotelRepository.getHotels(pageable, city, search);
    }

    public List<Rating> getRatings() {
        return Arrays.stream(Rating.values())
                .sorted(Comparator.comparing(Rating::getValue))
                .toList();
    }

    public HotelImagesGetDto getHotelDetails(Long id) {
        Hotel hotel = lookupHotel(id);
        return HotelImagesGetDto.builder().imageUrls(hotel.getImages().stream().map(HotelImage::getImageUrl).toList()).build();
    }

    public void createHotel(HotelCreateDto dto) {
        validateEmailAndPhone(dto);
        User currentUser = userService.curentUser();
        Hotel hotel = Hotel.builder()
                .name(dto.name())
                .address(dto.address())
                .city(dto.city())
                .zipCode(dto.zipCode())
                .email(dto.email())
                .phone(dto.phone())
                .description(dto.description())
                .user(currentUser)
                .build();
        hotel = hotelRepository.saveAndFlush(hotel);

        List<HotelImage> images = mapImageUrlsToHotelImages(dto.imageUrls(), hotel);
        hotel.setImages(images);
        if (!images.isEmpty()) {
            hotel.setCoverImage(images.get(0).getImageUrl());
        }

        hotelRepository.saveAndFlush(hotel);
    }

    private List<HotelImage> mapImageUrlsToHotelImages(List<String> newImageUrls, Hotel hotel) {
        List<HotelImage> existingImages = Optional.ofNullable(hotel.getImages()).orElse(new ArrayList<>());

        Set<String> existingImageUrls = existingImages.stream()
                .map(HotelImage::getImageUrl)
                .collect(Collectors.toSet());

        List<HotelImage> newImages = newImageUrls.stream()
                .filter(imageUrl -> !existingImageUrls.contains(imageUrl))
                .map(imageUrl -> {
                    HotelImage image = new HotelImage();
                    image.setImageUrl(imageUrl);
                    image.setHotel(hotel);
                    return image;
                })
                .toList();

        existingImages.addAll(newImages);
        return existingImages;
    }

    public void updateHotel(Long id, HotelCreateDto dto) {
        Hotel hotel = lookupHotel(id);
        if (!dto.email().equals(hotel.getEmail()) && !dto.phone().equals(hotel.getPhone())) {
            validateEmailAndPhone(dto);
        }
        hotel.setName(dto.name());
        hotel.setAddress(dto.address());
        hotel.setCity(dto.city());
        hotel.setZipCode(dto.zipCode());
        hotel.setEmail(dto.email());
        hotel.setPhone(dto.phone());
        hotel.setDescription(dto.description());
        hotel.setImages(mapImageUrlsToHotelImages(dto.imageUrls(), hotel));
        hotelRepository.saveAndFlush(hotel);
    }


    private void validateEmailAndPhone(HotelCreateDto dto) {
        Optional<Hotel> byEmailOrPhone = hotelRepository.findByEmailOrPhone(dto.email(), dto.phone());
        if (byEmailOrPhone.isPresent()) {
            throw new BusinessException("email_or_phone_number_exists");
        }
    }

    public void deleteHotel(Long id) {
        Hotel hotel = lookupHotel(id);
        try {
            hotelRepository.delete(hotel);
            hotelRepository.flush();
        } catch (DataIntegrityViolationException ex) {
            throw new BusinessException("cant_delete");
        }
    }

    public void rateHotel(Long id, Integer rating) {
        Hotel hotel = lookupHotel(id);
        User currentUser = userService.curentUser();
        Optional<Room> roomByReservedBy = roomRepository.findRoomByReservedBy(currentUser);
        if (roomByReservedBy.isEmpty()) {
            throw new BusinessException("cant_rate_this_hotel");
        }
        if(rating < 1 || rating > 10) {
            throw new BusinessException("incorrect_rating");
        }

        HotelRating newRating = HotelRating.builder()
                .hotel(hotel)
                .rating(rating)
                .build();
        hotel.getRatings().add(newRating);
        hotelRepository.saveAndFlush(hotel);
    }

    public void deleteHotelImage(Long hotelId, String image) {
        Hotel hotel = lookupHotel(hotelId);
        if(hotel.getCoverImage().equals(image)) {
            hotel.setCoverImage(null);
        }
        HotelImage imageToRemove = hotel.getImages().stream()
                .filter(currentImage -> currentImage.getImageUrl().equals(image))
                .findFirst()
                .orElseThrow(SecurityViolationException::new);
        hotel.getImages().remove(imageToRemove);
        hotelRepository.saveAndFlush(hotel);
    }

    public void changeCoverImage(Long hotelId, String image) {
        Hotel hotel = lookupHotel(hotelId);
        hotel.getImages().stream()
                .filter(currentImage -> currentImage.getImageUrl().equals(image))
                .findFirst()
                .orElseThrow(SecurityViolationException::new);
        hotel.setCoverImage(image);
        hotelRepository.saveAndFlush(hotel);
    }
}
