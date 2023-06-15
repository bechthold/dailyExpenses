package de.ibechthold.bidailyexpenses.gui.tableview;

import de.ibechthold.bidailyexpenses.model.Category;
import de.ibechthold.bidailyexpenses.settings.AppStyles;
import javafx.scene.control.TableCell;

/**
 * The class is designed to change the style in the TableView table column for the {@link Category} class.
 */
public class CategoryCell extends TableCell<Category, String> {

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (item == null || empty) {
            setText(null);
        } else {
            setText(item);
            setStyle(AppStyles.FX_FONT_SIZE);
        }
    }
    //region constants
    //endregion

    //region attributes
    //endregion

    //region constructors
    //endregion

    //region methods
    //endregion
}
