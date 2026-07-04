package org.example.f1database.controller;

import jakarta.validation.Valid;
import org.example.f1database.dto.RaceRequestDto;
import org.example.f1database.dto.RaceResponseDto;
import org.example.f1database.service.RaceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/races")
public class RaceController {

    private final RaceService raceService;

    public RaceController(RaceService raceService) {
        this.raceService = raceService;
    }

    @GetMapping
    public List<RaceResponseDto> getAllRaces() {
        return raceService.getAllRaces();
    }

    @GetMapping("/year/{year}")
    public List<RaceResponseDto> getRacesByYear(@PathVariable int year) {
        return raceService.getRacesByYear(year);
    }

    @GetMapping("/{id}")
    public RaceResponseDto getRaceById(@PathVariable Long id) {
        return raceService.getRaceById(id);
    }

    @PostMapping
    public RaceResponseDto createRace(@Valid @RequestBody RaceRequestDto dto) {
        return raceService.createRace(dto);
    }

    @PutMapping("/{id}")
    public RaceResponseDto updateRace(@PathVariable Long id,
                                      @Valid @RequestBody RaceRequestDto dto) {
        return raceService.updateRace(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteRace(@PathVariable Long id) {
        raceService.deleteRace(id);
    }
}