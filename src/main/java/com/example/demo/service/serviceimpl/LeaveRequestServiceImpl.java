package com.example.demo.service.impl;

import com.example.demo.dto.LeaveRequestDto;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.EmployeeProfile;
import com.example.demo.model.LeaveRequest;
import com.example.demo.repository.EmployeeProfileRepository;
import com.example.demo.repository.LeaveRequestRepository;
import com.example.demo.service.LeaveRequestService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class LeaveRequestServiceImpl implements LeaveRequestService {

    private final LeaveRequestRepository leaveRepo;
    private final EmployeeProfileRepository employeeRepo;

    public LeaveRequestServiceImpl(
            LeaveRequestRepository leaveRepo,
            EmployeeProfileRepository employeeRepo) {
        this.leaveRepo = leaveRepo;
        this.employeeRepo = employeeRepo;
    }

    public LeaveRequestDto create(LeaveRequestDto dto) {

        if (dto.getStartDate().isAfter(dto.getEndDate())) {
            throw new BadRequestException("Invalid date range");
        }

        EmployeeProfile emp = employeeRepo.findById(dto.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        LeaveRequest leave = new LeaveRequest();
        leave.setEmployee(emp);
        leave.setStartDate(dto.getStartDate());
        leave.setEndDate(dto.getEndDate());
        leave.setType(dto.getType());
        leave.setStatus("PENDING");

        leaveRepo.save(leave);

        dto.setId(leave.getId());
        dto.setStatus("PENDING");
        return dto;
    }

    public LeaveRequestDto approve(Long id) {
        LeaveRequest leave = leaveRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave not found"));
        leave.setStatus("APPROVED");
        leaveRepo.save(leave);

        LeaveRequestDto dto = new LeaveRequestDto();
        dto.setId(leave.getId());
        dto.setStatus("APPROVED");
        return dto;
    }

    public LeaveRequestDto reject(Long id) {
        LeaveRequest leave = leaveRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave not found"));
        leave.setStatus("REJECTED");
        leaveRepo.save(leave);

        LeaveRequestDto dto = new LeaveRequestDto();
        dto.setId(leave.getId());
        dto.setStatus("REJECTED");
        return dto;
    }

    public List<LeaveRequestDto> getByEmployee(Long employeeId) {
        EmployeeProfile emp = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        return leaveRepo.findByEmployee(emp)
                .stream()
                .map(l -> {
                    LeaveRequestDto d = new LeaveRequestDto();
                    d.setId(l.getId());
                    d.setStatus(l.getStatus());
                    return d;
                }).collect(Collectors.toList());
    }

    public List<LeaveRequestDto> getOverlappingForTeam(
            String team, LocalDate start, LocalDate end) {

        return leaveRepo
                .findApprovedOverlappingForTeam(team, start, end)
                .stream()
                .map(l -> {
                    LeaveRequestDto d = new LeaveRequestDto();
                    d.setId(l.getId());
                    return d;
                }).collect(Collectors.toList());
    }
}
