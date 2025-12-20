package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class CapacityAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String teamName;
    private LocalDate date;
    private String severity;
    private String message;

    public CapacityAlert() {}

    public CapacityAlert(String teamName, LocalDate date, String severity, String message) {
        this.teamName = teamName;
        this.date = date;
        this.severity = severity;
        this.message = message;
    }

    // getters
    public Long getId() { return id; }
    public String getTeamName() { return teamName; }
    public LocalDate getDate() { return date; }
    public String getSeverity() { return severity; }
    public String getMessage() { return message; }
}
