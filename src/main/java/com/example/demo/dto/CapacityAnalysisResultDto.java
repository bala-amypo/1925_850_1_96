package com.example.demo.dto;

import java.time.LocalDate;
import java.util.Map;

public class CapacityAnalysisResultDto {
    public String teamName;
    public Map<LocalDate, Integer> availableCount;
    public boolean breach;
}
