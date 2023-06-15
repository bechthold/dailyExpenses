package de.ibechthold.bidailyexpenses.logic.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DbUtils {
    //region constants
    //endregion

    //region attributes
    //endregion

    //region constructors
    //endregion

    //region methods
    public static List<String> getUniqueValuesFromColumn(Connection connection, String tableName, String columnName) {
        List<String> uniqueValue = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT DISTINCT " + columnName + " FROM " + tableName);
            while(resultSet.next()) {
                uniqueValue.add(resultSet.getString(columnName));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uniqueValue;
    }
    //endregion
}
