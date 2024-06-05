package ge.project.InternalModule.rooms.service;

import ge.project.ExternalModule.rooms.controller.dto.ReservationDto;
import ge.project.InternalModule.hotels.controller.dto.HotelImagesGetDto;
import ge.project.InternalModule.hotels.repository.HotelRepository;
import ge.project.InternalModule.hotels.repository.entity.Hotel;
import ge.project.InternalModule.hotels.repository.entity.HotelImage;
import ge.project.InternalModule.hotels.service.InternalHotelService;
import ge.project.InternalModule.rooms.controller.dto.IdNamePhotoDto;
import ge.project.InternalModule.rooms.controller.dto.RoomCreateDto;
import ge.project.InternalModule.rooms.controller.dto.RoomImagesGetDto;
import ge.project.InternalModule.rooms.controller.dto.RoomsGetDto;
import ge.project.InternalModule.rooms.repository.RoomRepository;
import ge.project.InternalModule.rooms.repository.entity.Room;
import ge.project.InternalModule.rooms.repository.entity.RoomImage;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final InternalHotelService internalHotelService;
    private final UserService userService;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;

    public Room lookupRoom(Long id) {
        return roomRepository.findById(id).orElseThrow(SecurityViolationException::new);
    }

    public Page<RoomsGetDto> getRooms(
            PageAndSortCriteria pageAndSortCriteria,
            Long hotelId,
            Integer bed,
            Integer floor,
            BigDecimal priceFrom,
            BigDecimal priceTo,
            Boolean isReserved,
            String search) {
        Pageable pageable = pageAndSortCriteria.build("id");
        return roomRepository.getRoomsByHotel(
                pageable,
                hotelId,
                bed,
                floor,
                priceFrom,
                priceTo,
                isReserved,
                search);
    }

    public Page<RoomsGetDto> getRoomsExternal(PageAndSortCriteria pageAndSortCriteria,
                                              Long hotelId,
                                              Integer bed,
                                              Integer floor,
                                              BigDecimal priceFrom,
                                              BigDecimal priceTo,
                                              String search) {
        Pageable pageable = pageAndSortCriteria.build("id");
        return roomRepository.getActiveRoomsByHotelExternal(
                pageable,
                hotelId,
                bed,
                floor,
                priceFrom,
                priceTo,
                search);
    }

    public RoomImagesGetDto getRoomDetails(Long id) {
        Room room = lookupRoom(id);
        return RoomImagesGetDto.builder().imageUrls(room.getImages().stream().map(RoomImage::getImageUrl).toList()).build();
    }

    public void createRoom(RoomCreateDto dto) {
        Hotel hotel = internalHotelService.lookupHotel(dto.hotelId());
        validateRoom(dto.roomNumber(), hotel);
        Room room = Room.builder()
                .hotel(hotel)
                .roomNumber(dto.roomNumber())
                .floor(dto.floor())
                .bed(dto.bed())
                .pricePerNight(dto.pricePerNight())
                .isReserved(false)
                .build();
        room = roomRepository.saveAndFlush(room);

        List<RoomImage> images = mapImageUrlsToRoomImages(dto.imageUrls(), room);
        room.setImages(images);
        if (!images.isEmpty()) {
            room.setCoverImage(images.get(0).getImageUrl());
        }
        roomRepository.saveAndFlush(room);
    }

    private List<RoomImage> mapImageUrlsToRoomImages(List<String> newImageUrls, Room room) {
        List<RoomImage> existingImages = Optional.ofNullable(room.getImages()).orElse(new ArrayList<>());

        Set<String> existingImageUrls = existingImages.stream()
                .map(RoomImage::getImageUrl)
                .collect(Collectors.toSet());

        List<RoomImage> newImages = newImageUrls.stream()
                .filter(imageUrl -> !existingImageUrls.contains(imageUrl))
                .map(imageUrl -> {
                    RoomImage image = new RoomImage();
                    image.setImageUrl(imageUrl);
                    image.setRoom(room);
                    return image;
                })
                .toList();

        existingImages.addAll(newImages);
        return existingImages;
    }

    public void updateRoom(Long id, RoomCreateDto dto) {
        Room room = lookupRoom(id);
        Hotel hotel = internalHotelService.lookupHotel(dto.hotelId());
        if (!room.getRoomNumber().equals(dto.roomNumber())) {
            validateRoom(dto.roomNumber(), hotel);
        }

        room.setRoomNumber(dto.roomNumber());
        room.setBed(dto.bed());
        room.setFloor(dto.floor());
        room.setPricePerNight(dto.pricePerNight());
        room.setImages(mapImageUrlsToRoomImages(dto.imageUrls(), room));
        roomRepository.saveAndFlush(room);
    }


    public void removeReservation(Long id) {
        Room room = lookupRoom(id);
        room.setReservedFrom(null);
        room.setReservedTo(null);
        room.setReservedBy(null);
        room.setReserved(false);
        roomRepository.saveAndFlush(room);
    }

    public void changeReservationStatusExternal(Long id, ReservationDto dto) {
        Room room = lookupRoom(id);
        User currentUser = userService.curentUser();
        room.setReservedFrom(dto.reservedFrom());
        room.setReservedTo(dto.reservedTo());
        room.setReservedBy(currentUser);
        room.setReserved(true);
        roomRepository.saveAndFlush(room);
    }

    public void deleteRoom(Long id) {
        Room room = lookupRoom(id);
        try {
            roomRepository.delete(room);
            roomRepository.flush();
        } catch (DataIntegrityViolationException ex) {
            throw new BusinessException("cant_delete");
        }
    }

    private void validateRoom(String roomNumber, Hotel hotel) {
        Optional<Room> roomByRoomNumberAndAndHotel = roomRepository.findRoomByRoomNumberAndAndHotel(roomNumber, hotel);
        if (roomByRoomNumberAndAndHotel.isPresent()) {
            throw new BusinessException("hotel_room_exists");
        }
    }

    public List<IdNamePhotoDto> getHotels() {
        return hotelRepository.getHotelsForRoom();
    }

    public void deleteRoomImage(Long roomId, String image) {
        Room room = lookupRoom(roomId);
        if(room.getCoverImage().equals(image)) {
            room.setCoverImage(null);
        }
        RoomImage imageToRemove = room.getImages().stream()
                .filter(currentImage -> currentImage.getImageUrl().equals(image))
                .findFirst()
                .orElseThrow(SecurityViolationException::new);
        room.getImages().remove(imageToRemove);
        roomRepository.saveAndFlush(room);
    }

    public void changeCoverImage(Long roomId, String image) {
        Room room = lookupRoom(roomId);
        room.getImages().stream()
                .filter(currentImage -> currentImage.getImageUrl().equals(image))
                .findFirst()
                .orElseThrow(SecurityViolationException::new);
        room.setCoverImage(image);
        roomRepository.saveAndFlush(room);
    }
}
