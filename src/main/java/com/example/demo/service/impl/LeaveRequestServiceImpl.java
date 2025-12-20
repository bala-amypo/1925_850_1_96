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
    private final EmployeeProfileRepository empRepo;

    public LeaveRequestServiceImpl(LeaveRequestRepository leaveRepo,
                                   EmployeeProfileRepository empRepo) {
        this.leaveRepo = leaveRepo;
        this.empRepo = empRepo;
    }

    @Override
    public LeaveRequestDto create(LeaveRequestDto dto) {

        if (dto.startDate.isAfter(dto.endDate)) {
            throw new BadRequestException("Start date invalid");
        }

        EmployeeProfile emp = empRepo.findById(dto.employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found"));

        LeaveRequest leave = new LeaveRequest();
        leave.setEmployee(emp);
        leave.setStartDate(dto.startDate);
        leave.setEndDate(dto.endDate);
        leave.setType(dto.type);
        leave.setReason(dto.reason);
        leave.setStatus("PENDING");

        leaveRepo.save(leave);

        dto.id = leave.getId();
        dto.status = leave.getStatus();
        return dto;
    }

    @Override
    public LeaveRequestDto approve(Long id) {
        LeaveRequest leave = leaveRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Leave not found"));

        leave.setStatus("APPROVED");
        leaveRepo.save(leave);

        LeaveRequestDto dto = new LeaveRequestDto();
        dto.id = leave.getId();
        dto.status = leave.getStatus();
        return dto;
    }

    @Override
    public LeaveRequestDto reject(Long id) {
        LeaveRequest leave = leaveRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Leave not found"));

        leave.setStatus("REJECTED");
        leaveRepo.save(leave);

        LeaveRequestDto dto = new LeaveRequestDto();
        dto.id = leave.getId();
        dto.status = leave.getStatus();
        return dto;
    }

    @Override
    public List<LeaveRequestDto> getByEmployee(Long employeeId) {
        EmployeeProfile emp = empRepo.findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found"));

        return leaveRepo.findByEmployee(emp)
                .stream()
                .map(l -> {
                    LeaveRequestDto d = new LeaveRequestDto();
                    d.id = l.getId();
                    d.status = l.getStatus();
                    return d;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<LeaveRequestDto> getOverlappingForTeam(
            String teamName, LocalDate start, LocalDate end) {

        return leaveRepo
                .findApprovedOverlappingForTeam(teamName, start, end)
                .stream()
                .map(l -> {
                    LeaveRequestDto d = new LeaveRequestDto();
                    d.id = l.getId();
                    d.status = l.getStatus();
                    return d;
                })
                .collect(Collectors.toList());
    }
}
