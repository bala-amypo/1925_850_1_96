package com.example.demo.model;
import jakarta.persistence.*;
import lombok.Data;

@Entity @Data
public class UserAccount {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true) private String email;
    private String password;
    private String role;
    @OneToOne private EmployeeProfile employeeProfile;
}