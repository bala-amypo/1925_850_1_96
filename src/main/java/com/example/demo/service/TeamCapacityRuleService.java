package com.example.demo.service;

import com.example.demo.model.TeamCapacityConfig;

public interface TeamCapacityRuleService {
    void createRule(TeamCapacityConfig config);
    TeamCapacityConfig getRuleByTeam(String teamName);
}