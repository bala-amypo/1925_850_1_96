package com.example.demo.service.impl; // MUST BE .impl

import com.example.demo.dto.CapacityAnalysisResultDto;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.CapacityAnalysisService;
import com.example.demo.util.DateRangeUtil;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;

@Service
public class CapacityAnalysisServiceImpl implements CapacityAnalysisService {
    private final TeamCapacityConfigRepository capacityRepo;
    private final EmployeeProfileRepository employeeRepo;
    private final LeaveRequestRepository leaveRepo;
    private final CapacityAlertRepository alertRepo;

    public CapacityAnalysisServiceImpl(TeamCapacityConfigRepository capacityRepo, 
                                       EmployeeProfileRepository employeeRepo, 
                                       LeaveRequestRepository leaveRepo, 
                                       CapacityAlertRepository alertRepo) {
        this.capacityRepo = capacityRepo;
        this.employeeRepo = employeeRepo;
        this.leaveRepo = leaveRepo;
        this.alertRepo = alertRepo;
    }

    @Override
    public CapacityAnalysisResultDto analyzeTeamCapacity(String teamName, LocalDate start, LocalDate end) {
        if (start.isAfter(end)) {
            throw new BadRequestException("Start date cannot be in the future of end date");
        }

        TeamCapacityConfig config = capacityRepo.findByTeamName(teamName)
                .orElseThrow(() -> new ResourceNotFoundException("Capacity config not found"));

        if (config.getTotalHeadcount() < 1) {
            throw new BadRequestException("Invalid total headcount");
        }

        List<LocalDate> range = DateRangeUtil.daysBetween(start, end);
        Map<LocalDate, Double> capacityByDate = new HashMap<>();
        boolean risky = false;

        for (LocalDate date : range) {
            long onLeave = leaveRepo.findApprovedOnDate(date).stream()
                    .filter(l -> l.getEmployee().getTeamName().equals(teamName))
                    .count();
            
            double capacity = ((double)(config.getTotalHeadcount() - onLeave) / config.getTotalHeadcount()) * 100;
            capacityByDate.put(date, capacity);
            
            if (capacity < config.getMinCapacityPercent()) {
                risky = true;
                alertRepo.save(new CapacityAlert(teamName, date, "HIGH", "Low capacity"));
            }
        }
        return new CapacityAnalysisResultDto(risky, capacityByDate);
    }
}