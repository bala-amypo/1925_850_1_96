package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(
    name = "team_capacity_config",
    uniqueConstraints = @UniqueConstraint(columnNames = "teamName")
)
public class TeamCapacityConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String teamName;
    private int totalHeadcount;
    private int minCapacityPercent;

    // getters & setters
}
