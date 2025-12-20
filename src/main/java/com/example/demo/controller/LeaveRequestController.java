package com.example.demo.controller;

import com.example.demo.dto.LeaveRequestDto;
import com.example.demo.service.serviceimpli.LeaveRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/leaves")
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    public LeaveRequestController(LeaveRequestService leaveRequestService) {
        this.leaveRequestService = leaveRequestService;
    }

    @PostMapping
    public ResponseEntity<LeaveRequestDto> create(@RequestBody LeaveRequestDto dto) {
        return ResponseEntity.ok(leaveRequestService.create(dto));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<LeaveRequestDto> approve(@PathVariable Long id) {
        return ResponseEntity.ok(leaveRequestService.approve(id));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<LeaveRequestDto> reject(@PathVariable Long id) {
        return ResponseEntity.ok(leaveRequestService.reject(id));
    }

    @GetMapping("/employee/{id}")
    public ResponseEntity<List<LeaveRequestDto>> getByEmployee(@PathVariable Long id) {
        return ResponseEntity.ok(leaveRequestService.getByEmployee(id));
    }
}