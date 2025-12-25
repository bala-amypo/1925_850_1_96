package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.TeamCapacityConfig;
import com.example.demo.repository.TeamCapacityConfigRepository;
import com.example.demo.service.TeamCapacityRuleService;
import org.springframework.stereotype.Service;

@Service
public class TeamCapacityRuleServiceImpl implements TeamCapacityRuleService {
    private final TeamCapacityConfigRepository capacityRepo;

    public TeamCapacityRuleServiceImpl(TeamCapacityConfigRepository capacityRepo) {
        this.capacityRepo = capacityRepo;
    }

    @Override
    public void createRule(TeamCapacityConfig config) {
        validateConfig(config);
        capacityRepo.save(config);
    }

    @Override
    public TeamCapacityConfig getRuleByTeam(String teamName) {
        return capacityRepo.findByTeamName(teamName)
                .orElseThrow(() -> new ResourceNotFoundException("Capacity config not found"));
    }

    private void validateConfig(TeamCapacityConfig config) {
        if (config.getTotalHeadcount() < 1) {
            throw new BadRequestException("Invalid total headcount: must be at least 1");
        }
        if (config.getMinCapacityPercent() < 1 || config.getMinCapacityPercent() > 100) {
            throw new BadRequestException("minCapacityPercent must be between 1 and 100");
        }
    }
}