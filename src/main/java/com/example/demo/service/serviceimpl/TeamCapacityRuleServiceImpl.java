package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.TeamCapacityConfig;
import com.example.demo.repository.TeamCapacityConfigRepository;
import com.example.demo.service.TeamCapacityRuleService;
import org.springframework.stereotype.Service;

@Service
public class TeamCapacityRuleServiceImpl implements TeamCapacityRuleService {
    
    private final TeamCapacityConfigRepository repository;

    public TeamCapacityRuleServiceImpl(TeamCapacityConfigRepository repository) {
        this.repository = repository;
    }

    @Override
    public TeamCapacityConfig createRule(TeamCapacityConfig rule) {
        validateRule(rule);
        return repository.save(rule);
    }

    @Override
    public TeamCapacityConfig updateRule(Long id, TeamCapacityConfig updatedRule) {
        TeamCapacityConfig existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team capacity rule not found"));
        
        validateRule(updatedRule);
        existing.setTeamName(updatedRule.getTeamName());
        existing.setTotalHeadcount(updatedRule.getTotalHeadcount());
        existing.setMinCapacityPercent(updatedRule.getMinCapacityPercent());
        
        return repository.save(existing);
    }

    @Override
    public TeamCapacityConfig getRuleByTeam(String teamName) {
        return repository.findByTeamName(teamName)
                .orElseThrow(() -> new ResourceNotFoundException("Capacity config not found"));
    }

    private void validateRule(TeamCapacityConfig rule) {
        if (rule.getTotalHeadcount() == null || rule.getTotalHeadcount() < 1) {
            throw new BadRequestException("Invalid total headcount");
        }
        if (rule.getMinCapacityPercent() == null || rule.getMinCapacityPercent() < 1 || rule.getMinCapacityPercent() > 100) {
            throw new BadRequestException("Min capacity percent must be between 1 and 100");
        }
    }
}