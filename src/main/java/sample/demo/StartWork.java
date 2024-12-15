package sample.demo;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The class responsible for launching the application and initializing the main window.
 * This class extends {@link Application} and implements methods to start
 * and display the main window of the application.
 */
public class StartWork extends Application {

    /**
     * The method called when the application starts.
     * Initializes the main window of the application using the {@link MainWindowInitialization} class
     * and displays it.
     *
     * @param stage The main {@link Stage} instance representing the main window of the application.
     * @throws IOException If an I/O error occurs during the initialization of the main window.
     */
    @Override
    public void start(Stage stage) throws IOException {
        MainWindowInitialization al = new MainWindowInitialization();
        al.MainWindow(stage);
        stage.show();
    }

    /**
     * The method to launch the application.
     * Calls the {@link Application#launch(String...)} method to start the JavaFX application.
     *
     * @param args The command-line arguments passed to the application.
     */
    void StartToStart(String[] args) {
        launch(args);
    }
}