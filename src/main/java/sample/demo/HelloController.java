package sample.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * The controller class for the "Hello" screen in the application.
 * This class handles the interaction between the UI and the application logic,
 * specifically for the "Hello" button and the welcome message.
 */
public class HelloController {

    /**
     * The label that displays the welcome message.
     */
    @FXML
    private Label welcomeText;

    /**
     * Handles the event when the "Hello" button is clicked.
     * Updates the welcome message to display a greeting to the user.
     */
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}