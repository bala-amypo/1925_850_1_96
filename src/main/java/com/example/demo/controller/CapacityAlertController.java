package com.example.demo.controller;

import com.example.demo.dto.CapacityAnalysisResultDto;
import com.example.demo.service.serviceimpli.CapacityAnalysisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/capacity-alerts")
public class CapacityAlertController {

    private final CapacityAnalysisService capacityAnalysisService;

    public CapacityAlertController(CapacityAnalysisService capacityAnalysisService) {
        this.capacityAnalysisService = capacityAnalysisService;
    }

    @PostMapping("/analyze")
    public ResponseEntity<CapacityAnalysisResultDto> analyzeCapacity(
            @RequestParam String teamName,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        CapacityAnalysisResultDto result = capacityAnalysisService.analyzeTeamCapacity(teamName, startDate, endDate);
        return ResponseEntity.ok(result);
    }
}