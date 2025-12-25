package com.example.demo.service.impl;

import com.example.demo.dto.LeaveRequestDto;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.EmployeeProfile;
import com.example.demo.model.LeaveRequest;
import com.example.demo.repository.EmployeeProfileRepository;
import com.example.demo.repository.LeaveRequestRepository;
import com.example.demo.service.LeaveRequestService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaveRequestServiceImpl implements LeaveRequestService {
    
    private final LeaveRequestRepository leaveRepository;
    private final EmployeeProfileRepository employeeRepository;

    public LeaveRequestServiceImpl(LeaveRequestRepository leaveRepository, EmployeeProfileRepository employeeRepository) {
        this.leaveRepository = leaveRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public LeaveRequestDto create(LeaveRequestDto dto) {
        EmployeeProfile employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        
        if (dto.getStartDate().isAfter(dto.getEndDate())) {
            throw new BadRequestException("Start date");
        }
        
        LeaveRequest entity = new LeaveRequest();
        entity.setEmployee(employee);
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setType(dto.getType());
        entity.setReason(dto.getReason());
        entity.setStatus("PENDING");
        
        entity = leaveRepository.save(entity);
        return mapToDto(entity);
    }

    @Override
    public LeaveRequestDto approve(Long id) {
        LeaveRequest entity = leaveRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found"));
        entity.setStatus("APPROVED");
        entity = leaveRepository.save(entity);
        return mapToDto(entity);
    }

    @Override
    public LeaveRequestDto reject(Long id) {
        LeaveRequest entity = leaveRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found"));
        entity.setStatus("REJECTED");
        entity = leaveRepository.save(entity);
        return mapToDto(entity);
    }

    @Override
    public List<LeaveRequestDto> getByEmployee(Long employeeId) {
        EmployeeProfile employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        return leaveRepository.findByEmployee(employee)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<LeaveRequestDto> getOverlappingForTeam(String teamName, LocalDate start, LocalDate end) {
        return leaveRepository.findApprovedOverlappingForTeam(teamName, start, end)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private LeaveRequestDto mapToDto(LeaveRequest entity) {
        LeaveRequestDto dto = new LeaveRequestDto();
        dto.setId(entity.getId());
        dto.setEmployeeId(entity.getEmployee().getId());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setType(entity.getType());
        dto.setStatus(entity.getStatus());
        dto.setReason(entity.getReason());
        return dto;
    }
}