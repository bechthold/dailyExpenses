package de.ibechthold.bidailyexpenses.gui;

import de.ibechthold.bidailyexpenses.Main;
import de.ibechthold.bidailyexpenses.model.Category;
import de.ibechthold.bidailyexpenses.model.Movement;
import de.ibechthold.bidailyexpenses.settings.AppTexts;
import de.ibechthold.bidailyexpenses.settings.AppFiles;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 This class implements the controlling logic for the scenes displayed in the project
 */

public class SceneManager {
    //region constants
    //endregion

    //region attributes
    private static SceneManager instance;
    private Stage mainStage;
    private Stage dialog;
    //endregion

    //region constructors
    private SceneManager() {}
    //endregion

    //region methods
    public static synchronized SceneManager getInstance() {
        if (instance == null) instance = new SceneManager();
        return instance;
    }

    public void setAndConfigureMainStage(Stage mainStage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource(AppFiles.OVERVIEW_LAYOUT_FXML)));
        Scene scene = new Scene(root);

        mainStage.setScene(scene);
        mainStage.setResizable(false);
        mainStage.show();

        this.mainStage = mainStage;
        this.mainStage.setTitle(AppTexts.APP_NAME);
    }

    public void switchToOverviewScene() {
        Stage stage = (Stage) dialog.getScene().getWindow();
        stage.close();
    }

    public void switchToCategoryDetailScene(Category selectedCategory, String sign) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(AppFiles.CATEGORY_DETAIL_LAYOUT_FXML));
            dialog = new Stage();
            dialog.initOwner(mainStage);
            dialog.setResizable(false);
            Scene categoryDetailScene = new Scene(fxmlLoader.load());

            CategoryDetailController categoryDetailController = fxmlLoader.getController();
            categoryDetailController.setSelectedCategory(selectedCategory, sign);

            dialog.setScene(categoryDetailScene);
            dialog.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void switchToMovementDetailScene(Movement selectedMovement) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(AppFiles.MOVEMENT_DETAIL_LAYOUT_FXML));
            dialog = new Stage();
            dialog.initOwner(mainStage);
            dialog.setResizable(false);
            Scene movementDetailScene = new Scene(fxmlLoader.load());

            MovementDetailController movementDetailController = fxmlLoader.getController();
            movementDetailController.setSelectedMovement(selectedMovement);

            dialog.setScene(movementDetailScene);
            dialog.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //endregion
}
