package com.example.demo.controller;

import com.example.demo.model.TeamCapacityConfig;
import com.example.demo.service.TeamCapacityRuleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/capacity-rules")
@Tag(name = "Capacity Rules")
public class TeamCapacityRuleController {
    private final TeamCapacityRuleService ruleService;

    public TeamCapacityRuleController(TeamCapacityRuleService ruleService) {
        this.ruleService = ruleService;
    }

    @PostMapping("/")
    public ResponseEntity<Void> create(@RequestBody TeamCapacityConfig config) {
        ruleService.createRule(config);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/team/{teamName}")
    public ResponseEntity<TeamCapacityConfig> get(@PathVariable String teamName) {
        return ResponseEntity.ok(ruleService.getRuleByTeam(teamName));
    }
}