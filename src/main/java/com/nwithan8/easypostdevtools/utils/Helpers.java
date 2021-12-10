package com.nwithan8.easypostdevtools.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Helpers {

    public static class Dates {

        private static LocalDateTime now = LocalDateTime.now();

        private static boolean isLastDayOfMonth(LocalDateTime date) {
            YearMonth yearMonth = YearMonth.of(date.getYear(), date.getMonthValue());
            LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();
            return lastDayOfMonth.getDayOfMonth() == date.getDayOfMonth();
        }

        private static boolean isLastMonthOfYear(LocalDateTime date) {
            return date.getMonth() == Month.DECEMBER;
        }

        private static boolean isLastDayOfYear(LocalDateTime date) {
            return date.getMonth() == Month.DECEMBER && date.getDayOfMonth() == 31;
        }

        public static Date getFutureDateThisYear() {
            // will return a date in the future, this year
            if (isLastDayOfYear(now)) {
                throw new RuntimeException("This year is over.");
            }

            int month;
            int day;

            if (isLastDayOfMonth(now)) {
                // pull from next month on
                month = Random.getRandomIntInRange(now.getMonthValue() + 1, 12);

            } else {
                // pull from next day on
                month = Random.getRandomIntInRange(now.getMonthValue(), 12);
            }
            YearMonth yearMonth = YearMonth.of(now.getYear(), month);
            int daysInMonth = yearMonth.lengthOfMonth();

            if (month == now.getMonthValue()) {
                // pull from tomorrow on
                day = Random.getRandomIntInRange(now.getDayOfMonth() + 1, daysInMonth);
            } else {
                // pull from day 1 on
                day = Random.getRandomIntInRange(1, daysInMonth);
            }

            Calendar calendar = Calendar.getInstance();
            calendar.set(now.getYear(), month, day);
            return calendar.getTime();
        }

        public static Date getFutureDateThisMonth() {
            // will return a date in the future, this month
            if (isLastDayOfMonth(now)) {
                throw new RuntimeException("This month is over.");
            }

            YearMonth yearMonth = YearMonth.of(now.getYear(), now.getMonthValue());
            int daysInMonth = yearMonth.lengthOfMonth();

            int day = Random.getRandomIntInRange(now.getDayOfMonth() + 1, daysInMonth);

            Calendar calendar = Calendar.getInstance();
            calendar.set(now.getYear(), now.getMonthValue(), day);
            return calendar.getTime();
        }
    }
}
