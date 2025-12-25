package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "teamName"))
public class TeamCapacityConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String teamName;
    private int totalHeadcount;
    private int minCapacityPercent;

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }

    public int getTotalHeadcount() { return totalHeadcount; }
    public void setTotalHeadcount(int totalHeadcount) {
        this.totalHeadcount = totalHeadcount;
    }

    public int getMinCapacityPercent() { return minCapacityPercent; }
    public void setMinCapacityPercent(int minCapacityPercent) {
        this.minCapacityPercent = minCapacityPercent;
    }
}
