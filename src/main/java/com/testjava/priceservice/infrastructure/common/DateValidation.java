package com.testjava.priceservice.infrastructure.common;

/**
 * Date/time validation constants.
 */
public final class DateValidation {
    public static final int DATE_PARTS_LENGTH = 4;
    public static final int PARTS_INDEX_MONTH = 1;
    public static final int PARTS_INDEX_DAY = 2;
    public static final int PARTS_INDEX_TIME = 3;
    public static final int TIME_INDEX_HOUR = 0;
    public static final int TIME_INDEX_MINUTE = 1;
    public static final int TIME_INDEX_SECOND = 2;

    public static final int MIN_MONTH = 1;
    public static final int MAX_MONTH = 12;
    public static final int MIN_DAY = 1;
    public static final int MAX_DAY = 31;
    public static final int MIN_HOUR = 0;
    public static final int MAX_HOUR = 23;
    public static final int MIN_MINUTE = 0;
    public static final int MAX_MINUTE = 59;
    public static final int MIN_SECOND = 0;
    public static final int MAX_SECOND = 59;

    private DateValidation() {
        // Utility class
    }
}
