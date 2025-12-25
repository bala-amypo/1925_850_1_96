package com.example.demo.controller;

import com.example.demo.dto.CapacityAnalysisResultDto;
import com.example.demo.model.CapacityAlert;
import com.example.demo.repository.CapacityAlertRepository;
import com.example.demo.service.CapacityAnalysisService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/capacity-alerts")
public class CapacityAlertController {
    
    private final CapacityAnalysisService capacityAnalysisService;
    private final CapacityAlertRepository capacityAlertRepository;

    public CapacityAlertController(CapacityAnalysisService capacityAnalysisService, 
                                  CapacityAlertRepository capacityAlertRepository) {
        this.capacityAnalysisService = capacityAnalysisService;
        this.capacityAlertRepository = capacityAlertRepository;
    }

    @PostMapping("/analyze")
    public CapacityAnalysisResultDto analyzeCapacity(@RequestBody Map<String, Object> request) {
        String teamName = (String) request.get("teamName");
        LocalDate start = LocalDate.parse((String) request.get("start"));
        LocalDate end = LocalDate.parse((String) request.get("end"));
        return capacityAnalysisService.analyzeTeamCapacity(teamName, start, end);
    }

    @GetMapping("/team/{teamName}")
    public List<CapacityAlert> getAlertsByTeam(@PathVariable String teamName,
                                              @RequestParam(required = false) LocalDate start,
                                              @RequestParam(required = false) LocalDate end) {
        if (start != null && end != null) {
            return capacityAlertRepository.findByTeamNameAndDateBetween(teamName, start, end);
        }
        return capacityAlertRepository.findAll();
    }
}
