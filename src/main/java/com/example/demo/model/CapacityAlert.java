package com.example.demo.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity @Data @NoArgsConstructor
public class CapacityAlert {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String teamName;
    private LocalDate date;
    private String severity;
    private String message;

    public CapacityAlert(String teamName, LocalDate date, String severity, String message) {
        this.teamName = teamName;
        this.date = date;
        this.severity = severity;
        this.message = message;
    }
}