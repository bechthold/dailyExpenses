package de.ibechthold.bidailyexpenses.logic.db;

import de.ibechthold.bidailyexpenses.model.Category;
import de.ibechthold.bidailyexpenses.settings.AppTexts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for object-{@link Category}
 */
public class DaoCategory  implements Dao<Category>{
    //region constants
    //endregion

    //region attributes
    //endregion

    //region constructors
    //endregion

    //region methods
    @Override
    public void create(Connection dbConnection, Category category) {
        PreparedStatement statement = null;

        try {
            statement = dbConnection.prepareStatement("INSERT INTO categories (name, sign, icon) VALUES (?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, category.getCategoryName());
            statement.setString(2, category.getCategorySign());
            statement.setString(3, category.getCategoryIcon());

            statement.executeUpdate();
            ResultSet generatedKey = statement.getGeneratedKeys();

            if(generatedKey.next()) {
                int insertId = generatedKey.getInt(AppTexts.INSERT_ID);
                category.setId(insertId);
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
    public List<Category> readAll(Connection dbConnection) {
        List<Category> categories = new ArrayList<>();

        Statement statement = null;

        try {
            statement = dbConnection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM categories");

            while(resultSet.next()) {
                Category category = new Category(
                        resultSet.getString(AppTexts.CATEGORY_COLUMN_NAME),
                        resultSet.getString(AppTexts.CATEGORY_COLUMN_SIGN),
                        resultSet.getString(AppTexts.CATEGORY_COLUMN_ICON)
                );
                category.setId(resultSet.getInt(AppTexts.CATEGORY_COLUMN_ID));
                categories.add(category);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(statement != null) statement.close();
                dbConnection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return categories;
    }

    @Override
    public void update(Connection dbConnection, Category category) {
        PreparedStatement statement = null;

        try {
            statement = dbConnection.prepareStatement("UPDATE categories SET name=?, sign=?, icon=? WHERE _id=?");
            statement.setString(1, category.getCategoryName());
            statement.setString(2, category.getCategorySign());
            statement.setString(3, category.getCategoryIcon());
            statement.setInt(4, category.getId());

            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(statement != null) statement.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void delete(Connection dbConnection, Category category) {
        PreparedStatement statement = null;

        try {
            statement = dbConnection.prepareStatement("DELETE FROM categories WHERE _id = ?");
            statement.setInt(1, category.getId());

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
    //endregion
}
