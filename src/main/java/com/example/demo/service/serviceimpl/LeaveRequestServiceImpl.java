package com.example.demo.service.impl;

import com.example.demo.dto.LeaveRequestDto;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.EmployeeProfile;
import com.example.demo.model.LeaveRequest;
import com.example.demo.repository.EmployeeProfileRepository;
import com.example.demo.repository.LeaveRequestRepository;
import com.example.demo.service.LeaveRequestService;
import com.example.demo.util.DateRangeUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaveRequestServiceImpl implements LeaveRequestService {

    private final LeaveRequestRepository leaveRepo;
    private final EmployeeProfileRepository employeeRepo;

    public LeaveRequestServiceImpl(LeaveRequestRepository leaveRepo,
                                   EmployeeProfileRepository employeeRepo) {
        this.leaveRepo = leaveRepo;
        this.employeeRepo = employeeRepo;
    }

    @Override
    public LeaveRequestDto create(LeaveRequestDto dto) {
        EmployeeProfile emp = employeeRepo.findById(dto.employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        if (dto.startDate.isAfter(dto.endDate)) {
            throw new BadRequestException("Start date cannot be after end date");
        }

        LeaveRequest lr = new LeaveRequest();
        lr.setEmployee(emp);
        lr.setStartDate(dto.startDate);
        lr.setEndDate(dto.endDate);
        lr.setType(dto.type);
        lr.setStatus("PENDING");
        lr.setReason(dto.reason);

        leaveRepo.save(lr);
        dto.id = lr.getId();
        dto.status = lr.getStatus();
        return dto;
    }

    @Override
    public LeaveRequestDto approve(Long id) {
        LeaveRequest lr = leaveRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave not found"));
        lr.setStatus("APPROVED");
        leaveRepo.save(lr);

        LeaveRequestDto dto = new LeaveRequestDto();
        dto.id = lr.getId();
        dto.status = lr.getStatus();
        return dto;
    }

    @Override
    public LeaveRequestDto reject(Long id) {
        LeaveRequest lr = leaveRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave not found"));
        lr.setStatus("REJECTED");
        leaveRepo.save(lr);

        LeaveRequestDto dto = new LeaveRequestDto();
        dto.id = lr.getId();
        dto.status = lr.getStatus();
        return dto;
    }

    @Override
    public List<LeaveRequestDto> getByEmployee(Long employeeId) {
        EmployeeProfile emp = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        return leaveRepo.findByEmployee(emp).stream().map(lr -> {
            LeaveRequestDto dto = new LeaveRequestDto();
            dto.id = lr.getId();
            dto.employeeId = emp.getId();
            dto.startDate = lr.getStartDate();
            dto.endDate = lr.getEndDate();
            dto.type = lr.getType();
            dto.status = lr.getStatus();
            dto.reason = lr.getReason();
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<LeaveRequestDto> getOverlappingForTeam(String teamName, LocalDate start, LocalDate end) {
        return leaveRepo.findApprovedOverlappingForTeam(teamName, start, end)
                .stream().map(lr -> {
                    LeaveRequestDto dto = new LeaveRequestDto();
                    dto.id = lr.getId();
                    dto.employeeId = lr.getEmployee().getId();
                    dto.startDate = lr.getStartDate();
                    dto.endDate = lr.getEndDate();
                    dto.type = lr.getType();
                    dto.status = lr.getStatus();
                    dto.reason = lr.getReason();
                    return dto;
                }).collect(Collectors.toList());
    }
}
