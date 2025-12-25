package com.example.demo.controller;
import com.example.demo.dto.CapacityAnalysisResultDto;
import com.example.demo.service.CapacityAnalysisService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/capacity-alerts")
@Tag(name = "Capacity Alerts")
public class CapacityAlertController {
    private final CapacityAnalysisService service;
    public CapacityAlertController(CapacityAnalysisService service) { this.service = service; }

    @PostMapping("/analyze")
    public CapacityAnalysisResultDto analyze(@RequestParam String teamName, @RequestParam String start, @RequestParam String end) {
        return service.analyzeTeamCapacity(teamName, LocalDate.parse(start), LocalDate.parse(end));
    }
}