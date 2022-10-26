package pl.gov.cmp.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface DateProvider {
    LocalDateTime getCurrentTimestamp();

    LocalDate getCurrentDate();
}
