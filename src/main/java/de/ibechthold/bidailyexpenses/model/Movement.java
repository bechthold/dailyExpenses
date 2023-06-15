package de.ibechthold.bidailyexpenses.model;

import de.ibechthold.bidailyexpenses.settings.AppTexts;
import javafx.beans.property.*;
import javafx.scene.image.ImageView;

/**
 * Model class for Movement
 */
public class Movement {
    //endregion

    //region attributes
    private int id;
    private ImageView icon;
    private DoubleProperty amount;
    private StringProperty categoryName;
    private StringProperty categorySign;
    private StringProperty detail;
    private StringProperty date;

    private StringProperty dateIndex;
    private IntegerProperty day;
    private IntegerProperty month;
    private IntegerProperty year;

    private IntegerProperty hour;
    private IntegerProperty minute;
    //endregion

    //region constructors
    public Movement() {

        this.amount = new SimpleDoubleProperty(AppTexts.DEF_DOUBLE_VALUE);
        this.categoryName = new SimpleStringProperty(AppTexts.DEF_STRING_VALUE);
        this.categorySign = new SimpleStringProperty(AppTexts.DEF_STRING_VALUE);
        this.detail = new SimpleStringProperty(AppTexts.DEF_STRING_VALUE);
        this.date = new SimpleStringProperty(AppTexts.DEF_STRING_VALUE);

        this.dateIndex = new SimpleStringProperty(AppTexts.DEF_STRING_VALUE);
        this.day = new SimpleIntegerProperty(AppTexts.DEF_INTEGER_VALUE);
        this.month = new SimpleIntegerProperty(AppTexts.DEF_INTEGER_VALUE);
        this.year = new SimpleIntegerProperty(AppTexts.DEF_INTEGER_VALUE);

        this.hour = new SimpleIntegerProperty(AppTexts.DEF_INTEGER_VALUE);
        this.minute = new SimpleIntegerProperty(AppTexts.DEF_INTEGER_VALUE);
    }

    public Movement(Double amount, String categoryName, String categorySign, String detail, String date, Integer hour, Integer minute) {

        this.amount = new SimpleDoubleProperty(amount);
        this.categoryName = new SimpleStringProperty(categoryName);
        this.categorySign = new SimpleStringProperty(categorySign);
        this.detail = new SimpleStringProperty(detail);
        this.date = new SimpleStringProperty(date);

        this.day = new SimpleIntegerProperty(createDay());
        this.month = new SimpleIntegerProperty(createMonth());
        this.year = new SimpleIntegerProperty(createYear());

        this.hour = new SimpleIntegerProperty(hour);
        this.minute = new SimpleIntegerProperty(minute);

        this.dateIndex = new SimpleStringProperty(createDateIndex());
    }
    //endregion

    //region methods
    //region getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ImageView getIcon() {
        return icon;
    }

    public void setIcon(ImageView icon) {
        this.icon = icon;
    }

    public double getAmount() {
        return amount.get();
    }

    public DoubleProperty amountProperty() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount.set(amount);
    }

    public String getCategoryName() {
        return categoryName.get();
    }

    public StringProperty categoryNameProperty() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName.set(categoryName);
    }

    public String getCategorySign() {
        return categorySign.get();
    }

    public StringProperty categorySignProperty() {
        return categorySign;
    }

    public void setCategorySign(String categorySign) {
        this.categorySign.set(categorySign);
    }

    public String getDetail() {
        return detail.get();
    }

    public StringProperty detailProperty() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail.set(detail);
    }

    public String getDate() {
        return date.get();
    }

    public StringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public String getDateIndex() {
        return dateIndex.get();
    }

    public StringProperty dateIndexProperty() {
        return dateIndex;
    }

    public void setDateIndex(String dateIndex) {
        this.dateIndex.set(dateIndex);
    }

    public int getDay() {
        return day.get();
    }

    public IntegerProperty dayProperty() {
        return day;
    }

    public void setDay(int day) {
        this.day.set(day);
    }

    public int getMonth() {
        return month.get();
    }

    public IntegerProperty monthProperty() {
        return month;
    }

    public void setMonth(int month) {
        this.month.set(month);
    }

    public int getYear() {
        return year.get();
    }

    public IntegerProperty yearProperty() {
        return year;
    }

    public void setYear(int year) {
        this.year.set(year);
    }

    public int getHour() {
        return hour.get();
    }

    public IntegerProperty hourProperty() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour.set(hour);
    }

    public int getMinute() {
        return minute.get();
    }

    public IntegerProperty minuteProperty() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute.set(minute);
    }


    //endregion

    private Integer createDay() {
        try {
            return Integer.valueOf(date.get().split("-")[2]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Integer createMonth() {
        try {
            return Integer.valueOf(date.get().split(AppTexts.DELIMITER)[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Integer createYear() {
        try {
            return Integer.valueOf(date.get().split(AppTexts.DELIMITER)[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String createDateIndex() {
        return String.format(AppTexts.TEMP_STRING_DATE_INDEX, createYear(), createMonth(), createDay(), hour.get(), minute.get());
    }

    public void createAndSetDateIndexAndDate(String date, Integer hour, Integer minute) {
        try {
            int year = Integer.parseInt(date.split(AppTexts.DELIMITER)[2]);
            int month = Integer.parseInt(date.split(AppTexts.DELIMITER)[1]);
            int day = Integer.parseInt(date.split(AppTexts.DELIMITER)[0]);
            String dateIndexString = String.format(AppTexts.TEMP_STRING_DATE_INDEX, year, month, day, hour, minute);
            setDateIndex(dateIndexString);
            setYear(year);
            setMonth(month);
            setDay(day);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Movement{" +
                "id=" + id +
                ", icon=" + icon +
                ", amount=" + amount +
                ", categoryName=" + categoryName +
                ", categorySign=" + categorySign +
                ", detail=" + detail +
                ", date=" + date +
                ", dateIndex=" + dateIndex +
                ", day=" + day +
                ", month=" + month +
                ", year=" + year +
                ", hour=" + hour +
                ", minute=" + minute +
                '}';
    }
    //endregion
}
