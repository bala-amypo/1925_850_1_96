package com.example.demo.dto;
import lombok.Data;
import java.time.LocalDate;

@Data
public class LeaveRequestDto {
    private Long id;
    private Long employeeId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String type;
    private String status;
    private String reason;
}