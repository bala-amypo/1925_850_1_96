package com.example.demo.controller;

import com.example.demo.dto.LeaveRequestDto;
import com.example.demo.service.LeaveRequestService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/leaves")
@Tag(name = "Leave Requests")
public class LeaveRequestController {
    private final LeaveRequestService leaveService;

    public LeaveRequestController(LeaveRequestService leaveService) {
        this.leaveService = leaveService;
    }

    @PostMapping("/")
    public ResponseEntity<LeaveRequestDto> create(@RequestBody LeaveRequestDto dto) {
        return ResponseEntity.ok(leaveService.create(dto));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<LeaveRequestDto> approve(@PathVariable Long id) {
        return ResponseEntity.ok(leaveService.approve(id));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<LeaveRequestDto> reject(@PathVariable Long id) {
        return ResponseEntity.ok(leaveService.reject(id));
    }

    @GetMapping("/employee/{id}")
    public ResponseEntity<List<LeaveRequestDto>> getByEmployee(@PathVariable Long id) {
        return ResponseEntity.ok(leaveService.getByEmployee(id));
    }

    @GetMapping("/team-overlaps")
    public ResponseEntity<List<LeaveRequestDto>> getOverlaps(
            @RequestParam String teamName,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(leaveService.getOverlappingForTeam(teamName, start, end));
    }
}