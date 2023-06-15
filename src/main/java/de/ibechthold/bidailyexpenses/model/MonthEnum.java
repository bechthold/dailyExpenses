package de.ibechthold.bidailyexpenses.model;

/**
 * This class contains the names of the months in English
 */
public enum MonthEnum {
    //region constants
    JANUARY(1, "January"),
    FEBRUARY(2, "February"),
    MARCH(3, "March"),
    APRIL(4, "April"),
    MAY(5, "May"),
    JUNE(6, "June"),
    JULY(7, "July"),
    AUGUST(8, "August"),
    SEPTEMBER(9, "September"),
    OCTOBER(10, "October"),
    NOVEMBER(11, "November"),
    DECEMBER(12, "December");
    private final int monthNumber;
    private final String monthName;

    //endregion

    //region attributes
    //endregion

    //region constructors
    MonthEnum(int monthNumber, String monthNName) {
        this.monthNumber = monthNumber;
        this.monthName = monthNName;
    }
    //endregion

    //region methods
    public int getMonthNumber() {
        return monthNumber;
    }

    public String getMonthName() {
        return monthName;
    }
    //endregion
}
