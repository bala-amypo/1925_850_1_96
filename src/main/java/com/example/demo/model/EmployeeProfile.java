package com.example.demo.model;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity @Data
public class EmployeeProfile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true) private String employeeId;
    private String fullName;
    @Column(unique = true) private String email;
    private String teamName;
    private String role;
    private boolean active = true;
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<EmployeeProfile> colleagues = new HashSet<>();
}