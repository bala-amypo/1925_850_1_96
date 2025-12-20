package com.example.demo.controller;

import com.example.demo.dto.CapacityAnalysisResultDto;
import com.example.demo.model.CapacityAlert;
import com.example.demo.repository.CapacityAlertRepository;
import com.example.demo.service.CapacityAnalysisService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/capacity")
public class CapacityAlertController {

    private final CapacityAnalysisService analysisService;
    private final CapacityAlertRepository alertRepository;

    public CapacityAlertController(
            CapacityAnalysisService analysisService,
            CapacityAlertRepository alertRepository) {
        this.analysisService = analysisService;
        this.alertRepository = alertRepository;
    }

    @PostMapping("/analyze")
    public CapacityAnalysisResultDto analyze(
            @RequestParam String teamName,
            @RequestParam LocalDate start,
            @RequestParam LocalDate end) {

        return analysisService.analyzeTeamCapacity(
                teamName, start, end);
    }

    @GetMapping("/alerts/{teamName}")
    public List<CapacityAlert> getAlerts(
            @PathVariable String teamName,
            @RequestParam LocalDate start,
            @RequestParam LocalDate end) {

        return alertRepository
                .findByTeamNameAndDateBetween(
                        teamName, start, end);
    }
}
