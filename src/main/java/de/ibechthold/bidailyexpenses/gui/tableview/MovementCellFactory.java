package de.ibechthold.bidailyexpenses.gui.tableview;

import de.ibechthold.bidailyexpenses.model.Category;
import de.ibechthold.bidailyexpenses.model.Movement;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 * This class is a factory cell of the {@link Movement} class
 */
public class MovementCellFactory implements Callback<TableColumn<Movement, Object>, TableCell<Movement, Object>> {

    @Override
    public TableCell<Movement, Object> call(TableColumn<Movement, Object> column) {
        return new MovementCell();
    }
}