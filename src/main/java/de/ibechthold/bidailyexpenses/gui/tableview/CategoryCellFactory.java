package de.ibechthold.bidailyexpenses.gui.tableview;

import de.ibechthold.bidailyexpenses.model.Category;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 * This class is a factory cell of the {@link Category} class
 */
public class CategoryCellFactory implements Callback<TableColumn<Category, String>, TableCell<Category, String>> {

    @Override
    public TableCell<Category, String> call(TableColumn<Category, String> column) {
        return new CategoryCell();
    }
}