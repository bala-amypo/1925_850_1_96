package com.example.demo.service.serviceimpli;

import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.TeamCapacityConfig;
import com.example.demo.repository.TeamCapacityConfigRepository;
import org.springframework.stereotype.Service;

@Service
public class TeamCapacityRuleService {

    private final TeamCapacityConfigRepository teamCapacityConfigRepository;

    public TeamCapacityRuleService(TeamCapacityConfigRepository teamCapacityConfigRepository) {
        this.teamCapacityConfigRepository = teamCapacityConfigRepository;
    }

    public TeamCapacityConfig createRule(TeamCapacityConfig rule) {
        if (rule.getTotalHeadcount() < 1) {
            throw new BadRequestException("Invalid total headcount");
        }
        if (rule.getMinCapacityPercent() < 1 || rule.getMinCapacityPercent() > 100) {
            throw new BadRequestException("Invalid capacity percentage");
        }
        return teamCapacityConfigRepository.save(rule);
    }

    public TeamCapacityConfig updateRule(Long id, TeamCapacityConfig updatedRule) {
        TeamCapacityConfig existing = teamCapacityConfigRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team capacity rule not found"));
        
        if (updatedRule.getTotalHeadcount() < 1) {
            throw new BadRequestException("Invalid total headcount");
        }
        
        existing.setTotalHeadcount(updatedRule.getTotalHeadcount());
        existing.setMinCapacityPercent(updatedRule.getMinCapacityPercent());
        return teamCapacityConfigRepository.save(existing);
    }

    public TeamCapacityConfig getRuleByTeam(String teamName) {
        return teamCapacityConfigRepository.findByTeamName(teamName)
                .orElseThrow(() -> new ResourceNotFoundException("Capacity config not found"));
    }
}
