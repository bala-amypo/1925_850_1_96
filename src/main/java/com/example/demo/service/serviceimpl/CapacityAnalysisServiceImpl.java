package com.example.demo.service.impl;

import com.example.demo.dto.CapacityAnalysisResultDto;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.CapacityAlert;
import com.example.demo.model.TeamCapacityConfig;
import com.example.demo.repository.CapacityAlertRepository;
import com.example.demo.repository.EmployeeProfileRepository;
import com.example.demo.repository.LeaveRequestRepository;
import com.example.demo.repository.TeamCapacityConfigRepository;
import com.example.demo.service.CapacityAnalysisService;
import com.example.demo.util.DateRangeUtil;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class CapacityAnalysisServiceImpl implements CapacityAnalysisService {

    private final TeamCapacityConfigRepository configRepo;
    private final EmployeeProfileRepository empRepo;
    private final LeaveRequestRepository leaveRepo;
    private final CapacityAlertRepository alertRepo;

    public CapacityAnalysisServiceImpl(
            TeamCapacityConfigRepository configRepo,
            EmployeeProfileRepository empRepo,
            LeaveRequestRepository leaveRepo,
            CapacityAlertRepository alertRepo) {

        this.configRepo = configRepo;
        this.empRepo = empRepo;
        this.leaveRepo = leaveRepo;
        this.alertRepo = alertRepo;
    }

    public CapacityAnalysisResultDto analyzeTeamCapacity(
            String team, LocalDate start, LocalDate end) {

        if (start.isAfter(end)) {
            throw new BadRequestException("Start date cannot be after end date");
        }

        TeamCapacityConfig config = configRepo.findByTeamName(team)
                .orElseThrow(() -> new ResourceNotFoundException("Capacity config not found"));

        if (config.getTotalHeadcount() <= 0) {
            throw new BadRequestException("Invalid total headcount");
        }

        Map<LocalDate, Integer> map = new HashMap<>();
        boolean risky = false;

        for (LocalDate d : DateRangeUtil.daysBetween(start, end)) {
            int leaves = leaveRepo
                    .findApprovedOverlappingForTeam(team, d, d)
                    .size();

            int available = config.getTotalHeadcount() - leaves;
            int capacity = (available * 100) / config.getTotalHeadcount();
            map.put(d, capacity);

            if (capacity < config.getMinCapacityPercent()) {
                risky = true;
                alertRepo.save(new CapacityAlert(
                        team, d, "HIGH", "Capacity below threshold"));
            }
        }

        CapacityAnalysisResultDto dto = new CapacityAnalysisResultDto();
        dto.setRisky(risky);
        dto.setCapacityByDate(map);
        return dto;
    }
}
