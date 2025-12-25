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
        EmployeeProfile emp = new EmployeeProfile();
        emp.setEmployeeId(dto.getEmployeeId());
        emp.setFullName(dto.getFullName());
        emp.setEmail(dto.getEmail());
        emp.setTeamName(dto.getTeamName());
        emp.setRole(dto.getRole());
        EmployeeProfile saved = employeeRepo.save(emp);
        return mapToDto(saved);
    }

    @Override
    public EmployeeProfileDto update(Long id, EmployeeProfileDto dto) {
        EmployeeProfile emp = employeeRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not found"));
        emp.setFullName(dto.getFullName());
        emp.setTeamName(dto.getTeamName());
        emp.setRole(dto.getRole());
        return mapToDto(employeeRepo.save(emp));
    }

    @Override
    public void deactivate(Long id) {
        EmployeeProfile emp = employeeRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not found"));
        emp.setActive(false);
        employeeRepo.save(emp);
    }

    @Override
    public EmployeeProfileDto getById(Long id) {
        return employeeRepo.findById(id).map(this::mapToDto).orElseThrow(() -> new ResourceNotFoundException("Not found"));
    }

    @Override
    public List<EmployeeProfileDto> getByTeam(String teamName) {
        return employeeRepo.findByTeamNameAndActiveTrue(teamName).stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<EmployeeProfileDto> getAll() {
        return employeeRepo.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private EmployeeProfileDto mapToDto(EmployeeProfile emp) {
        EmployeeProfileDto dto = new EmployeeProfileDto();
        dto.setId(emp.getId());
        dto.setEmployeeId(emp.getEmployeeId());
        dto.setFullName(emp.getFullName());
        dto.setEmail(emp.getEmail());
        dto.setTeamName(emp.getTeamName());
        dto.setRole(emp.getRole());
        return dto;
    }
}