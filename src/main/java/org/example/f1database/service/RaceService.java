package org.example.f1database.service;

import org.example.f1database.entity.Race;
import org.example.f1database.exception.ResourceNotFoundException;
import org.example.f1database.repository.RaceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RaceService {

    private final RaceRepository raceRepository;

    public RaceService(RaceRepository raceRepository) {
        this.raceRepository = raceRepository;
    }

    public List<Race> getAllRaces() {
        return raceRepository.findAll();
    }

    public Race getRaceById(Long id) {
        return raceRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Race not found with id: " + id));
    }

    public Race createRace(Race race) {
        return raceRepository.save(race);
    }

    public Race updateRace(Long id, Race updatedRace) {
        Race existingRace = raceRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Race not found with id: " + id));

        existingRace.setName(updatedRace.getName());
        existingRace.setLocation(updatedRace.getLocation());
        existingRace.setYear(updatedRace.getYear());

        return raceRepository.save(existingRace);
    }

    public void deleteRace(Long id) {
        Race existingRace = raceRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Race not found with id: " + id));

        raceRepository.delete(existingRace);
    }
}