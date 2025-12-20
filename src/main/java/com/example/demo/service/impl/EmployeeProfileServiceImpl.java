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

    private final EmployeeProfileRepository repo;

    public EmployeeProfileServiceImpl(EmployeeProfileRepository repo) {
        this.repo = repo;
    }

    @Override
    public EmployeeProfileDto create(EmployeeProfileDto dto) {
        EmployeeProfile e = new EmployeeProfile();
        e.setEmployeeId(dto.employeeId);
        e.setFullName(dto.fullName);
        e.setEmail(dto.email);
        e.setTeamName(dto.teamName);
        e.setRole(dto.role);
        e.setActive(true);

        repo.save(e);
        dto.id = e.getId();
        return dto;
    }

    @Override
    public EmployeeProfileDto update(Long id, EmployeeProfileDto dto) {
        EmployeeProfile e = repo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found"));

        e.setFullName(dto.fullName);
        e.setTeamName(dto.teamName);
        e.setRole(dto.role);

        repo.save(e);
        dto.id = e.getId();
        return dto;
    }

    @Override
    public void deactivate(Long id) {
        EmployeeProfile e = repo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found"));
        e.setActive(false);
        repo.save(e);
    }

    @Override
    public EmployeeProfileDto getById(Long id) {
        EmployeeProfile e = repo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found"));

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
        return repo.findByTeamNameAndActiveTrue(teamName)
                .stream()
                .map(e -> {
                    EmployeeProfileDto d = new EmployeeProfileDto();
                    d.id = e.getId();
                    d.employeeId = e.getEmployeeId();
                    d.fullName = e.getFullName();
                    d.email = e.getEmail();
                    d.teamName = e.getTeamName();
                    d.role = e.getRole();
                    return d;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeProfileDto> getAll() {
        return repo.findAll()
                .stream()
                .map(e -> {
                    EmployeeProfileDto d = new EmployeeProfileDto();
                    d.id = e.getId();
                    d.employeeId = e.getEmployeeId();
                    d.fullName = e.getFullName();
                    d.email = e.getEmail();
                    d.teamName = e.getTeamName();
                    d.role = e.getRole();
                    return d;
                })
                .collect(Collectors.toList());
    }
}
