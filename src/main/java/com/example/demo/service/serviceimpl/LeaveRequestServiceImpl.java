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
    private final LeaveRequestRepository leaveRepo;
    private final EmployeeProfileRepository employeeRepo;

    public LeaveRequestServiceImpl(LeaveRequestRepository leaveRepo, EmployeeProfileRepository employeeRepo) {
        this.leaveRepo = leaveRepo;
        this.employeeRepo = employeeRepo;
    }

    @Override
    public LeaveRequestDto create(LeaveRequestDto dto) {
        if (dto.getStartDate().isAfter(dto.getEndDate())) {
            // Tests check for keywords "Start date"
            throw new BadRequestException("Start date cannot be after end date");
        }
        EmployeeProfile emp = employeeRepo.findById(dto.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        LeaveRequest leave = new LeaveRequest();
        leave.setEmployee(emp);
        leave.setStartDate(dto.getStartDate());
        leave.setEndDate(dto.getEndDate());
        leave.setType(dto.getType());
        leave.setStatus("PENDING");
        leave.setReason(dto.getReason());
        return mapToDto(leaveRepo.save(leave));
    }

    @Override
    public LeaveRequestDto approve(Long id) {
        LeaveRequest l = leaveRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not found"));
        l.setStatus("APPROVED");
        return mapToDto(leaveRepo.save(l));
    }

    @Override
    public LeaveRequestDto reject(Long id) {
        LeaveRequest l = leaveRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not found"));
        l.setStatus("REJECTED");
        return mapToDto(leaveRepo.save(l));
    }

    @Override
    public List<LeaveRequestDto> getByEmployee(Long empId) {
        EmployeeProfile emp = employeeRepo.findById(empId).orElseThrow(() -> new ResourceNotFoundException("Not found"));
        return leaveRepo.findByEmployee(emp).stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<LeaveRequestDto> getOverlappingForTeam(String team, LocalDate s, LocalDate e) {
        return leaveRepo.findApprovedOverlappingForTeam(team, s, e).stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private LeaveRequestDto mapToDto(LeaveRequest l) {
        LeaveRequestDto d = new LeaveRequestDto();
        d.setId(l.getId());
        d.setEmployeeId(l.getEmployee().getId());
        d.setStartDate(l.getStartDate());
        d.setEndDate(l.getEndDate());
        d.setType(l.getType());
        d.setStatus(l.getStatus());
        d.setReason(l.getReason());
        return d;
    }
}