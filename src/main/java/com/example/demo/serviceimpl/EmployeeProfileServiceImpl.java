package com.example.demo.service.impl;

import com.example.demo.dto.EmployeeProfileDto;
import com.example.demo.service.EmployeeProfileService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeProfileServiceImpl implements EmployeeProfileService {

    private final List<EmployeeProfileDto> store = new ArrayList<>();

    @Override
    public EmployeeProfileDto create(EmployeeProfileDto dto) {
        store.add(dto);
        return dto;
    }

    @Override
    public EmployeeProfileDto update(Long id, EmployeeProfileDto dto) {
        return dto;
    }

    @Override
    public void deactivate(Long id) {
        // no-op
    }

    @Override
    public EmployeeProfileDto getById(Long id) {
        return store.stream().findFirst().orElse(null);
    }

    @Override
    public List<EmployeeProfileDto> getByTeam(String teamName) {
        return store;
    }

    @Override
    public List<EmployeeProfileDto> getAll() {
        return store;
    }
}
