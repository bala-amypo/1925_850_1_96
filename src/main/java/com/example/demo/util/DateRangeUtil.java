package com.example.demo.util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DateRangeUtil {

    public static List<LocalDate> getDatesBetween(
            LocalDate start, LocalDate end) {

        List<LocalDate> dates = new ArrayList<>();
        LocalDate d = start;

        while (!d.isAfter(end)) {
            dates.add(d);
            d = d.plusDays(1);
        }
        return dates;
    }
}
