package pl.gov.cmp.utils.impl;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DateProviderImpl implements pl.gov.cmp.utils.DateProvider {
    @Override
    public LocalDateTime getCurrentTimestamp() {
        return LocalDateTime.now();
    }

    @Override
    public LocalDate getCurrentDate() {
        return LocalDate.now();
    }
}
