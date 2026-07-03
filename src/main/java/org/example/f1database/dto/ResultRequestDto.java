package org.example.f1database.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultRequestDto {

    @Positive(message = "Position must be positive")
    private int position;

    @Min(value = 0, message = "Points cannot be negative")
    private int points;

    @NotNull(message = "Driver id is required")
    private Long driverId;

    @NotNull(message = "Race id is required")
    private Long raceId;
}