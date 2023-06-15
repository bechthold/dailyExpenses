package de.ibechthold.bidailyexpenses.logic;

import de.ibechthold.bidailyexpenses.logic.db.DbManager;
import de.ibechthold.bidailyexpenses.model.Category;
import javafx.collections.ListChangeListener;

/**
 * List change listener class for the list of {@link Category}
 */
public class CategoryListChangeListener implements ListChangeListener<Category> {

    //region constants
    //endregion

    //region attributes
    //endregion

    //region constructors
    //endregion

    //region methods
    @Override
    public void onChanged(Change<? extends Category> change) {
        while (change.next()) {
            if (change.wasReplaced()) {
//                System.out.println("Changed by replace");
                Category categoryToUpdate = change.getAddedSubList().get(0);
                DbManager.getInstance().updateDataRecord(categoryToUpdate);

            } else if (change.wasRemoved()) {
                Category categoryToRemove = change.getRemoved().get(0);
                DbManager.getInstance().deleteDataRecord(categoryToRemove);

            } else if (change.wasAdded()) {
                Category categoryToInsert = change.getAddedSubList().get(0);
                DbManager.getInstance().insertDataRecord(categoryToInsert);
            }
        }
    }
    //endregion

}
