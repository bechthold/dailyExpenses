package de.ibechthold.bidailyexpenses.gui;

import de.ibechthold.bidailyexpenses.Main;
import de.ibechthold.bidailyexpenses.gui.tableview.CategoryCellFactory;
import de.ibechthold.bidailyexpenses.gui.tableview.MovementCellFactory;
import de.ibechthold.bidailyexpenses.logic.CategoryHolder;
import de.ibechthold.bidailyexpenses.logic.MovementHolder;
import de.ibechthold.bidailyexpenses.logic.db.DbManager;
import de.ibechthold.bidailyexpenses.model.Category;
import de.ibechthold.bidailyexpenses.model.MonthEnum;
import de.ibechthold.bidailyexpenses.model.Movement;
import de.ibechthold.bidailyexpenses.settings.AppTexts;
import de.ibechthold.bidailyexpenses.settings.AppFiles;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.IntStream;

/**
 * This class implements the control logic for the main scene
 * used for displaying a table with objects {@link Movement}
 */
public class MainController implements Initializable {

    //constants
    //endregion

    //region attributes
    private String searchString = AppTexts.VOID_STRING;
    private boolean movementSortToggele = true;

    private Movement selectedMovement = null;


    //region fxml-elements
    @FXML
    private PieChart expensePieChart;

    @FXML
    private Label lblMonthIncome;

    @FXML
    private Label lblPrevBalance;

    @FXML
    private Label lblMonthExpense;

    @FXML
    private Label lblCurrentBalance;

    @FXML
    private Label homePageHeader;

    @FXML
    private Button btnMovementCategory;
    @FXML
    private TextField txtSearch;
    @FXML
    private TextField txtTotal;
    @FXML
    private Label labelTotal;
    @FXML
    private GridPane paneExpenseCategory;
    @FXML
    private TableView<Category> tableExpenseCategories;
    @FXML
    private TableColumn<Category, String> columnExpenseIcon;
    @FXML
    private TableColumn<Category, String> columnExpenseCategory;
    @FXML
    private TableColumn<Category, String> columnExpenseSign;

    @FXML
    private GridPane paneIncomeCategory;
    @FXML
    private TableView<Category> tableIncomeCategories;
    @FXML
    private TableColumn<Category, String> columnIncomeIcon;
    @FXML
    private TableColumn<Category, String> columnIncomeCategory;
    @FXML
    private TableColumn<Category, String> columnIncomeSign;

    @FXML
    private TableView<Movement> tableMovements;
    @FXML
    private TableColumn<Movement, Object> columnMovementSign;
    @FXML
    private TableColumn<Movement, Object> columnMovementAmount;
    @FXML
    private TableColumn<Movement, Object> columnMovementCategory;
    @FXML
    private TableColumn<Movement, Object> columnMovementDetail;
    @FXML
    private TableColumn<Movement, Object> columnMovementDate;

    @FXML
    private TableColumn<Movement, ImageView> columnMovementIcon;

    @FXML
    private GridPane paneHome;

    @FXML
    private GridPane paneAddMovement;
    @FXML
    private GridPane paneMovements;

    @FXML
    private ComboBox<Category> comboBoxCategory;

    @FXML
    private ToggleButton buttonSort;

    @FXML
    private ToggleButton btnPlus;
    @FXML
    private ToggleButton btnMinus;
    @FXML
    private ToggleButton btnPlusAndMinus;

    private final ToggleGroup toggleGroup = new ToggleGroup();
    @FXML
    private ComboBox<MonthEnum> comboBoxMonth;
    @FXML
    private ComboBox<String> comboBoxYear;

    @FXML
    private Label lblAmount;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<String> comboBoxHours;
    @FXML
    private ComboBox<String> comboBoxMinutes;

    @FXML
    private TextField txtAmount;
    @FXML
    private ComboBox<Category> comboBoxMovementCategory;

    @FXML
    private TextField txtDescription;

    //endregion
    //endregion

    //region constructors
    //endregion

    //region methods
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        paneMovements.toFront();
        paneHome.toFront();

        initializeComboBoxCategory();
        initializeComboBoxMonth();
        initializeComboBoxYear();

        initializeExpenseCategoryTableView();
        initializeIncomeCategoryTableView();
        initializeMovementsTableView();

        initializeSortButton();
        getSearchInDetailsFilter();

        initializeHomePage();

        tableExpenseCategories.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {
                Category selectedCategory = tableExpenseCategories.getSelectionModel().getSelectedItem();
                openCategoryDetailScene(selectedCategory);
            }
        });

        tableIncomeCategories.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {
                Category selectedCategory = tableIncomeCategories.getSelectionModel().getSelectedItem();
                openCategoryDetailScene(selectedCategory);
            }
        });

        tableMovements.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {
                Movement selectedMovement = tableMovements.getSelectionModel().getSelectedItem();
                openMovementDetailScene(selectedMovement);
            }
        });
    }

    private void initializeHomePage() {
        initializePieChart();
    }

    private void initializePieChart() {
        expensePieChart.getData().clear();
        Map<String, Double> pieChartData = makeFilterListForChart();
        for (Map.Entry<String, Double> entry : pieChartData.entrySet()) {
            PieChart.Data data = new PieChart.Data(entry.getKey(), entry.getValue());
            expensePieChart.getData().add(data);
        }
    }

    private Map<String, Double> makeFilterListForChart() {
        Map<String, Double> expenseTotals = new HashMap<>();
        String sign = AppTexts.SIGN_MINUS;
        String month = String.valueOf(LocalDate.now().getMonthValue());
        String year = String.valueOf(LocalDate.now().getYear());

        for (Category category : CategoryHolder.getInstance().getFilteredCategories(AppTexts.SIGN_MINUS)) {
            List<String> tmpList = new ArrayList<>();
            tmpList.add(sign);
            tmpList.add(category.getCategoryName());
            tmpList.add(month);
            tmpList.add(year);
            rebuildFilteredMovements(tmpList);
            expenseTotals.put(category.getCategoryName(), getTotal());
        }
        System.out.println(expenseTotals);
        return expenseTotals;
    }

    private void initializeSortButton() {
        sortMovementsByDate();
        buttonSort.setSelected(true);

        Image pressedImage = new Image(Objects.requireNonNull(
                Main.class.getResourceAsStream(AppFiles.ICONS_PRESSED)));
        Image releasedImage = new Image(
                Objects.requireNonNull(Main.class.getResourceAsStream(AppFiles.ICONS_RELEASED)));

        buttonSort.setGraphic(new ImageView(releasedImage));

        buttonSort.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                buttonSort.setGraphic(new ImageView(pressedImage));
            } else {
                buttonSort.setGraphic(new ImageView(releasedImage));
            }
        });

    }

    private void initializeComboBoxYear() {
        comboBoxYear.getItems().add(0, null);
        comboBoxYear.getItems().addAll(FXCollections.observableArrayList(DbManager.getInstance().getUniqueYears()));


        comboBoxYear.setCellFactory(new Callback<>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(item);
                    }
                };
            }
        });

        comboBoxYear.setConverter(new StringConverter<>() {
            @Override
            public String toString(String year) {
                return year != null ? year : AppTexts.ALL;
            }

            @Override
            public String fromString(String string) {
                return null;
            }
        });

        comboBoxYear.setOnAction(event -> selectBySearchString(searchString));
    }

    private void initializeComboBoxCategory() {
        toggleComboBoxCategory();

        comboBoxCategory.setCellFactory(new Callback<>() {
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

        comboBoxCategory.setConverter(new StringConverter<>() {
            @Override
            public String toString(Category category) {
                return category != null ? category.getCategoryName() : AppTexts.ALL;
            }

            @Override
            public Category fromString(String string) {
                return null;
            }
        });

        comboBoxCategory.setOnAction(event -> selectBySearchString(searchString));
    }

    private void toggleComboBoxCategory() {

        toggleGroup.getToggles().addAll(btnMinus, btnPlus, btnPlusAndMinus);
        toggleGroup.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
            comboBoxCategory.getItems().clear();
            comboBoxCategory.getItems().add(0, null);

            if (newToggle == null) {
                comboBoxCategory.setDisable(true);
            } else if (newToggle == btnMinus) {
                comboBoxCategory.setDisable(false);
                comboBoxCategory.getItems().addAll(FXCollections.observableArrayList(
                        CategoryHolder.getInstance().getFilteredCategories(AppTexts.SIGN_MINUS)));
            } else if (newToggle == btnPlus) {
                comboBoxCategory.setDisable(false);
                comboBoxCategory.getItems().addAll(FXCollections.observableArrayList(
                        CategoryHolder.getInstance().getFilteredCategories(AppTexts.SIGN_PLUS)));
            } else {
                comboBoxCategory.setDisable(true);
                comboBoxCategory.getItems().addAll(FXCollections.observableArrayList(
                        CategoryHolder.getInstance().getAllCategories()));
            }
        });
        comboBoxCategory.getSelectionModel().selectFirst();
        toggleGroup.selectToggle(btnPlusAndMinus);
    }

    private void initializeComboBoxMonth() {
        comboBoxMonth.getItems().add(0, null);
        comboBoxMonth.getItems().addAll(MonthEnum.values());

        comboBoxMonth.setCellFactory(new Callback<>() {
            @Override
            public ListCell<MonthEnum> call(ListView<MonthEnum> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(MonthEnum item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item != null) {
                            setText(item.getMonthName());
                        } else {
                            setText(null);
                        }
                    }
                };
            }
        });

        comboBoxMonth.setConverter(new StringConverter<>() {
            @Override
            public String toString(MonthEnum monthEnum) {
                return monthEnum != null ? monthEnum.getMonthName() : AppTexts.ALL;
            }

            @Override
            public MonthEnum fromString(String string) {
                return null;
            }
        });

        comboBoxMonth.setOnAction(event -> selectBySearchString(searchString));
    }

    private void initializeIncomeCategoryTableView() {
        CategoryCellFactory categoryCellFactory = new CategoryCellFactory();

        columnIncomeIcon.setCellValueFactory(new PropertyValueFactory<>(AppTexts.CATEGORY_ICON));
        columnIncomeCategory.setCellValueFactory(new PropertyValueFactory<>(AppTexts.CATEGORY_NAME));
        columnIncomeSign.setCellValueFactory(new PropertyValueFactory<>(AppTexts.CATEGORY_SIGN));

        columnIncomeIcon.setCellFactory(categoryCellFactory);
        columnIncomeCategory.setCellFactory(categoryCellFactory);
        columnIncomeSign.setCellFactory(categoryCellFactory);

        tableIncomeCategories.setItems(CategoryHolder.getInstance().getFilteredCategories(AppTexts.SIGN_PLUS));
    }

    private void initializeExpenseCategoryTableView() {
        CategoryCellFactory categoryCellFactory = new CategoryCellFactory();

        columnExpenseIcon.setCellValueFactory(new PropertyValueFactory<>(AppTexts.CATEGORY_ICON));
        columnExpenseCategory.setCellValueFactory(new PropertyValueFactory<>(AppTexts.CATEGORY_NAME));
        columnExpenseSign.setCellValueFactory(new PropertyValueFactory<>(AppTexts.CATEGORY_SIGN));

        columnExpenseIcon.setCellFactory(categoryCellFactory);
        columnExpenseCategory.setCellFactory(categoryCellFactory);
        columnExpenseSign.setCellFactory(categoryCellFactory);

        tableExpenseCategories.setItems(CategoryHolder.getInstance().getFilteredCategories(AppTexts.SIGN_MINUS));
    }

    private void initializeMovementsTableView() {
        MovementCellFactory movementCellFactory = new MovementCellFactory();

        columnMovementIcon.setCellValueFactory(new PropertyValueFactory<>(AppTexts.ICON));
        columnMovementSign.setCellValueFactory(new PropertyValueFactory<>(AppTexts.CATEGORY_SIGN));
        columnMovementAmount.setCellValueFactory(new PropertyValueFactory<>(AppTexts.AMOUNT));
        columnMovementCategory.setCellValueFactory(new PropertyValueFactory<>(AppTexts.CATEGORY_NAME));
        columnMovementDetail.setCellValueFactory(new PropertyValueFactory<>(AppTexts.DETAIL));

        columnMovementDate.setCellValueFactory(new PropertyValueFactory<>(AppTexts.DATE));

        columnMovementSign.setCellFactory(movementCellFactory);
        columnMovementAmount.setCellFactory(movementCellFactory);
        columnMovementCategory.setCellFactory(movementCellFactory);
        columnMovementDetail.setCellFactory(movementCellFactory);
        columnMovementDate.setCellFactory(movementCellFactory);

        rebuildFilteredMovements(getFilterList());
        tableMovements.setItems(MovementHolder.getInstance().getFilteredMovements());
        setTotal();
    }

    /**
     * It applies the current filters to the collection of objects contained in the {@link MovementHolder} class
     */
    public void rebuildFilteredMovements(List<String> filterList) {
        MovementHolder.getInstance().setFilteredMovements(
                FXCollections.observableArrayList(DbManager.getInstance().readAllFilteredMovements(filterList)));
    }

    @FXML
    private void openCategoryDetailScene(ActionEvent actionEvent) {
        if (actionEvent.getSource() instanceof Button button) {
            if (Objects.equals(button.getId(), AppTexts.BTN_CREATE_EXPENSE_CATEGORY)) {
                SceneManager.getInstance().switchToCategoryDetailScene(null, AppTexts.SIGN_MINUS);
            } else if (Objects.equals(button.getId(), AppTexts.BTN_CREATE_INCOME_CATEGORY)) {
                SceneManager.getInstance().switchToCategoryDetailScene(null, AppTexts.SIGN_PLUS);
            }
        }
    }

    private void openMovementDetailScene(Movement selectedMovement) {
        SceneManager.getInstance().switchToMovementDetailScene(selectedMovement);
    }

    private void openCategoryDetailScene(Category selectedCategory) {
        SceneManager.getInstance().switchToCategoryDetailScene(selectedCategory, null);
    }

    @FXML
    protected void changeScene(ActionEvent actionEvent) {

        if (actionEvent.getSource() instanceof Button button) {
            if (Objects.equals(button.getId(), AppTexts.BTN_ADD_INCOME)) {
                initializeAddMovementPane(AppTexts.BTN_ADD_INCOME);
                paneAddMovement.toFront();
                clearAllMovementFields();
            } else if (Objects.equals(button.getId(), AppTexts.BTN_ADD_EXPENSE)) {
                initializeAddMovementPane(AppTexts.BTN_ADD_EXPENSE);
                paneAddMovement.toFront();
                clearAllMovementFields();
            } else if (Objects.equals(button.getId(), AppTexts.BTN_MOVEMENTS)) {
                paneMovements.toFront();
            } else if (Objects.equals(button.getId(), AppTexts.BTN_HOME)) {
                paneHome.toFront();
                initializePieChart();
                //paneMovements.toFront();
            } else if (Objects.equals(button.getText(), AppTexts.EXPENSE_CATEGORIES)) {
                paneExpenseCategory.toFront();
            } else if (Objects.equals(button.getText(), AppTexts.INCOME_CATEGORIES)) {
                paneIncomeCategory.toFront();
            } else if (Objects.equals((button.getId()), AppTexts.TO_ADD_EXPENSE)) {
                initializeAddMovementPane(AppTexts.BTN_ADD_EXPENSE);
                paneAddMovement.toFront();
            } else if (Objects.equals((button.getId()), AppTexts.TO_ADD_INCOME)) {
                initializeAddMovementPane(AppTexts.BTN_ADD_INCOME);
                paneAddMovement.toFront();
            }
        }
    }

    private void initializeAddMovementPane(String actionButton) {
        initializeLblAmount(actionButton);
        initializeComboBoxMovementCategory(actionButton);
        initializeDatePicker();
        initializeComboBoxHours();
        initializeComboBoxMinutes();
        initializeButtonMovementCategory(actionButton);
    }

    private void initializeButtonMovementCategory(String actionButton) {
        if (actionButton.equals(AppTexts.BTN_ADD_EXPENSE)) {
            btnMovementCategory.setText(AppTexts.EXPENSE_CATEGORIES);
        } else if (actionButton.equals(AppTexts.BTN_ADD_INCOME)) {
            btnMovementCategory.setText(AppTexts.INCOME_CATEGORIES);
        }
    }

    private void initializeDatePicker() {
        datePicker.setValue(LocalDate.now());
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

    private void initializeComboBoxMovementCategory(String actionButton) {
        comboBoxMovementCategory.getItems().clear();
        if (actionButton.equals(AppTexts.BTN_ADD_EXPENSE)) {
            comboBoxMovementCategory.getItems().addAll(FXCollections.observableArrayList(CategoryHolder.getInstance().getFilteredCategories(AppTexts.SIGN_MINUS)));
        } else if (actionButton.equals(AppTexts.BTN_ADD_INCOME)) {
            comboBoxMovementCategory.getItems().addAll(FXCollections.observableArrayList(CategoryHolder.getInstance().getFilteredCategories(AppTexts.SIGN_PLUS)));
        }
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
                return category != null ? category.getCategoryName() : null;
            }

            @Override
            public Category fromString(String string) {
                return null;
            }
        });
        comboBoxMovementCategory.setOnAction(event ->
                comboBoxMovementCategory.getSelectionModel().getSelectedItem());
    }

    private void initializeLblAmount(String actionButton) {
        if (actionButton.equals(AppTexts.BTN_ADD_EXPENSE)) {
            lblAmount.setText(AppTexts.EXPENSE);
        } else if (actionButton.equals(AppTexts.BTN_ADD_INCOME)) {
            lblAmount.setText(AppTexts.INCOME);
        }
    }

    @FXML
    protected void changeMovementsView(ActionEvent actionEvent) {
        if (actionEvent.getSource() instanceof ToggleButton button) {
            if (Objects.equals(button.getId(), AppTexts.BUTTON_SORT)) {
                sortMovementsByDate();
            } else if (Objects.equals(button.getId(), AppTexts.BTN_PLUS) ||
                    Objects.equals(button.getId(), AppTexts.BTN_MINUS) ||
                    Objects.equals(button.getId(), AppTexts.BTN_PLUS_AND_MINUS)) {
                selectBySearchString(searchString);
            }
        }
    }

    public String getYearFilter() {
        return comboBoxYear.getValue() != null ? comboBoxYear.getValue() : null;
    }

    public String getMonthFilter() {
        return comboBoxMonth.getValue() != null ? String.valueOf(comboBoxMonth.getValue().getMonthNumber()) : null;
    }

    public String getCategoryFilter() {
        return comboBoxCategory.getValue() != null ? comboBoxCategory.getValue().getCategoryName() : null;
    }

    public String getSignFilter() {
        ToggleButton selectedButton = (ToggleButton) toggleGroup.getSelectedToggle();
        if (selectedButton != null) {
            String selectedButtonText = selectedButton.getId();
            if (AppTexts.BTN_PLUS.equals(selectedButtonText)) {
                return AppTexts.SIGN_PLUS;
            } else if (AppTexts.BTN_MINUS.equals(selectedButtonText)) {
                return AppTexts.SIGN_MINUS;
            }
        }
        return null;
    }

    public List<String> getFilterList() {
        List<String> filterList = new ArrayList<>();
        filterList.add(getSignFilter());
        filterList.add(getCategoryFilter());
        filterList.add(getMonthFilter());
        filterList.add(getYearFilter());
        return filterList;
    }

    @FXML
    private void saveMovement() {
        try {
            if (selectedMovement != null) {
                editSelectedMovement();
            } else {
                createNewMovement();
                rebuildFilteredMovements(getFilterList());
                clearAllMovementFields();
            }
        } catch (NumberFormatException e ) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(AppTexts.ERROR_WHEN_SAVING);
            alert.setHeaderText(AppTexts.CANNOT_BE_CONVERTED_TO_NUMBERS);
            alert.setContentText(String.format(AppTexts.CANNOT_BE_CONVERTED, txtAmount.getText()));
            alert.showAndWait();
        }
    }

    private void clearAllMovementFields() {
        txtAmount.clear();
        txtDescription.clear();
        comboBoxMovementCategory.setValue(comboBoxMovementCategory.getItems().get(0));
        datePicker.setValue(LocalDate.now());
        comboBoxHours.setValue(String.valueOf(LocalTime.now().getHour()));
        comboBoxMinutes.setValue(String.valueOf(LocalTime.now().getMinute()));
    }

    private void editSelectedMovement() {

        selectedMovement.setAmount(Double.parseDouble(txtAmount.getText()));
        selectedMovement.setCategoryName(comboBoxMovementCategory.getValue().getCategoryName());
        selectedMovement.setDetail(txtDescription.getText());
        selectedMovement.setDate(String.valueOf(datePicker.getValue()));

        MovementHolder.getInstance().getFilteredMovements().set(
                MovementHolder.getInstance().getFilteredMovements().indexOf(selectedMovement), selectedMovement
        );
    }

    private void createNewMovement() {
        Movement movement = new Movement(
                Double.parseDouble(txtAmount.getText()),
                comboBoxMovementCategory.getValue().getCategoryName(),
                comboBoxMovementCategory.getValue().getCategorySign(),
                txtDescription.getText(),
                String.valueOf(datePicker.getValue()),
                Integer.parseInt(comboBoxHours.getValue()),
                Integer.parseInt(comboBoxMinutes.getValue())
        );
        MovementHolder.getInstance().getFilteredMovements().add(movement);
    }

    public void sortMovementsByDate() {
        movementSortToggele = !movementSortToggele;

        MovementHolder.getInstance().getFilteredMovements().sort((currentMovement, nextMovement) -> {
            String currentMovementDateIndex = currentMovement.getDateIndex();
            String nextMovementDateIndex = nextMovement.getDateIndex();

            if (movementSortToggele) {
                return currentMovementDateIndex.compareTo(nextMovementDateIndex);
            } else {
                return -currentMovementDateIndex.compareTo(nextMovementDateIndex);
            }
        });
    }

    public double getTotal() {
        double incomes = 0d;
        double expenses = 0d;
        for (Movement movement : MovementHolder.getInstance().getFilteredMovements()) {
            if (movement.getCategorySign().equals(AppTexts.SIGN_PLUS)) {
                incomes += movement.getAmount();
            } else if (movement.getCategorySign().equals(AppTexts.SIGN_MINUS)) {
                expenses += movement.getAmount();
            }
        }
        return Math.abs(incomes - expenses);
    }

    public void setTotal() {
        txtTotal.setText(String.valueOf(getTotal()));
        //set label before total
        Image minus = new Image(Objects.requireNonNull(Main.class.getResourceAsStream(AppFiles.ICONS_MINUS)));
        Image plus = new Image(Objects.requireNonNull(Main.class.getResourceAsStream(AppFiles.ICONS_PLUS)));
        Image equal = new Image(Objects.requireNonNull(Main.class.getResourceAsStream(AppFiles.ICONS_EQUAL)));

        if (getSignFilter() == null) {

            labelTotal.setGraphic(new ImageView(equal));
        } else if (getSignFilter().equals(AppTexts.SIGN_MINUS)) {
            labelTotal.setGraphic(new ImageView(minus));
        } else if (getSignFilter().equals(AppTexts.SIGN_PLUS)) {
            labelTotal.setGraphic(new ImageView(plus));
        }
    }

    private void getSearchInDetailsFilter() {
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            selectBySearchString(newValue);
            searchString = newValue;
        });

    }

    private void selectBySearchString(String searchString) {
        rebuildFilteredMovements(getFilterList());
        List<Movement> searchedMovements = new ArrayList<>();
        for (Movement movement : MovementHolder.getInstance().getFilteredMovements()) {
            if (movement.getDetail().toLowerCase().contains(searchString.toLowerCase())) {
                searchedMovements.add(movement);
            }
        }
        MovementHolder.getInstance().setFilteredMovements(FXCollections.observableArrayList(searchedMovements));
        tableMovements.setItems(MovementHolder.getInstance().getFilteredMovements());
        setTotal();
    }

    @FXML
    private void clearSearch() {
        txtSearch.textProperty().set(AppTexts.VOID_STRING);
    }
}
//endregion
