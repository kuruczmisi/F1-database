package org.example.f1database.controller;

import jakarta.validation.Valid;
import org.example.f1database.dto.TeamRequestDto;
import org.example.f1database.dto.TeamResponseDto;
import org.example.f1database.service.TeamService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teams")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping
    public List<TeamResponseDto> getAllTeams() {
        return teamService.getAllTeams();
    }

    @GetMapping("/country/{country}")
    public List<TeamResponseDto> getTeamsByCountry(@PathVariable String country) {
        return teamService.getTeamsByCountry(country);
    }

    @GetMapping("/{id}")
    public TeamResponseDto getTeamById(@PathVariable Long id) {
        return teamService.getTeamById(id);
    }

    @PostMapping
    public TeamResponseDto createTeam(@Valid @RequestBody TeamRequestDto dto) {
        return teamService.createTeam(dto);
    }

    @PutMapping("/{id}")
    public TeamResponseDto updateTeam(@PathVariable Long id,
                                      @Valid @RequestBody TeamRequestDto dto) {
        return teamService.updateTeam(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteTeam(@PathVariable Long id) {
        teamService.deleteTeam(id);
    }
}