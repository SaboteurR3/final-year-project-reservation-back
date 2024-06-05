package ge.project.InternalModule.hotels.controller.dto;

import ge.project.common.repository.City;
import ge.project.common.repository.Rating;

import java.math.BigDecimal;

public interface HotelsGetDto {
    Long getId();

    String getName();

    String getAddress();

    City getCity();

    String getZipCode();

    String getPhone();

    String getEmail();

    BigDecimal getRating();

    String getPriceRangeFrom();

    String getPriceRangeTo();
    String getCoverImage();
}
