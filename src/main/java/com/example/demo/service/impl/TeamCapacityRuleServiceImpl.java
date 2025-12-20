package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.TeamCapacityConfig;
import com.example.demo.repository.TeamCapacityConfigRepository;
import com.example.demo.service.TeamCapacityRuleService;
import org.springframework.stereotype.Service;

@Service
public class TeamCapacityRuleServiceImpl
        implements TeamCapacityRuleService {

    private final TeamCapacityConfigRepository repo;

    public TeamCapacityRuleServiceImpl(
            TeamCapacityConfigRepository repo) {
        this.repo = repo;
    }

    @Override
    public TeamCapacityConfig createRule(
            TeamCapacityConfig rule) {

        if (rule.getTotalHeadcount() <= 0) {
            throw new BadRequestException(
                    "Invalid total headcount");
        }

        return repo.save(rule);
    }

    @Override
    public TeamCapacityConfig updateRule(
            Long id, TeamCapacityConfig rule) {

        TeamCapacityConfig existing =
                repo.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Rule not found"));

        existing.setTotalHeadcount(
                rule.getTotalHeadcount());
        existing.setMinCapacityPercent(
                rule.getMinCapacityPercent());

        return repo.save(existing);
    }

    @Override
    public TeamCapacityConfig getRuleByTeam(
            String teamName) {

        return repo.findByTeamName(teamName)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Capacity config not found"));
    }
}
