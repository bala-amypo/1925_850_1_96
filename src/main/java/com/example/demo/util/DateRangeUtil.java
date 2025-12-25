package com.example.demo.util;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DateRangeUtil {
    public static List<LocalDate> daysBetween(LocalDate start, LocalDate end) {
        List<LocalDate> dates = new ArrayList<>();
        LocalDate curr = start;
        while (!curr.isAfter(end)) {
            dates.add(curr);
            curr = curr.plusDays(1);
        }
        return dates;
    }
}