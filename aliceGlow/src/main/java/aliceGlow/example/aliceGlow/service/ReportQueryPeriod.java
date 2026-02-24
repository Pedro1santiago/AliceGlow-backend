package aliceGlow.example.aliceGlow.service;

import java.time.*;

public record ReportQueryPeriod(LocalDateTime start, LocalDateTime end) {

    public static ReportQueryPeriod from(LocalDateTime start, LocalDateTime end, Integer month, Integer year) {
        if (start != null && end != null) {
            return new ReportQueryPeriod(start, end);
        }
        if (month != null && year != null) {
            YearMonth ym = YearMonth.of(year, month);
            LocalDateTime s = ym.atDay(1).atStartOfDay();
            LocalDateTime e = ym.atEndOfMonth().atTime(LocalTime.MAX);
            return new ReportQueryPeriod(s, e);
        }
        LocalDate today = LocalDate.now();
        return new ReportQueryPeriod(today.atStartOfDay(), today.atTime(LocalTime.MAX));
    }
}
