package com.example.demo.model;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity @Data
public class LeaveRequest {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne private EmployeeProfile employee;
    private LocalDate startDate;
    private LocalDate endDate;
    private String type;
    private String status; // PENDING, APPROVED, REJECTED
    private String reason;
}