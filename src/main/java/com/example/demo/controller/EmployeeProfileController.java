package com.example.demo.controller;
import com.example.demo.dto.EmployeeProfileDto;
import com.example.demo.service.EmployeeProfileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
@Tag(name = "Employee Profiles")
public class EmployeeProfileController {
    private final EmployeeProfileService service;
    public EmployeeProfileController(EmployeeProfileService service) { this.service = service; }

    @PostMapping("/") public EmployeeProfileDto create(@RequestBody EmployeeProfileDto dto) { return service.create(dto); }
    @GetMapping("/{id}") public EmployeeProfileDto get(@PathVariable Long id) { return service.getById(id); }
    @GetMapping("/team/{teamName}") public List<EmployeeProfileDto> getTeam(@PathVariable String teamName) { return service.getByTeam(teamName); }
}