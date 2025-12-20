package com.example.demo.service.impl;

import com.example.demo.dto.CapacityAnalysisResultDto;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.CapacityAlert;
import com.example.demo.model.TeamCapacityConfig;
import com.example.demo.repository.CapacityAlertRepository;
import com.example.demo.repository.LeaveRequestRepository;
import com.example.demo.repository.TeamCapacityConfigRepository;
import com.example.demo.service.CapacityAnalysisService;
import com.example.demo.util.DateRangeUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class CapacityAnalysisServiceImpl
        implements CapacityAnalysisService {

    private final TeamCapacityConfigRepository capRepo;
    private final LeaveRequestRepository leaveRepo;
    private final CapacityAlertRepository alertRepo;

    public CapacityAnalysisServiceImpl(
            TeamCapacityConfigRepository capRepo,
            LeaveRequestRepository leaveRepo,
            CapacityAlertRepository alertRepo) {
        this.capRepo = capRepo;
        this.leaveRepo = leaveRepo;
        this.alertRepo = alertRepo;
    }

    @Override
    public CapacityAnalysisResultDto analyzeTeamCapacity(
            String teamName, LocalDate start, LocalDate end) {

        if (start.isAfter(end)) {
            throw new BadRequestException("Start date invalid");
        }

        TeamCapacityConfig config =
                capRepo.findByTeamName(teamName)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Capacity config not found"));

        Map<LocalDate, Double> capacityMap = new HashMap<>();
        boolean risky = false;

        for (LocalDate date : DateRangeUtil.daysBetween(start, end)) {

            int onLeave =
                    leaveRepo.findApprovedOnDate(date).size();

            double capacity =
                    ((config.getTotalHeadcount() - onLeave) * 100.0)
                            / config.getTotalHeadcount();

            capacityMap.put(date, capacity);

            if (capacity < config.getMinCapacityPercent()) {
                risky = true;
                alertRepo.save(new CapacityAlert(
                        teamName, date,
                        "HIGH",
                        "Capacity below threshold"));
            }
        }

        CapacityAnalysisResultDto dto =
                new CapacityAnalysisResultDto();
        dto.capacityByDate = capacityMap;
        dto.risky = risky;

        return dto;
    }
}
