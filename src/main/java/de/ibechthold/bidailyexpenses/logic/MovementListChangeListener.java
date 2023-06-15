package de.ibechthold.bidailyexpenses.logic;

import de.ibechthold.bidailyexpenses.logic.db.DbManager;
import de.ibechthold.bidailyexpenses.model.Category;
import de.ibechthold.bidailyexpenses.model.Movement;
import javafx.collections.ListChangeListener;

/**
 * List change listener class for the list of {@link Movement}
 */
public class MovementListChangeListener implements ListChangeListener<Movement> {

    //region constants
    //endregion

    //region attributes
    //endregion

    //region constructors
    //endregion

    //region methods
    @Override
    public void onChanged(Change<? extends Movement> change) {
//        System.out.println(change);
        while (change.next()) {
            if (change.wasReplaced()) {
//                System.out.println("Changed by replace");
                Movement movementToUpdate = change.getAddedSubList().get(0);
                DbManager.getInstance().updateDataRecord(movementToUpdate);

            } else if (change.wasRemoved()) {
                Movement movementToRemove = change.getRemoved().get(0);
                DbManager.getInstance().deleteDataRecord(movementToRemove);
//                System.out.println("Listener: delete executed!");
            } else if (change.wasAdded()) {
                Movement movementToInsert = change.getAddedSubList().get(0);
//                System.out.println("Movement to insert: " + movementToInsert);
                DbManager.getInstance().insertDataRecord(movementToInsert);
            }
        }
    }
    //endregion
}
