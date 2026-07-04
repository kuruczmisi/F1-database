package org.example.f1database.service;

import org.example.f1database.dto.TeamRequestDto;
import org.example.f1database.dto.TeamResponseDto;
import org.example.f1database.entity.Team;
import org.example.f1database.exception.ResourceNotFoundException;
import org.example.f1database.mapper.TeamMapper;
import org.example.f1database.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;

    public TeamService(TeamRepository teamRepository, TeamMapper teamMapper) {
        this.teamRepository = teamRepository;
        this.teamMapper = teamMapper;
    }

    public List<TeamResponseDto> getAllTeams() {
        return teamRepository.findAll()
                .stream()
                .map(teamMapper::toDto)
                .toList();
    }

    public TeamResponseDto getTeamById(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with id: " + id));

        return teamMapper.toDto(team);
    }

    public List<TeamResponseDto> getTeamsByCountry(String country) {
        return teamRepository.findByCountry(country)
                .stream()
                .map(teamMapper::toDto)
                .toList();
    }

    public TeamResponseDto createTeam(TeamRequestDto dto) {
        Team team = teamMapper.toEntity(dto);
        Team savedTeam = teamRepository.save(team);

        return teamMapper.toDto(savedTeam);
    }

    public TeamResponseDto updateTeam(Long id, TeamRequestDto dto) {
        Team existingTeam = teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with id: " + id));

        existingTeam.setName(dto.getName());
        existingTeam.setCountry(dto.getCountry());

        Team updatedTeam = teamRepository.save(existingTeam);

        return teamMapper.toDto(updatedTeam);
    }

    public void deleteTeam(Long id) {
        Team existingTeam = teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with id: " + id));

        teamRepository.delete(existingTeam);
    }
}