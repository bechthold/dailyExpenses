package de.ibechthold.bidailyexpenses.logic.db;

import de.ibechthold.bidailyexpenses.Main;
import de.ibechthold.bidailyexpenses.model.Category;
import de.ibechthold.bidailyexpenses.model.Movement;
import de.ibechthold.bidailyexpenses.settings.AppTexts;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Data Access Object for object-{@link Movement}
 */
public class DaoMovement implements Dao<Movement> {
    //region constants
    //endregion

    //region attributes
    //endregion

    //region constructors
    //endregion

    //region methods
    @Override
    public void create(Connection dbConnection, Movement movement) {
        PreparedStatement statement = null;

        try {
            statement = dbConnection.prepareStatement("INSERT INTO movements (" +
                            "category, amount, sign, detail, date, date_idx, day, month, year, hour, minute) " +
                            "VALUES (?,?,?,?,?,?,?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, movement.getCategoryName());
            statement.setDouble(2, movement.getAmount());
            statement.setString(3, movement.getCategorySign());
            statement.setString(4, movement.getDetail());
            statement.setString(5, movement.getDate());

            statement.setString(6, movement.getDateIndex());
            statement.setInt(7, movement.getDay());
            statement.setInt(8, movement.getMonth());
            statement.setInt(9, movement.getYear());

            statement.setInt(10, movement.getHour());
            statement.setInt(11, movement.getMinute());

            statement.executeUpdate();
//            System.out.println("Inserted!" + movement.toString());
            ResultSet generatedKey = statement.getGeneratedKeys();

            if (generatedKey.next()) {
                int insertId = generatedKey.getInt(AppTexts.INSERT_ID);
                movement.setId(insertId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
                dbConnection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<Movement> readAll(Connection dbConnection) {
        List<Movement> movements = new ArrayList<>();

        Statement statement = null;

        try {
            statement = dbConnection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM movements");

            while (resultSet.next()) {
                Movement movement = new Movement(
                        resultSet.getDouble(AppTexts.MOVEMENT_COLUMN_AMOUNT),
                        resultSet.getString(AppTexts.MOVEMENT_COLUMN_CATEGORY),
                        resultSet.getString(AppTexts.MOVEMENT_COLUMN_SIGN),
                        resultSet.getString(AppTexts.MOVEMENT_COLUMN_DETAIL),
                        resultSet.getString(AppTexts.MOVEMENT_COLUMN_DATE),
                        resultSet.getInt(AppTexts.MOVEMENT_COLUMN_HOUR),
                        resultSet.getInt(AppTexts.MOVEMENT_COLUMN_MINUTE)
                );
                movement.setId(resultSet.getInt(AppTexts.MOVEMENT_COLUMN_ID));
                movement.setIcon(new ImageView(new Image(Objects.requireNonNull(Main.class.getResourceAsStream(AppTexts.ICONS_MONEY_MOUTH_FACE)))));
                movements.add(movement);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
                dbConnection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return movements;
    }

    @Override
    public void update(Connection dbConnection, Movement movement) {
        PreparedStatement statement = null;

        try {
            statement = dbConnection.prepareStatement("UPDATE movements SET amount=?, category=?, detail=?, date=? WHERE _id=?");
            statement.setDouble(1, movement.getAmount());
            statement.setString(2, movement.getCategoryName());
            statement.setString(3, movement.getDetail());
            statement.setString(4, movement.getDate());
            statement.setInt(5, movement.getId());

            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void delete(Connection dbConnection, Movement movement) {
        PreparedStatement statement = null;

        try {
            statement = dbConnection.prepareStatement("DELETE FROM movements WHERE _id = ?");
            statement.setInt(1, movement.getId());

            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
                dbConnection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public List<Movement> readAllFilterMovements(Connection dbConnection, List<String> filterList) {
        List<Movement> movements = new ArrayList<>();
        PreparedStatement statement = null;
        try {
            if (!filterList.isEmpty()) {

                statement = dbConnection.prepareStatement(
                        "SELECT * FROM movements " +
                                "WHERE " +
                                "(? IS NULL OR sign = ?) " +
                                "AND (? IS NULL OR category = ?) " +
                                "AND (? IS NULL OR month = ?) " +
                                "AND (? IS NULL OR year = ?)"
                );

                statement.setString(1, filterList.get(0));
                statement.setString(2, filterList.get(0));
                statement.setString(3, filterList.get(1));
                statement.setString(4, filterList.get(1));
                statement.setString(5, filterList.get(2));
                statement.setString(6, filterList.get(2));
                statement.setString(7, filterList.get(3));
                statement.setString(8, filterList.get(3));

                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    Movement movement = new Movement(
                            resultSet.getDouble(AppTexts.MOVEMENT_COLUMN_AMOUNT),
                            resultSet.getString(AppTexts.MOVEMENT_COLUMN_CATEGORY),
                            resultSet.getString(AppTexts.MOVEMENT_COLUMN_SIGN),
                            resultSet.getString(AppTexts.MOVEMENT_COLUMN_DETAIL),
                            resultSet.getString(AppTexts.MOVEMENT_COLUMN_DATE),
                            resultSet.getInt(AppTexts.MOVEMENT_COLUMN_HOUR),
                            resultSet.getInt(AppTexts.MOVEMENT_COLUMN_MINUTE)
                    );
                    movement.setId(resultSet.getInt(AppTexts.MOVEMENT_COLUMN_ID));

                    // movement.setIcon(new ImageView(new Image(Main.class.getResourceAsStream(resultSet.getString("icon")))));

                    movements.add(movement);
                }
            } else {
                return readAll(dbConnection);
            }
            } catch(Exception e){
                e.printStackTrace();
            } finally{
                try {
                    if (statement != null) statement.close();
                    dbConnection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
//        System.out.println(movements);
        return movements;
    }
}
