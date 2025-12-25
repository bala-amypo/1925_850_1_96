package com.example.demo.dto;

import java.time.LocalDate;

public class LeaveRequestDto {
    private Long id;
    private Long employeeId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String type;
    private String status;
    private String reason;
    // getters & setters
}
