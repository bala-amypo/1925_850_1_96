package com.example.demo.model;
import jakarta.persistence.*;
import lombok.Data;

@Entity @Data
public class TeamCapacityConfig {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true) private String teamName;
    private int totalHeadcount;
    private int minCapacityPercent;
}