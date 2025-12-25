package com.example.demo.service;

import com.example.demo.dto.CapacityAnalysisResultDto;

import java.time.LocalDate;

public interface CapacityAnalysisService {

    CapacityAnalysisResultDto analyzeTeamCapacity(
            String team,
            LocalDate start,
            LocalDate end
    );
}
