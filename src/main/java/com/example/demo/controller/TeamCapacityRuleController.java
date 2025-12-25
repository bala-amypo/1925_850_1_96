package com.example.demo.controller;

import com.example.demo.dto.TeamCapacityDto;
import com.example.demo.service.TeamCapacityService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/team-capacity")
public class TeamCapacityController {

    private final TeamCapacityService teamCapacityService;

    public TeamCapacityController(TeamCapacityService teamCapacityService) {
        this.teamCapacityService = teamCapacityService;
    }

    @GetMapping
    public TeamCapacityDto calculateTeamCapacity(
            @RequestParam String teamName,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        return teamCapacityService.calculateCapacity(
                teamName, startDate, endDate);
    }
}
