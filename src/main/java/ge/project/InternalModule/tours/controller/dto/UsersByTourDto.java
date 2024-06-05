package ge.project.InternalModule.tours.controller.dto;

import ge.project.common.repository.Country;

public interface UsersByTourDto {
    Long getId();
    String getFirstName();
    String getLastName();
    String getEmail();
    String getMobileNumber();
    Country getCountry();
}