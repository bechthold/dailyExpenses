package de.ibechthold.bidailyexpenses.gui.tableview;

import de.ibechthold.bidailyexpenses.model.Movement;
import de.ibechthold.bidailyexpenses.settings.AppStyles;
import javafx.scene.control.TableCell;

/**
 * The class is designed to change the style in the TableView table column for the {@link Movement} class.
 */
public class MovementCell extends TableCell<Movement, Object> {

    @Override
    protected void updateItem(Object item, boolean empty) {
        super.updateItem(item, empty);

        if (item == null || empty) {
            setText(null);
        } else {
            if (item instanceof Double) {
                setStyle(AppStyles.FX_FONT_SIZE + AppStyles.FX_ALIGNMENT_CENTER_RIGHT);
                setText(item.toString());
            } else {
                setText(item.toString());
                setStyle(AppStyles.FX_FONT_SIZE);
            }
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
