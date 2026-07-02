package org.example.f1database.service;

import org.example.f1database.entity.Team;
import org.example.f1database.exception.ResourceNotFoundException;
import org.example.f1database.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {

    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public Team getTeamById(Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Team not found with id: " + id));
    }

    public Team createTeam(Team team) {
        return teamRepository.save(team);
    }

    public Team updateTeam(Long id, Team updatedTeam) {
        Team existingTeam = teamRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Team not found with id: " + id));

        existingTeam.setName(updatedTeam.getName());
        existingTeam.setCountry(updatedTeam.getCountry());

        return teamRepository.save(existingTeam);
    }

    public void deleteTeam(Long id) {
        Team existingTeam = teamRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Team not found with id: " + id));

        teamRepository.delete(existingTeam);
    }
}