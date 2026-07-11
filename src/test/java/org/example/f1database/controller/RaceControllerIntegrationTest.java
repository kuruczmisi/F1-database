package org.example.f1database.controller;

import org.example.f1database.entity.Driver;
import org.example.f1database.entity.Race;
import org.example.f1database.entity.Team;
import org.example.f1database.repository.DriverRepository;
import org.example.f1database.repository.RaceRepository;
import org.example.f1database.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RaceControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RaceRepository raceRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private TeamRepository teamRepository;

    private Driver savedDriver;

    @BeforeEach
    void setUp() {
        raceRepository.deleteAll();
        driverRepository.deleteAll();
        teamRepository.deleteAll();

        Team team = new Team();
        team.setName("Ferrari");
        team.setCountry("Italy");
        team.setTeamPrincipal("Fred Vasseur");
        team.setFoundedYear(1929);

        Team savedTeam = teamRepository.save(team);

        Driver driver = new Driver();
        driver.setName("Lewis Hamilton");
        driver.setNationality("British");
        driver.setNumber(44);
        driver.setTeam(savedTeam);

        savedDriver = driverRepository.save(driver);
    }

    @Test
    void createRace_shouldReturnCreatedRaceWithDriver() throws Exception {

        String requestBody = """
                {
                    "name": "Hungarian Grand Prix",
                    "location": "Hungary",
                    "year": 2025,
                    "driverIds": [%d]
                }
                """.formatted(savedDriver.getId());

        mockMvc.perform(post("/races")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Hungarian Grand Prix"))
                .andExpect(jsonPath("$.location").value("Hungary"))
                .andExpect(jsonPath("$.year").value(2025))
                .andExpect(jsonPath("$.driverIds[0]").value(savedDriver.getId()))
                .andExpect(jsonPath("$.driverNames[0]").value("Lewis Hamilton"));
    }

    @Test
    void getRaceById_shouldReturnRace() throws Exception {

        Race race = new Race();
        race.setName("Monaco Grand Prix");
        race.setLocation("Monaco");
        race.setYear(2025);
        race.getDrivers().add(savedDriver);

        Race savedRace = raceRepository.save(race);

        mockMvc.perform(get("/races/{id}", savedRace.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedRace.getId()))
                .andExpect(jsonPath("$.name").value("Monaco Grand Prix"))
                .andExpect(jsonPath("$.location").value("Monaco"))
                .andExpect(jsonPath("$.driverIds[0]").value(savedDriver.getId()));
    }

    @Test
    void updateRace_shouldReturnUpdatedRace() throws Exception {

        Race race = new Race();
        race.setName("Hungarian Grand Prix");
        race.setLocation("Hungary");
        race.setYear(2025);
        race.getDrivers().add(savedDriver);

        Race savedRace = raceRepository.save(race);

        String requestBody = """
                {
                    "name": "Updated Hungarian Grand Prix",
                    "location": "Budapest",
                    "year": 2026,
                    "driverIds": [%d]
                }
                """.formatted(savedDriver.getId());

        mockMvc.perform(put("/races/{id}", savedRace.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedRace.getId()))
                .andExpect(jsonPath("$.name").value("Updated Hungarian Grand Prix"))
                .andExpect(jsonPath("$.location").value("Budapest"))
                .andExpect(jsonPath("$.year").value(2026))
                .andExpect(jsonPath("$.driverIds[0]").value(savedDriver.getId()));
    }

    @Test
    void deleteRace_shouldReturnSuccessMessage() throws Exception {

        Race race = new Race();
        race.setName("Italian Grand Prix");
        race.setLocation("Italy");
        race.setYear(2025);
        race.getDrivers().add(savedDriver);

        Race savedRace = raceRepository.save(race);

        mockMvc.perform(delete("/races/{id}", savedRace.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Race deleted successfully."));
    }

    @Test
    void getRaceById_shouldReturnNotFound() throws Exception {

        mockMvc.perform(get("/races/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error")
                        .value("Race not found with id: 999"));
    }
}