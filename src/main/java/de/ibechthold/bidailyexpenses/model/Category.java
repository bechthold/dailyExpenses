package de.ibechthold.bidailyexpenses.model;

import de.ibechthold.bidailyexpenses.settings.AppTexts;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Model class for Category
 */
public class Category {
    //region constants

    //endregion

    //region attributes
    private int id;
    private StringProperty categoryName;
    private StringProperty categorySign;
    private StringProperty categoryIcon;
    //endregion

    //region constructors
    public Category() {
        this.categoryName = new SimpleStringProperty(AppTexts.DEF_STRING_VALUE);
        this.categorySign = new SimpleStringProperty(AppTexts.DEF_STRING_VALUE);
        this.categoryIcon = new SimpleStringProperty(AppTexts.DEF_STRING_VALUE);
    }

    public Category(String categoryName, String categorySign, String categoryIcon) {
        this.categoryName = new SimpleStringProperty(categoryName);
        this.categorySign = new SimpleStringProperty(categorySign);
        this.categoryIcon = new SimpleStringProperty(categoryIcon);
    }
    //endregion

    //region methods
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getCategoryIcon() {
        return categoryIcon.get();
    }

    public StringProperty categoryIconProperty() {
        return categoryIcon;
    }

    public void setCategoryIcon(String categoryIcon) {
        this.categoryIcon.set(categoryIcon);
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", categoryName=" + categoryName +
                ", categorySign=" + categorySign +
                ", categoryIcon=" + categoryIcon +
                '}';
    }
    //endregion
}
