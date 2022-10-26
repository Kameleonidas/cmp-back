package pl.gov.cmp.auth.service;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.gov.cmp.auth.exception.InvalidUserPeselException;

import java.time.LocalDate;

@Slf4j
@Component
@NoArgsConstructor
public class PeselValidatorService {

    private final byte[] pesel = new byte[11];

    public LocalDate getUserBirthDateFromPesel (String pesel) {

        int birthDay;
        int birthMonth;
        int birthYear;

        if (pesel.length() != 11){
            throw new InvalidUserPeselException(pesel);
        }
        else {
            for (int i = 0; i < 11; i++){
                this.pesel[i] = Byte.parseByte(pesel.substring(i, i+1));
            }
            if (checkSum() && checkMonth() && checkDay()) {
                birthDay = getBirthDay();
                birthMonth = getBirthMonth();
                birthYear = getBirthYear();
            }
            else {
                throw new InvalidUserPeselException(pesel);
            }
        }
        return LocalDate.of(birthYear, birthMonth, birthDay);
    }
    public int getBirthYear() {
        int year;
        int month;
        year = 10 * pesel[0];
        year += pesel[1];
        month = 10 * pesel[2];
        month += pesel[3];
        if (month > 80 && month < 93) {
            year += 1800;
        }
        else if (month > 0 && month < 13) {
            year += 1900;
        }
        else if (month > 20 && month < 33) {
            year += 2000;
        }
        else if (month > 40 && month < 53) {
            year += 2100;
        }
        else if (month > 60 && month < 73) {
            year += 2200;
        }
        return year;
    }

    public int getBirthMonth() {
        int month;
        month = 10 * pesel[2];
        month += pesel[3];
        if (month > 80 && month < 93) {
            month -= 80;
        }
        else if (month > 20 && month < 33) {
            month -= 20;
        }
        else if (month > 40 && month < 53) {
            month -= 40;
        }
        else if (month > 60 && month < 73) {
            month -= 60;
        }
        return month;
    }


    public int getBirthDay() {
        int day;
        day = 10 * pesel[4];
        day += pesel[5];
        log.info("Day: {}", day);
        return day;
    }

    private boolean checkSum() {
        int sum = pesel[0] +
                3 * pesel[1] +
                7 * pesel[2] +
                9 * pesel[3] +
                pesel[4] +
                3 * pesel[5] +
                7 * pesel[6] +
                9 * pesel[7] +
                pesel[8] +
                3 * pesel[9];
        sum %= 10;
        sum = 10 - sum;
        sum %= 10;

        return sum == pesel[10];
    }

    private boolean checkMonth() {
        int month = getBirthMonth();
        return month > 0 && month < 13;
    }

    private boolean checkDay() {
        int year = getBirthYear();
        int month = getBirthMonth();
        int day = getBirthDay();
        if ((day >0 && day < 32) &&
                (month == 1 || month == 3 || month == 5 ||
                        month == 7 || month == 8 || month == 10 ||
                        month == 12)) {
            return true;
        }
        else if ((day >0 && day < 31) &&
                (month == 4 || month == 6 || month == 9 ||
                        month == 11)) {
            return true;
        }
        else return (day > 0 && day < 30 && leapYear(year)) ||
                    (day > 0 && day < 29 && !leapYear(year));
    }

    private boolean leapYear(int year) {
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
    }

}
