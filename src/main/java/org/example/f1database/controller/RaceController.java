package org.example.f1database.controller;

import org.example.f1database.entity.Race;
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
    public List<Race> getAllRaces() {
        return raceService.getAllRaces();
    }

    @GetMapping("/{id}")
    public Race getRaceById(@PathVariable Long id) {
        return raceService.getRaceById(id);
    }

    @PostMapping
    public Race createRace(@RequestBody Race race) {
        return raceService.createRace(race);
    }

    @PutMapping("/{id}")
    public Race updateRace(@PathVariable Long id,
                           @RequestBody Race race) {
        return raceService.updateRace(id, race);
    }

    @DeleteMapping("/{id}")
    public void deleteRace(@PathVariable Long id) {
        raceService.deleteRace(id);
    }
}