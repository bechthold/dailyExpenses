package de.ibechthold.bidailyexpenses.logic;

import de.ibechthold.bidailyexpenses.logic.db.DbManager;
import de.ibechthold.bidailyexpenses.model.Category;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * Implemented as a singleton and makes categories available as an ObservableList.
 */
public class CategoryHolder {
    //region constants
    //endregion

    //region attributes
    private static CategoryHolder instance;
    private ObservableList<Category> categories;
    private List<Category> normalCategoryList;
    //endregion

    //region constructors
    private CategoryHolder() {
        normalCategoryList = DbManager.getInstance().readAllCategories();

        categories = FXCollections.observableArrayList(category -> new Observable[]{
                category.categoryNameProperty(),
                category.categorySignProperty(),
                category.categoryIconProperty()});
        categories.addAll(normalCategoryList);
        categories.addListener(new CategoryListChangeListener());
    }
    //endregion

    //region methods
    public static synchronized CategoryHolder getInstance() {
        if (instance == null) instance = new CategoryHolder();
        return instance;
    }

    public ObservableList<Category> getAllCategories() {
        return categories;
    }

    public ObservableList<Category> getFilteredCategories(String sign) {
        return categories.filtered(category -> category.getCategorySign().equals(sign));
    }
    //endregion
}
