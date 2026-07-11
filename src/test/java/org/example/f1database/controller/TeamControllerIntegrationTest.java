package org.example.f1database.controller;

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

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TeamControllerIntegrationTest {

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private RaceRepository raceRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TeamRepository teamRepository;

    @BeforeEach
    void setUp() {
        resultRepository.deleteAll();
        raceRepository.deleteAll();
        driverRepository.deleteAll();
        teamRepository.deleteAll();
    }

    @Test
    void createTeam_shouldReturnCreatedTeam() throws Exception {

        String requestBody = """
                {
                    "name": "Ferrari",
                    "country": "Italy",
                    "teamPrincipal": "Fred Vasseur",
                    "foundedYear": 1929
                }
                """;

        mockMvc.perform(post("/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ferrari"))
                .andExpect(jsonPath("$.country").value("Italy"))
                .andExpect(jsonPath("$.teamPrincipal").value("Fred Vasseur"))
                .andExpect(jsonPath("$.foundedYear").value(1929));
    }

    @Test
    void getAllTeams_shouldReturnTeams() throws Exception {

        Team team = createTeam(
                "Mercedes",
                "Germany",
                "Toto Wolff",
                1954
        );

        teamRepository.save(team);

        mockMvc.perform(get("/teams"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Mercedes"))
                .andExpect(jsonPath("$[0].country").value("Germany"));
    }

    @Test
    void getTeamById_shouldReturnTeam() throws Exception {

        Team savedTeam = teamRepository.save(
                createTeam(
                        "Ferrari",
                        "Italy",
                        "Fred Vasseur",
                        1929
                )
        );

        mockMvc.perform(get("/teams/{id}", savedTeam.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedTeam.getId()))
                .andExpect(jsonPath("$.name").value("Ferrari"))
                .andExpect(jsonPath("$.country").value("Italy"))
                .andExpect(jsonPath("$.teamPrincipal").value("Fred Vasseur"))
                .andExpect(jsonPath("$.foundedYear").value(1929));
    }

    @Test
    void searchTeamsByCountry_shouldReturnMatchingTeams() throws Exception {

        teamRepository.save(
                createTeam(
                        "Ferrari",
                        "Italy",
                        "Fred Vasseur",
                        1929
                )
        );

        teamRepository.save(
                createTeam(
                        "Mercedes",
                        "Germany",
                        "Toto Wolff",
                        1954
                )
        );

        mockMvc.perform(get("/teams/country/{country}", "Italy"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Ferrari"))
                .andExpect(jsonPath("$[0].country").value("Italy"));
    }

    @Test
    void updateTeam_shouldReturnUpdatedTeam() throws Exception {

        Team savedTeam = teamRepository.save(
                createTeam(
                        "Ferrari",
                        "Italy",
                        "Fred Vasseur",
                        1929
                )
        );

        String requestBody = """
                {
                    "name": "Scuderia Ferrari",
                    "country": "Italy",
                    "teamPrincipal": "Fred Vasseur",
                    "foundedYear": 1929
                }
                """;

        mockMvc.perform(put("/teams/{id}", savedTeam.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedTeam.getId()))
                .andExpect(jsonPath("$.name").value("Scuderia Ferrari"))
                .andExpect(jsonPath("$.country").value("Italy"))
                .andExpect(jsonPath("$.teamPrincipal").value("Fred Vasseur"))
                .andExpect(jsonPath("$.foundedYear").value(1929));
    }

    @Test
    void deleteTeam_shouldReturnSuccessMessage() throws Exception {

        Team savedTeam = teamRepository.save(
                createTeam(
                        "Ferrari",
                        "Italy",
                        "Fred Vasseur",
                        1929
                )
        );

        mockMvc.perform(delete("/teams/{id}", savedTeam.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Team deleted successfully."));
    }

    @Test
    void getTeamById_shouldReturnNotFound() throws Exception {

        mockMvc.perform(get("/teams/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error")
                        .value("Team not found with id: 999"));
    }

    @Test
    void createTeam_shouldReturnValidationError() throws Exception {

        String requestBody = """
                {
                    "name": "",
                    "country": "",
                    "teamPrincipal": "",
                    "foundedYear": 1800
                }
                """;

        mockMvc.perform(post("/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    private Team createTeam(String name,
                            String country,
                            String teamPrincipal,
                            int foundedYear) {

        Team team = new Team();
        team.setName(name);
        team.setCountry(country);
        team.setTeamPrincipal(teamPrincipal);
        team.setFoundedYear(foundedYear);

        return team;
    }
}