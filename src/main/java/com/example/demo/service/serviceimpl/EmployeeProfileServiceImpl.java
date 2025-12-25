package com.example.demo.service.impl;

import com.example.demo.dto.EmployeeProfileDto;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.EmployeeProfile;
import com.example.demo.repository.EmployeeProfileRepository;
import com.example.demo.service.EmployeeProfileService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeProfileServiceImpl implements EmployeeProfileService {

    private final EmployeeProfileRepository employeeRepo;

    public EmployeeProfileServiceImpl(EmployeeProfileRepository employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    @Override
    public EmployeeProfileDto create(EmployeeProfileDto dto) {
        EmployeeProfile e = new EmployeeProfile();
        e.setEmployeeId(dto.employeeId);
        e.setFullName(dto.fullName);
        e.setEmail(dto.email);
        e.setTeamName(dto.teamName);
        e.setRole(dto.role);
        employeeRepo.save(e);
        dto.id = e.getId();
        return dto;
    }

    @Override
    public EmployeeProfileDto update(Long id, EmployeeProfileDto dto) {
        EmployeeProfile e = employeeRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        e.setFullName(dto.fullName);
        e.setTeamName(dto.teamName);
        e.setRole(dto.role);
        employeeRepo.save(e);

        dto.id = e.getId();
        return dto;
    }

    @Override
    public void deactivate(Long id) {
        EmployeeProfile e = employeeRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        e.setActive(false);
        employeeRepo.save(e);
    }

    @Override
    public EmployeeProfileDto getById(Long id) {
        EmployeeProfile e = employeeRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        EmployeeProfileDto dto = new EmployeeProfileDto();
        dto.id = e.getId();
        dto.employeeId = e.getEmployeeId();
        dto.fullName = e.getFullName();
        dto.email = e.getEmail();
        dto.teamName = e.getTeamName();
        dto.role = e.getRole();
        return dto;
    }

    @Override
    public List<EmployeeProfileDto> getByTeam(String teamName) {
        return employeeRepo.findByTeamNameAndActiveTrue(teamName).stream()
                .map(e -> {
                    EmployeeProfileDto dto = new EmployeeProfileDto();
                    dto.id = e.getId();
                    dto.employeeId = e.getEmployeeId();
                    dto.fullName = e.getFullName();
                    dto.email = e.getEmail();
                    dto.teamName = e.getTeamName();
                    dto.role = e.getRole();
                    return dto;
                }).collect(Collectors.toList());
    }

    @Override
    public List<EmployeeProfileDto> getAll() {
        return employeeRepo.findAll().stream().map(e -> {
            EmployeeProfileDto dto = new EmployeeProfileDto();
            dto.id = e.getId();
            dto.employeeId = e.getEmployeeId();
            dto.fullName = e.getFullName();
            dto.email = e.getEmail();
            dto.teamName = e.getTeamName();
            dto.role = e.getRole();
            return dto;
        }).collect(Collectors.toList());
    }
}
