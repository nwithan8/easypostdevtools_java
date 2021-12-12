package com.nwithan8.easypostdevtools.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Dates {
    private static LocalDateTime nowLocalDateTime = LocalDateTime.now();

    private static Calendar nowCalendar = Calendar.getInstance();

    private static Date nowDate = nowCalendar.getTime();

    private static boolean isLastDayOfMonth(LocalDateTime date) {
        YearMonth yearMonth =
                YearMonth.of(date.getYear(), date.getMonthValue());
        LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();
        return lastDayOfMonth.getDayOfMonth() == date.getDayOfMonth();
    }

    private static boolean isLastMonthOfYear(LocalDateTime date) {
        return date.getMonth() == Month.DECEMBER;
    }

    private static boolean isLastDayOfYear(LocalDateTime date) {
        return date.getMonth() == Month.DECEMBER && date.getDayOfMonth() == 31;
    }

    public static Calendar getFutureDateThisYear() {
        // will return a date in the future, this year
        if (isLastDayOfYear(nowLocalDateTime)) {
            throw new RuntimeException("This year is over.");
        }

        int month;
        int day;

        if (isLastDayOfMonth(nowLocalDateTime)) {
            // pull from next month on
            month = Random.getRandomIntInRange(
                    nowLocalDateTime.getMonthValue() + 1, 12);

        } else {
            // pull from next day on
            month = Random.getRandomIntInRange(nowLocalDateTime.getMonthValue(),
                    12);
        }
        YearMonth yearMonth = YearMonth.of(nowLocalDateTime.getYear(), month);
        int daysInMonth = yearMonth.lengthOfMonth();

        if (month == nowLocalDateTime.getMonthValue()) {
            // pull from tomorrow on
            day = Random.getRandomIntInRange(
                    nowLocalDateTime.getDayOfMonth() + 1, daysInMonth);
        } else {
            // pull from day 1 on
            day = Random.getRandomIntInRange(1, daysInMonth);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(nowLocalDateTime.getYear(), month, day);
        return calendar;
    }

    public static Date getFutureDateThisMonth() {
        // will return a date in the future, this month
        if (isLastDayOfMonth(nowLocalDateTime)) {
            throw new RuntimeException("This month is over.");
        }

        YearMonth yearMonth = YearMonth.of(nowLocalDateTime.getYear(),
                nowLocalDateTime.getMonthValue());
        int daysInMonth = yearMonth.lengthOfMonth();

        int day =
                Random.getRandomIntInRange(nowLocalDateTime.getDayOfMonth() + 1,
                        daysInMonth);

        Calendar calendar = Calendar.getInstance();
        calendar.set(nowLocalDateTime.getYear(),
                nowLocalDateTime.getMonthValue(), day);
        return calendar.getTime();
    }

    public static Date getDateAfter(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (date.getMonth() == Calendar.DECEMBER) {
            // if it's December, set up the next date to be in January
            calendar.add(Calendar.MONTH, Calendar.JANUARY);
            calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
        }
        calendar.add(Calendar.DAY_OF_MONTH, Random.getRandomIntInRange(1, 30));
        return calendar.getTime();
    }

    public static Date getDateBefore(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH,
                -1 * Random.getRandomIntInRange(1, 30));
        return calendar.getTime();
    }

    public static List<Date> getFutureDates(int numberOfDates) {
        // returns a list of dates in chronological order
        List<Date> dates = new ArrayList<Date>();
        Date currentDate = nowDate;
        for (int i = 0; i < numberOfDates; i++) {
            currentDate = getDateAfter(currentDate);
            dates.add(currentDate);
        }
        return dates;
    }

    public static List<Date> getPastDates(int numberOfDates) {
        // returns a list of dates in backwards chronological order
        List<Date> dates = new ArrayList<Date>();
        Date currentDate = nowDate;
        for (int i = 0; i < numberOfDates; i++) {
            currentDate = getDateBefore(currentDate);
            dates.add(currentDate);
        }
        return dates;
    }
}
