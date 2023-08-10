package com.demo.mongodb.MongoBenchmark.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateGenerator {

    private static final String MIN_DATE_STR = "2023-07-15";
    private static final String MAX_DATE_STR = "2023-07-30";
    private static final Date MIN_DATE;
    private static final Date MAX_DATE;

    static {
        try {
            MIN_DATE = new SimpleDateFormat("yyyy-MM-dd").parse(MIN_DATE_STR);
            MAX_DATE = new SimpleDateFormat("yyyy-MM-dd").parse(MAX_DATE_STR);
        } catch (ParseException e) {
            throw new ExceptionInInitializerError("Failed to parse date strings.");
        }
    }

    public static Date generateRandomStartDate() {
        long randomTime = MIN_DATE.getTime() + (long) (Math.random() * (MAX_DATE.getTime() - MIN_DATE.getTime()));
        return new Date(randomTime);
    }

    public static Date generateRandomEndDate(Date startDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.DATE, (int) (Math.random() * 7)); // Add 0 to 6 days
        Date endDate = calendar.getTime();

        if (endDate.after(MAX_DATE)) {
            endDate = MAX_DATE;
        }

        return endDate;
    }
}