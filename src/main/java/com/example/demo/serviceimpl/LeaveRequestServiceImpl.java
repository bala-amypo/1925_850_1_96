package com.example.demo.service.impl;

import com.example.demo.dto.LeaveRequestDto;
import com.example.demo.service.LeaveRequestService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class LeaveRequestServiceImpl implements LeaveRequestService {

    private final List<LeaveRequestDto> requests = new ArrayList<>();

    @Override
    public LeaveRequestDto create(LeaveRequestDto dto) {
        requests.add(dto);
        return dto;
    }

    @Override
    public LeaveRequestDto approve(Long id) {
        return requests.stream().findFirst().orElse(null);
    }

    @Override
    public LeaveRequestDto reject(Long id) {
        return requests.stream().findFirst().orElse(null);
    }

    @Override
    public List<LeaveRequestDto> getByEmployee(Long employeeId) {
        return requests;
    }

    @Override
    public List<LeaveRequestDto> getOverlappingForTeam(
            String teamName, LocalDate start, LocalDate end) {
        return requests;
    }
}
