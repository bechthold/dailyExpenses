package de.ibechthold.bidailyexpenses.logic.db;

import de.ibechthold.bidailyexpenses.model.Category;
import de.ibechthold.bidailyexpenses.model.Movement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

/**
 * Establishes connections to databases
 * and has access to the respective DAOs of all the model classes of the project.
 */
public class DbManager {
    //region constants
    public static final String URL_PREFIX = "jdbc:mariadb://";
    public static final String SERVER_IP = "localhost";
    public static final String DB_NAME = "/daily_expenses";

    public static final String URL = URL_PREFIX + SERVER_IP + DB_NAME;

    public static final String USERNAME = "ibechthold";
    public static final String PASSWORD = "1234";
    //endregion

    //region attributes
    private static DbManager instance;
    private static DaoCategory daoCategory;
    private static DaoMovement daoMovement;
    //endregion

    //region constructors
    private DbManager() {
        daoCategory = new DaoCategory();
        daoMovement = new DaoMovement();
    }
    //endregion

    //region methods
    public static synchronized DbManager getInstance() {
        if (instance == null) instance = new DbManager();
        return instance;
    }

    /**
     * Establishes and returns a connection to the database
     *
     * @return {@link Connection} : connection to the database
     */
    private Connection getConnection() {
        Connection connection;
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    public void insertDataRecord(Object object) {
        if (object instanceof Category category) {
            daoCategory.create(getConnection(), category);
        } else if (object instanceof  Movement movement) {
            daoMovement.create(getConnection(), movement);
        }
    }

    public List<Category> readAllCategories() {
        return daoCategory.readAll(getConnection());
    }

    public List<Movement> readAllMovements() {
        return daoMovement.readAll(getConnection());
    }

    public void updateDataRecord(Object object) {
        if (object instanceof Category category) {
            daoCategory.update(getConnection(), category);
        } else if (object instanceof Movement movement) {
            daoMovement.update(getConnection(), movement);
        }
    }

    public void deleteDataRecord(Object object) {
        if (object instanceof Category category) {
            daoCategory.delete(getConnection(), category);
        } else if (object instanceof Movement movement) {
            daoMovement.delete(getConnection(), movement);
        }
    }

    public List<String> getUniqueYears() {
        return DbUtils.getUniqueValuesFromColumn(getConnection(), "movements", "year");
    }

    public List<Movement> readAllFilteredMovements(List<String> filterList) {
        return daoMovement.readAllFilterMovements(getConnection(), filterList);
    }
    //endregion
}
