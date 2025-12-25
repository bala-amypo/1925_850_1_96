package com.example.demo.controller;

import com.example.demo.model.TeamCapacityConfig;
import com.example.demo.service.TeamCapacityRuleService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/capacity-rules")
public class TeamCapacityRuleController {
    
    private final TeamCapacityRuleService teamCapacityRuleService;

    public TeamCapacityRuleController(TeamCapacityRuleService teamCapacityRuleService) {
        this.teamCapacityRuleService = teamCapacityRuleService;
    }

    @PostMapping
    public TeamCapacityConfig createRule(@RequestBody TeamCapacityConfig rule) {
        return teamCapacityRuleService.createRule(rule);
    }

    @PutMapping("/{id}")
    public TeamCapacityConfig updateRule(@PathVariable Long id, @RequestBody TeamCapacityConfig updatedRule) {
        return teamCapacityRuleService.updateRule(id, updatedRule);
    }

    @GetMapping("/team/{teamName}")
    public TeamCapacityConfig getRuleByTeam(@PathVariable String teamName) {
        return teamCapacityRuleService.getRuleByTeam(teamName);
    }
}
