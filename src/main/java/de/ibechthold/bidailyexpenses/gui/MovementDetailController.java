package de.ibechthold.bidailyexpenses.gui;

import de.ibechthold.bidailyexpenses.logic.CategoryHolder;
import de.ibechthold.bidailyexpenses.logic.MovementHolder;
import de.ibechthold.bidailyexpenses.model.Category;
import de.ibechthold.bidailyexpenses.model.Movement;
import de.ibechthold.bidailyexpenses.settings.AppTexts;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * This class implements the control logic for the scene
 * used for editing an object {@link Movement}
 */
public class MovementDetailController {
    //region constants
    //endregion

    //region attributes
    //region fxml-attributes
    @FXML
    private TextField txtAmount;

    @FXML
    private ComboBox<Category> comboBoxMovementCategory;

    @FXML
    private TextField txtDescription;

    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<String> comboBoxHours;
    @FXML
    private ComboBox<String> comboBoxMinutes;
    //endregion
    private Movement selectedMovement;
    //endregion

    //region constructors
    //endregion

    //region methods

    public void setSelectedMovement(Movement movement) {
        selectedMovement = movement;
        initializeComboBoxCategory();
        initializeComboBoxHours();
        initializeComboBoxMinutes();

        if (selectedMovement != null) {
            txtAmount.setText(String.valueOf(selectedMovement.getAmount()));
            comboBoxMovementCategory.setValue(findCategoryByName());
            txtDescription.setText((selectedMovement.getDetail()));
            datePicker.setValue(LocalDate.parse(selectedMovement.getDate()));
            comboBoxHours.setValue(String.valueOf(selectedMovement.getHour()));
            comboBoxMinutes.setValue(String.valueOf(selectedMovement.getMinute()));
        }
    }

    private Category findCategoryByName() {
        for (Category category : comboBoxMovementCategory.getItems()) {
            if (category.getCategoryName().equals(selectedMovement.getCategoryName())) {
                return category;
            }
        }
        return null;
    }

    private void initializeComboBoxCategory() {
        toggleComboBoxMovementCategory();

        comboBoxMovementCategory.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Category> call(ListView<Category> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Category item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item != null) {
                            setText(item.getCategoryName());
                        } else {
                            setText(null);
                        }
                    }
                };
            }
        });

        comboBoxMovementCategory.setConverter(new StringConverter<>() {
            @Override
            public String toString(Category category) {
                return category != null ? category.getCategoryName() : AppTexts.VOID_STRING;
            }

            @Override
            public Category fromString(String string) {
                return null;
            }
        });

        comboBoxMovementCategory.setOnAction(event -> {

        });
    }

    private void toggleComboBoxMovementCategory() {
        if (Objects.equals(selectedMovement.getCategorySign(), AppTexts.SIGN_MINUS)) {
            comboBoxMovementCategory.getItems().addAll(FXCollections.observableArrayList(
                    CategoryHolder.getInstance().getFilteredCategories(AppTexts.SIGN_MINUS)));
        } else if (Objects.equals(selectedMovement.getCategorySign(), AppTexts.SIGN_PLUS)) {
            comboBoxMovementCategory.getItems().addAll(FXCollections.observableArrayList(
                    CategoryHolder.getInstance().getFilteredCategories(AppTexts.SIGN_PLUS)));
        }
    }

    private void initializeComboBoxHours() {
        IntStream.rangeClosed(0, 23)
                .mapToObj(String::valueOf)
                .forEach(comboBoxHours.getItems()::add);

        comboBoxHours.setValue(String.valueOf(LocalTime.now().getHour()));

        comboBoxHours.setOnAction(event -> comboBoxHours.getSelectionModel().getSelectedItem());
    }

    private void initializeComboBoxMinutes() {
        IntStream.rangeClosed(0, 59)
                .mapToObj(String::valueOf)
                .forEach(comboBoxMinutes.getItems()::add);

        comboBoxMinutes.setValue(String.valueOf(LocalTime.now().getMinute()));


        comboBoxMinutes.setOnAction(event -> comboBoxMinutes.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void saveMovement() {
        try {
            editSelectedCategory();
            returnToOverviewScene();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(AppTexts.ERROR_WHEN_SAVING);
            alert.setHeaderText(AppTexts.CANNOT_BE_CONVERTED_TO_NUMBERS);
            alert.setContentText(String.format(AppTexts.CANNOT_BE_CONVERTED, txtAmount.getText()));
            alert.showAndWait();
        }
    }

    private void editSelectedCategory() {
        if (fieldIsNotEqualBlankOrEmpty(txtAmount, String.valueOf(selectedMovement.getAmount()))) {
            selectedMovement.setAmount(Double.parseDouble(txtAmount.getText()));
        }

        if (fieldIsNotEqualBlankOrEmpty(txtDescription, String.valueOf(selectedMovement.getDetail()))) {
            selectedMovement.setDetail(txtDescription.getText());
        }

        selectedMovement.setCategoryName(comboBoxMovementCategory.getValue().getCategoryName());
        selectedMovement.setCategorySign(comboBoxMovementCategory.getValue().getCategorySign());

        selectedMovement.setDate(String.valueOf(datePicker.getValue()));
        selectedMovement.setHour(Integer.parseInt(comboBoxHours.getValue()));
        selectedMovement.setMinute(Integer.parseInt(comboBoxMinutes.getValue()));

        selectedMovement.createAndSetDateIndexAndDate(
                String.valueOf(datePicker.getValue()),
                Integer.parseInt(comboBoxHours.getValue()),
                Integer.parseInt(comboBoxMinutes.getValue()));

//        selectedMovement.setIcon(new ImageView(new Image(Main.class.getResourceAsStream("icons/money_mouth_face"))));

        MovementHolder.getInstance().getFilteredMovements().set(
                MovementHolder.getInstance().getFilteredMovements().indexOf(selectedMovement), selectedMovement
        );

    }

    private boolean fieldIsNotEqualBlankOrEmpty(TextField textField, String property) {
        return !textField.getText().equals(property) && !textField.getText().isEmpty() && !textField.getText().isBlank();
    }

    @FXML
    private void deleteMovement() {
        if (selectedMovement != null) {
            Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION);
            deleteAlert.setTitle(AppTexts.DELETE_MOVEMENT);
            deleteAlert.setHeaderText(AppTexts.DO_YOU_WANT_TO_DELETE_A_MOVEMENT);
            deleteAlert.setContentText(selectedMovement.toString());
            Optional<ButtonType> optional = deleteAlert.showAndWait();

            if (optional.get() == ButtonType.OK) {
                //               System.out.println("It must be deleted! Selected Movement: " + selectedMovement);
                MovementHolder.getInstance().getFilteredMovements().remove(selectedMovement);
                returnToOverviewScene();
            }
        }
    }

    private void returnToOverviewScene() {
        SceneManager.getInstance().switchToOverviewScene();
    }
    //endregion
}
