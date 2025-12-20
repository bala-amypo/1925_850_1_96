package com.example.demo.service.impl;

import com.example.demo.dto.CapacityAnalysisResultDto;
import com.example.demo.service.CapacityAnalysisService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CapacityAnalysisServiceImpl implements CapacityAnalysisService {

    @Override
    public CapacityAnalysisResultDto analyzeTeamCapacity(
            String teamName, LocalDate start, LocalDate end) {

        CapacityAnalysisResultDto dto = new CapacityAnalysisResultDto();
        dto.setTeamName(teamName);
        dto.setWithinCapacity(true);
        dto.setMessage("Capacity is within limit");
        return dto;
    }
}
