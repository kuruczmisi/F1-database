package org.example.f1database.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RaceRequestDto {

    @NotBlank(message = "Race name is required")
    private String name;

    @NotBlank(message = "Location is required")
    private String location;

    @Min(value = 1950, message = "Year must be at least 1950")
    @Max(value = 2100, message = "Year cannot be greater than 2100")
    private int year;
}