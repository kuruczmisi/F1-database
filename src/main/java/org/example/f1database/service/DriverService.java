package org.example.f1database.service;

import org.example.f1database.dto.DriverRequestDto;
import org.example.f1database.dto.DriverResponseDto;
import org.example.f1database.entity.Driver;
import org.example.f1database.entity.Team;
import org.example.f1database.exception.ResourceNotFoundException;
import org.example.f1database.mapper.DriverMapper;
import org.example.f1database.repository.DriverRepository;
import org.example.f1database.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverService {

    private final DriverRepository driverRepository;
    private final TeamRepository teamRepository;
    private final DriverMapper driverMapper;

    public DriverService(DriverRepository driverRepository,
                         TeamRepository teamRepository,
                         DriverMapper driverMapper) {
        this.driverRepository = driverRepository;
        this.teamRepository = teamRepository;
        this.driverMapper = driverMapper;
    }

    public List<DriverResponseDto> getAllDrivers() {
        return driverRepository.findAll()
                .stream()
                .map(driverMapper::toDto)
                .toList();
    }

    public DriverResponseDto getDriverById(Long id) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with id: " + id));

        return driverMapper.toDto(driver);
    }

    public List<DriverResponseDto> getDriversByNationality(String nationality) {
        return driverRepository.findByNationality(nationality)
                .stream()
                .map(driverMapper::toDto)
                .toList();
    }

    public DriverResponseDto createDriver(DriverRequestDto dto) {
        Team team = teamRepository.findById(dto.getTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with id: " + dto.getTeamId()));

        Driver driver = driverMapper.toEntity(dto, team);
        Driver savedDriver = driverRepository.save(driver);

        return driverMapper.toDto(savedDriver);
    }

    public DriverResponseDto updateDriver(Long id, DriverRequestDto dto) {
        Driver existingDriver = driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with id: " + id));

        Team team = teamRepository.findById(dto.getTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with id: " + dto.getTeamId()));

        existingDriver.setName(dto.getName());
        existingDriver.setNationality(dto.getNationality());
        existingDriver.setNumber(dto.getNumber());
        existingDriver.setTeam(team);

        Driver updatedDriver = driverRepository.save(existingDriver);

        return driverMapper.toDto(updatedDriver);
    }

    public void deleteDriver(Long id) {
        Driver existingDriver = driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with id: " + id));

        driverRepository.delete(existingDriver);
    }
}