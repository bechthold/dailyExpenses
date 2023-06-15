package de.ibechthold.bidailyexpenses.logic.db;

import java.sql.Connection;
import java.util.List;

/**
 * Interface that provides the CRUD functionality for a model class provides.
 * Used to access object data in the database
 */
public interface Dao<T> {
    void create(Connection dbConnection, T object);
    List<T> readAll(Connection dbConnection);
    void update(Connection dbConnection, T object);
    void delete(Connection dbConnection, T object);

}
