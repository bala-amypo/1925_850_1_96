package com.example.demo.service.impl;

import com.example.demo.dto.CapacityAnalysisResultDto;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.CapacityAlert;
import com.example.demo.model.EmployeeProfile;
import com.example.demo.model.LeaveRequest;
import com.example.demo.model.TeamCapacityConfig;
import com.example.demo.repository.CapacityAlertRepository;
import com.example.demo.repository.EmployeeProfileRepository;
import com.example.demo.repository.LeaveRequestRepository;
import com.example.demo.repository.TeamCapacityConfigRepository;
import com.example.demo.service.CapacityAnalysisService;
import com.example.demo.util.DateRangeUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class CapacityAnalysisServiceImpl implements CapacityAnalysisService {

    private final TeamCapacityConfigRepository configRepo;
    private final EmployeeProfileRepository employeeRepo;
    private final LeaveRequestRepository leaveRepo;
    private final CapacityAlertRepository alertRepo;

    public CapacityAnalysisServiceImpl(TeamCapacityConfigRepository configRepo,
                                       EmployeeProfileRepository employeeRepo,
                                       LeaveRequestRepository leaveRepo,
                                       CapacityAlertRepository alertRepo) {
        this.configRepo = configRepo;
        this.employeeRepo = employeeRepo;
        this.leaveRepo = leaveRepo;
        this.alertRepo = alertRepo;
    }

    @Override
    public CapacityAnalysisResultDto analyzeTeamCapacity(String teamName, LocalDate start, LocalDate end) {
        if (start.isAfter(end))
            throw new BadRequestException("Start date cannot be after end date");

        TeamCapacityConfig config = configRepo.findByTeamName(teamName)
                .orElseThrow(() -> new ResourceNotFoundException("Capacity config not found"));

        List<EmployeeProfile> employees = employeeRepo.findByTeamNameAndActiveTrue(teamName);
        List<LeaveRequest> leaves = leaveRepo.findApprovedOverlappingForTeam(teamName, start, end);

        Map<LocalDate, Double> capacityByDate = new LinkedHashMap<>();
        boolean breach = false;

        for (LocalDate date : DateRangeUtil.getDatesBetween(start, end)) {
            long onLeave = leaves.stream()
                    .filter(lr -> !lr.getStartDate().isAfter(date) && !lr.getEndDate().isBefore(date))
                    .count();
            double percent = (employees.size() - onLeave) * 100.0 / employees.size();
            capacityByDate.put(date, percent);
            if (percent < config.getMinCapacityPercent()) {
                breach = true;

                CapacityAlert alert = new CapacityAlert();
                alert.setTeamName(teamName);
                alert.setDate(date);
                alert.setSeverity("HIGH");
                alert.setMessage("Capacity below threshold");
                alertRepo.save(alert);
            }
        }

        CapacityAnalysisResultDto dto = new CapacityAnalysisResultDto();
        dto.teamName = teamName;
        dto.availableCount = new HashMap<>();
        for (Map.Entry<LocalDate, Double> e : capacityByDate.entrySet())
            dto.availableCount.put(e.getKey(), e.getValue().intValue());
        dto.breach = breach;
        return dto;
    }
}
