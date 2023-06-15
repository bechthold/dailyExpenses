package de.ibechthold.bidailyexpenses.gui;

import de.ibechthold.bidailyexpenses.logic.CategoryHolder;
import de.ibechthold.bidailyexpenses.model.Category;
import de.ibechthold.bidailyexpenses.settings.AppTexts;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.util.Optional;

/**
 * This class implements the control logic for the scene used for editing the {@link Category}.
 */
public class CategoryDetailController {
    //region constants
    //endregion

    //region attributes
    @FXML
    private TextField txtExpenseCategory;

    @FXML
    private TextField txtExpenseIcon;

    private Category selectedCategory;

    private String categorySign;
    //endregion

    //region constructors
    //endregion

    //region methods
    public void setSelectedCategory(Category category, String sign) {
        selectedCategory = category;
        categorySign = sign;

        if (selectedCategory != null) {
            txtExpenseIcon.setText(selectedCategory.getCategoryIcon());
            txtExpenseCategory.setText(selectedCategory.getCategoryName());
        }
    }

    @FXML
    private void saveCategory() {
        if (selectedCategory != null) {
            editSelectedCategory();
        } else {
            createNewCategory(categorySign);
        }
        returnToOverviewScene();
    }

    private void editSelectedCategory() {
        if (fieldIsNotEqualBlankOrEmpty(txtExpenseCategory, selectedCategory.getCategoryName())) {
            selectedCategory.setCategoryName(txtExpenseCategory.getText());
        }

        if (fieldIsNotEqualBlankOrEmpty(txtExpenseIcon, selectedCategory.getCategoryIcon())) {
            selectedCategory.setCategoryIcon(txtExpenseIcon.getText());
        }

        CategoryHolder.getInstance().getAllCategories().set(
                CategoryHolder.getInstance().getAllCategories().indexOf(selectedCategory), selectedCategory
        );
    }

    private boolean fieldIsNotEqualBlankOrEmpty(TextField textField, String property) {
        return !textField.getText().equals(property) && !textField.getText().isEmpty() && !textField.getText().isBlank();
    }

    private void createNewCategory(String sign) {
        Category category = new Category(
                txtExpenseCategory.getText(),
                sign,
                txtExpenseIcon.getText()
        );
        CategoryHolder.getInstance().getAllCategories().add(category);
    }

    @FXML
    private void deleteCategory() {
        if (selectedCategory != null) {
            Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION);
            deleteAlert.setTitle(AppTexts.DELETE_CATEGORY);
            deleteAlert.setHeaderText(AppTexts.DO_YOU_WANT_TO_DELETE_CATEGORY);
            deleteAlert.setContentText(selectedCategory.toString());
            Optional<ButtonType> optional = deleteAlert.showAndWait();

            if (optional.isPresent() && optional.get() == ButtonType.OK) {
                CategoryHolder.getInstance().getAllCategories().remove(selectedCategory);
                returnToOverviewScene();
            }
        }
    }

    private void returnToOverviewScene() {
        SceneManager.getInstance().switchToOverviewScene();
    }
    //endregion
}
