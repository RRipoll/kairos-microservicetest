package com.testjava.priceservice.infrastructure.common;

/**
 * Date format constants used across the application.
 */
public final class DateFormats {
    public static final String API_DATE_TIME_FORMAT = "yyyy-MM-dd-HH:mm:ss";
    public static final String RESPONSE_DATE_TIME_FORMAT = "yyyy-MM-dd-HH.mm.ss";
    public static final String DATE_REGEX = "\\d{4}-\\d{2}-\\d{2}-\\d{2}:\\d{2}:\\d{2}";

    private DateFormats() {
        // Utility class
    }
}
