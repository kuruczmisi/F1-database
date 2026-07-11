package org.example.f1database.controller;

import org.example.f1database.entity.Driver;
import org.example.f1database.entity.Race;
import org.example.f1database.entity.Result;
import org.example.f1database.entity.Team;
import org.example.f1database.repository.DriverRepository;
import org.example.f1database.repository.RaceRepository;
import org.example.f1database.repository.ResultRepository;
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
class ResultControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private RaceRepository raceRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private TeamRepository teamRepository;

    private Driver savedDriver;
    private Race savedRace;

    @BeforeEach
    void setUp() {

        resultRepository.deleteAll();
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

        Race race = new Race();
        race.setName("Hungarian Grand Prix");
        race.setLocation("Hungary");
        race.setYear(2025);

        savedRace = raceRepository.save(race);
    }

    @Test
    void createResult_shouldReturnCreatedResult() throws Exception {

        String requestBody = """
                {
                    "position": 1,
                    "points": 25,
                    "driverId": %d,
                    "raceId": %d
                }
                """.formatted(savedDriver.getId(), savedRace.getId());

        mockMvc.perform(post("/results")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.position").value(1))
                .andExpect(jsonPath("$.points").value(25))
                .andExpect(jsonPath("$.driverId").value(savedDriver.getId()))
                .andExpect(jsonPath("$.driverName").value("Lewis Hamilton"))
                .andExpect(jsonPath("$.raceId").value(savedRace.getId()))
                .andExpect(jsonPath("$.raceName")
                        .value("Hungarian Grand Prix"));
    }

    @Test
    void getResultById_shouldReturnResult() throws Exception {

        Result result = new Result();
        result.setPosition(1);
        result.setPoints(25);
        result.setDriver(savedDriver);
        result.setRace(savedRace);

        Result savedResult = resultRepository.save(result);

        mockMvc.perform(get("/results/{id}", savedResult.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedResult.getId()))
                .andExpect(jsonPath("$.position").value(1))
                .andExpect(jsonPath("$.points").value(25))
                .andExpect(jsonPath("$.driverName")
                        .value("Lewis Hamilton"))
                .andExpect(jsonPath("$.raceName")
                        .value("Hungarian Grand Prix"));
    }

    @Test
    void updateResult_shouldReturnUpdatedResult() throws Exception {

        Result result = new Result();
        result.setPosition(1);
        result.setPoints(25);
        result.setDriver(savedDriver);
        result.setRace(savedRace);

        Result savedResult = resultRepository.save(result);

        String requestBody = """
                {
                    "position": 2,
                    "points": 18,
                    "driverId": %d,
                    "raceId": %d
                }
                """.formatted(savedDriver.getId(), savedRace.getId());

        mockMvc.perform(put("/results/{id}", savedResult.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedResult.getId()))
                .andExpect(jsonPath("$.position").value(2))
                .andExpect(jsonPath("$.points").value(18))
                .andExpect(jsonPath("$.driverName")
                        .value("Lewis Hamilton"));
    }

    @Test
    void deleteResult_shouldReturnSuccessMessage() throws Exception {

        Result result = new Result();
        result.setPosition(1);
        result.setPoints(25);
        result.setDriver(savedDriver);
        result.setRace(savedRace);

        Result savedResult = resultRepository.save(result);

        mockMvc.perform(delete("/results/{id}", savedResult.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Result deleted successfully."));
    }

    @Test
    void getResultById_shouldReturnNotFound() throws Exception {

        mockMvc.perform(get("/results/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error")
                        .value("Result not found with id: 999"));
    }
}