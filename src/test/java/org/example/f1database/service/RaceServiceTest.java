package org.example.f1database.service;

import org.example.f1database.dto.RaceRequestDto;
import org.example.f1database.dto.RaceResponseDto;
import org.example.f1database.entity.Driver;
import org.example.f1database.entity.Race;
import org.example.f1database.exception.ResourceNotFoundException;
import org.example.f1database.mapper.RaceMapper;
import org.example.f1database.repository.DriverRepository;
import org.example.f1database.repository.RaceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RaceServiceTest {

    @Mock
    private RaceRepository raceRepository;

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private RaceMapper raceMapper;

    @InjectMocks
    private RaceService raceService;

    @Test
    void createRace_shouldReturnSavedRace() {
        Driver driver = new Driver();
        driver.setId(1L);
        driver.setName("Lewis Hamilton");

        RaceRequestDto requestDto = new RaceRequestDto();
        requestDto.setName("Hungarian Grand Prix");
        requestDto.setLocation("Hungary");
        requestDto.setYear(2025);
        requestDto.setDriverIds(List.of(1L));

        Race race = new Race();
        race.setName("Hungarian Grand Prix");
        race.setLocation("Hungary");
        race.setYear(2025);
        race.setDrivers(List.of(driver));

        Race savedRace = new Race();
        savedRace.setId(1L);
        savedRace.setName("Hungarian Grand Prix");
        savedRace.setLocation("Hungary");
        savedRace.setYear(2025);
        savedRace.setDrivers(List.of(driver));

        RaceResponseDto responseDto = new RaceResponseDto(
                1L,
                "Hungarian Grand Prix",
                "Hungary",
                2025,
                List.of(1L),
                List.of("Lewis Hamilton")
        );

        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));
        when(raceMapper.toEntity(requestDto, List.of(driver))).thenReturn(race);
        when(raceRepository.save(race)).thenReturn(savedRace);
        when(raceMapper.toDto(savedRace)).thenReturn(responseDto);

        RaceResponseDto result = raceService.createRace(requestDto);

        assertEquals(1L, result.getId());
        assertEquals("Hungarian Grand Prix", result.getName());
        assertEquals(2025, result.getYear());
        assertEquals(List.of(1L), result.getDriverIds());
        assertEquals(List.of("Lewis Hamilton"), result.getDriverNames());
    }

    @Test
    void getRaceById_shouldReturnRace() {
        Driver driver = new Driver();
        driver.setId(1L);
        driver.setName("Lewis Hamilton");

        Race race = new Race();
        race.setId(1L);
        race.setName("Hungarian Grand Prix");
        race.setLocation("Hungary");
        race.setYear(2025);
        race.setDrivers(List.of(driver));

        RaceResponseDto responseDto = new RaceResponseDto(
                1L,
                "Hungarian Grand Prix",
                "Hungary",
                2025,
                List.of(1L),
                List.of("Lewis Hamilton")
        );

        when(raceRepository.findById(1L)).thenReturn(Optional.of(race));
        when(raceMapper.toDto(race)).thenReturn(responseDto);

        RaceResponseDto result = raceService.getRaceById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Hungarian Grand Prix", result.getName());
        assertEquals(List.of("Lewis Hamilton"), result.getDriverNames());
    }

    @Test
    void getRaceById_shouldThrowException() {
        when(raceRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> raceService.getRaceById(99L));
    }
}