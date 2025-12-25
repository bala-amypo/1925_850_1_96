package com.example.demo.dto;
import lombok.*;
import java.time.LocalDate;
import java.util.Map;

@Data @AllArgsConstructor @NoArgsConstructor
public class CapacityAnalysisResultDto {
    private boolean risky;
    private Map<LocalDate, Double> capacityByDate;
}