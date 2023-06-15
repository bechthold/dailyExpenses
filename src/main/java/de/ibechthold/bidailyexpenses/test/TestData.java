package de.ibechthold.bidailyexpenses.test;

import de.ibechthold.bidailyexpenses.model.Category;

import java.util.ArrayList;
import java.util.List;

public class TestData {
    //region constants
    //endregion

    //region attributes
    //endregion

    //region constructors
    //endregion

    //region methods
    public static List<Category> getTestCategories() {
        List<Category> testCategories = new ArrayList<>();

        testCategories.add(new Category("categoryName", "+", "/icon.png"));

        return testCategories;
    }
    //endregion
}
