package ge.project.InternalModule.rooms.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface RoomsGetDto {
    Long getId();

    String getRoomNumber();

    Integer getFloor();

    Integer getBed();

    BigDecimal getPricePerNight();

    LocalDate getReservedFrom();

    LocalDate getReservedTo();

    String getEmail();
    String getCoverImage();
}
