package com.example.demo.service.impl;

import com.example.demo.model.TeamCapacityConfig;
import com.example.demo.service.TeamCapacityRuleService;
import org.springframework.stereotype.Service;

@Service
public class TeamCapacityRuleServiceImpl implements TeamCapacityRuleService {

    private TeamCapacityConfig config;

    @Override
    public TeamCapacityConfig createRule(TeamCapacityConfig rule) {
        this.config = rule;
        return rule;
    }

    @Override
    public TeamCapacityConfig updateRule(Long id, TeamCapacityConfig rule) {
        this.config = rule;
        return rule;
    }

    @Override
    public TeamCapacityConfig getRuleByTeam(String teamName) {
        return config;
    }
}
