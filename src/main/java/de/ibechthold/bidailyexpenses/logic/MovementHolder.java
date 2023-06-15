package de.ibechthold.bidailyexpenses.logic;

import de.ibechthold.bidailyexpenses.logic.db.DbManager;
import de.ibechthold.bidailyexpenses.model.Movement;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

/**
 * Implemented as a singleton and makes movements available as an ObservableList.
 */
public class MovementHolder {
    //region constants
    //endregion

    //region attributes
    private static MovementHolder instance;
    private List<Movement> normalMovementList;


    public ObservableList<Movement> filteredMovements;

    //endregion

    //region constructors
    private MovementHolder() {
        normalMovementList = DbManager.getInstance().readAllMovements();

        filteredMovements = FXCollections.observableArrayList(movement -> new Observable[]{
                movement.amountProperty(),
                movement.categoryNameProperty(),
                movement.categorySignProperty(),
                movement.detailProperty(),
                movement.dateProperty(),
                movement.dayProperty(),
                movement.monthProperty(),
                movement.yearProperty(),
                movement.dateIndexProperty(),
                movement.hourProperty(),
                movement.minuteProperty(),
        });
        filteredMovements.addAll(normalMovementList);
        filteredMovements.addListener(new MovementListChangeListener());

        filteredMovements = FXCollections.observableArrayList(
                DbManager.getInstance().readAllFilteredMovements(new ArrayList<>()));
        filteredMovements.addListener(new MovementListChangeListener());
    }
    //endregion

    //region methods
    public static synchronized MovementHolder getInstance() {
        if (instance == null) instance = new MovementHolder();
        return instance;
    }


    public ObservableList<Movement> getFilteredMovements() {
        return filteredMovements;
    }

    public void setFilteredMovements(ObservableList<Movement> filteredMovements) {
        this.filteredMovements = filteredMovements;
        filteredMovements.addListener(new MovementListChangeListener());
    }


    //endregion
}
