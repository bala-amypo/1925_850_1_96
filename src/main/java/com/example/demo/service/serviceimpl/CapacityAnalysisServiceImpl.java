package com.example.demo.service.impl;

import com.example.demo.dto.CapacityAnalysisResultDto;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.CapacityAlert;
import com.example.demo.model.LeaveRequest;
import com.example.demo.model.TeamCapacityConfig;
import com.example.demo.repository.CapacityAlertRepository;
import com.example.demo.repository.LeaveRequestRepository;
import com.example.demo.repository.TeamCapacityConfigRepository;
import com.example.demo.service.CapacityAnalysisService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CapacityAnalysisServiceImpl implements CapacityAnalysisService {
    
    private final TeamCapacityConfigRepository configRepository;
    private final LeaveRequestRepository leaveRepository;
    private final CapacityAlertRepository alertRepository;

    public CapacityAnalysisServiceImpl(TeamCapacityConfigRepository configRepository,
                                     LeaveRequestRepository leaveRepository,
                                     CapacityAlertRepository alertRepository) {
        this.configRepository = configRepository;
        this.leaveRepository = leaveRepository;
        this.alertRepository = alertRepository;
    }

    @Override
    public CapacityAnalysisResultDto analyzeTeamCapacity(String teamName, LocalDate start, LocalDate end) {
        if (start.isAfter(end)) {
            throw new BadRequestException("Start date");
        }
        
        TeamCapacityConfig config = configRepository.findByTeamName(teamName)
                .orElseThrow(() -> new ResourceNotFoundException("Capacity config not found"));
        
        if (config.getTotalHeadcount() <= 0) {
            throw new BadRequestException("Invalid total headcount");
        }
        
        List<LeaveRequest> overlappingLeaves = leaveRepository.findApprovedOverlappingForTeam(teamName, start, end);
        
        Map<LocalDate, Double> capacityByDate = new HashMap<>();
        boolean risky = false;
        
        LocalDate current = start;
        while (!current.isAfter(end)) {
            final LocalDate currentDate = current;
            long leavesOnDate = overlappingLeaves.stream()
                    .filter(leave -> !currentDate.isBefore(leave.getStartDate()) && !currentDate.isAfter(leave.getEndDate()))
                    .count();
            
            double capacity = ((double) (config.getTotalHeadcount() - leavesOnDate) / config.getTotalHeadcount()) * 100;
            capacityByDate.put(currentDate, capacity);
            
            if (capacity < config.getMinCapacityPercent()) {
                risky = true;
                CapacityAlert alert = new CapacityAlert(teamName, currentDate, "HIGH", 
                        "Team capacity below minimum threshold");
                alertRepository.save(alert);
            }
            
            current = current.plusDays(1);
        }
        
        return new CapacityAnalysisResultDto(risky, capacityByDate);
    }
}