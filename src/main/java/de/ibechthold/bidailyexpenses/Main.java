package de.ibechthold.bidailyexpenses;

import de.ibechthold.bidailyexpenses.gui.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Start class of the project
 */
public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        SceneManager.getInstance().setAndConfigureMainStage(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}