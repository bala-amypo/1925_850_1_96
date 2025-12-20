package com.example.demo.service.serviceimpli;

import com.example.demo.dto.CapacityAnalysisResultDto;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.CapacityAlert;
import com.example.demo.model.LeaveRequest;
import com.example.demo.model.TeamCapacityConfig;
import com.example.demo.repository.CapacityAlertRepository;
import com.example.demo.repository.LeaveRequestRepository;
import com.example.demo.repository.TeamCapacityConfigRepository;
import com.example.demo.util.DateRangeUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CapacityAnalysisService {

    private final TeamCapacityConfigRepository teamCapacityConfigRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final CapacityAlertRepository capacityAlertRepository;

    public CapacityAnalysisService(TeamCapacityConfigRepository teamCapacityConfigRepository,
                                   LeaveRequestRepository leaveRequestRepository,
                                   CapacityAlertRepository capacityAlertRepository) {
        this.teamCapacityConfigRepository = teamCapacityConfigRepository;
        this.leaveRequestRepository = leaveRequestRepository;
        this.capacityAlertRepository = capacityAlertRepository;
    }

    public CapacityAnalysisResultDto analyzeTeamCapacity(String teamName, LocalDate start, LocalDate end) {
        // Validate date range
        if (start.isAfter(end)) {
            throw new BadRequestException("Start date cannot be after end date");
        }

        // Fetch team configuration
        TeamCapacityConfig config = teamCapacityConfigRepository.findByTeamName(teamName)
                .orElseThrow(() -> new ResourceNotFoundException("Capacity config not found"));

        if (config.getTotalHeadcount() <= 0) {
            throw new BadRequestException("Invalid total headcount");
        }

        // Get approved overlapping leaves
        List<LeaveRequest> overlappingLeaves = leaveRequestRepository.findApprovedOverlappingForTeam(teamName, start, end);
        List<LocalDate> dateRange = DateRangeUtil.daysBetween(start, end);

        Map<LocalDate, Double> capacityByDate = new HashMap<>();
        boolean risky = false;

        for (LocalDate date : dateRange) {
            long leavesOnDate = overlappingLeaves.stream()
                    .filter(leave -> !date.isBefore(leave.getStartDate()) && !date.isAfter(leave.getEndDate()))
                    .count();

            int presentCount = config.getTotalHeadcount() - (int) leavesOnDate;
            double capacityPercent = (presentCount * 100.0) / config.getTotalHeadcount();
            capacityByDate.put(date, capacityPercent);

            if (capacityPercent < config.getMinCapacityPercent()) {
                risky = true;
                CapacityAlert alert = new CapacityAlert(teamName, date, "HIGH",
                        "Team capacity below threshold: " + capacityPercent + "%");
                capacityAlertRepository.save(alert);
            }
        }

        return new CapacityAnalysisResultDto(risky, capacityByDate);
    }
}
