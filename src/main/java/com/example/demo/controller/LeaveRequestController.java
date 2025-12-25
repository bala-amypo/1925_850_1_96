package com.example.demo.controller;

import com.example.demo.dto.LeaveRequestDto;
import com.example.demo.service.LeaveRequestService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leaves")
public class LeaveRequestController {

    private final LeaveRequestService service;

    public LeaveRequestController(LeaveRequestService service) {
        this.service = service;
    }

    @PostMapping
    public LeaveRequestDto create(@RequestBody LeaveRequestDto dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}/approve")
    public LeaveRequestDto approve(@PathVariable Long id) {
        return service.approve(id);
    }

    @PutMapping("/{id}/reject")
    public LeaveRequestDto reject(@PathVariable Long id) {
        return service.reject(id);
    }

    @GetMapping("/employee/{id}")
    public List<LeaveRequestDto> getByEmployee(@PathVariable Long id) {
        return service.getByEmployee(id);
    }
}
