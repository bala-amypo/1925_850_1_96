package com.example.demo.dto;
import lombok.Data;

@Data
public class EmployeeProfileDto {
    private Long id;
    private String employeeId;
    private String fullName;
    private String email;
    private String teamName;
    private String role;
}