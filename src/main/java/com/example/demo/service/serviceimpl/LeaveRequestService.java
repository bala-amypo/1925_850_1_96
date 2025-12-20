package com.example.demo.service.serviceimpli;

import com.example.demo.dto.LeaveRequestDto;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.EmployeeProfile;
import com.example.demo.model.LeaveRequest;
import com.example.demo.repository.EmployeeProfileRepository;
import com.example.demo.repository.LeaveRequestRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeProfileRepository employeeProfileRepository;

    public LeaveRequestService(LeaveRequestRepository leaveRequestRepository, 
                              EmployeeProfileRepository employeeProfileRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.employeeProfileRepository = employeeProfileRepository;
    }

    public LeaveRequestDto create(LeaveRequestDto dto) {
        if (dto.getStartDate().isAfter(dto.getEndDate())) {
            throw new BadRequestException("Start date cannot be in the future");
        }

        EmployeeProfile employee = employeeProfileRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        LeaveRequest leave = new LeaveRequest();
        leave.setEmployee(employee);
        leave.setStartDate(dto.getStartDate());
        leave.setEndDate(dto.getEndDate());
        leave.setType(dto.getType());
        leave.setReason(dto.getReason());

        LeaveRequest saved = leaveRequestRepository.save(leave);
        return mapToDto(saved);
    }

    public LeaveRequestDto approve(Long id) {
        LeaveRequest leave = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found"));
        leave.setStatus("APPROVED");
        LeaveRequest saved = leaveRequestRepository.save(leave);
        return mapToDto(saved);
    }

    public LeaveRequestDto reject(Long id) {
        LeaveRequest leave = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found"));
        leave.setStatus("REJECTED");
        LeaveRequest saved = leaveRequestRepository.save(leave);
        return mapToDto(saved);
    }

    public List<LeaveRequestDto> getByEmployee(Long employeeId) {
        EmployeeProfile employee = employeeProfileRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        return leaveRequestRepository.findByEmployee(employee)
                .stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public List<LeaveRequestDto> getOverlappingForTeam(String teamName, LocalDate start, LocalDate end) {
        return leaveRequestRepository.findApprovedOverlappingForTeam(teamName, start, end)
                .stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private LeaveRequestDto mapToDto(LeaveRequest leave) {
        LeaveRequestDto dto = new LeaveRequestDto();
        dto.setId(leave.getId());
        dto.setEmployeeId(leave.getEmployee().getId());
        dto.setStartDate(leave.getStartDate());
        dto.setEndDate(leave.getEndDate());
        dto.setType(leave.getType());
        dto.setStatus(leave.getStatus());
        dto.setReason(leave.getReason());
        return dto;
    }
}
