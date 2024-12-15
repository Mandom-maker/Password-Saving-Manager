package sample.demo;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.NoSuchAlgorithmException;

/**
 * The class responsible for initializing and displaying the registration window.
 * This class sets up the UI components for user registration, including input fields
 * for username and password, a choice box for selecting encryption methods, and a button
 * to confirm registration. It also handles user input validation and encryption of data.
 */
public class RegistrationWindowInitialization {
    private static final Logger logger = LogManager.getLogger(RegistrationWindowInitialization.class);

    private Encryption encryptProcess = new Encryption();
    private Stage registerStage;
    private DatabaseManager databaseManager = new DatabaseManager(); // Object for database operations

    /**
     * Initializes and displays the registration window.
     * This method sets up the UI components, validates user input, encrypts the data,
     * and writes the encrypted data to a database. It also displays appropriate alerts
     * for success or error messages.
     */
    public void RegisterWindow() {
        registerStage = new Stage();

        // Create UI elements
        Label choiceCipherLabel = new Label("Choose cipher:");
        Button okButton = new Button("OK");
        ChoiceBox<String> choiceCipherBox = new ChoiceBox<>();
        choiceCipherBox.getItems().addAll("Base64", "md5", "Feistel cipher", "Salt", "Without cipher");
        choiceCipherBox.setValue("Without cipher"); // Set default value
        Label usernameLabel = new Label("Input your username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Input your password:");
        TextField passwordField = new TextField();
        okButton.setPrefSize(50, 30);

        // Set up the layout
        VBox layout = createLayout(choiceCipherLabel, choiceCipherBox, usernameLabel, usernameField, passwordLabel, passwordField, okButton);

        // Set up the scene and stage
        Scene scene = new Scene(layout, 500, 250);
        registerStage.setTitle("Registration window");
        registerStage.setScene(scene);
        registerStage.show();

        // Set up the event handler for the "OK" button
        okButton.setOnAction(event -> handleRegistration(usernameField, passwordField, choiceCipherBox));
    }

    /**
     * Creates the layout for the registration window.
     *
     * @param choiceCipherLabel The label for the choice box.
     * @param choiceCipherBox   The choice box for selecting encryption methods.
     * @param usernameLabel     The label for the username field.
     * @param usernameField     The text field for entering the username.
     * @param passwordLabel     The label for the password field.
     * @param passwordField     The text field for entering the password.
     * @param okButton          The button to confirm registration.
     * @return The VBox layout containing all UI components.
     */
    private VBox createLayout(Label choiceCipherLabel, ChoiceBox<String> choiceCipherBox, Label usernameLabel, TextField usernameField, Label passwordLabel, TextField passwordField, Button okButton) {
        VBox vbox1 = new VBox(10);
        VBox vbox2 = new VBox(10);

        vbox1.setPadding(new Insets(20));
        vbox2.getChildren().addAll(choiceCipherLabel, choiceCipherBox, okButton);
        vbox1.getChildren().addAll(usernameLabel, usernameField, passwordLabel, passwordField, vbox2);

        vbox2.setAlignment(Pos.BOTTOM_RIGHT);
        return vbox1;
    }

    /**
     * Handles the registration process when the "OK" button is clicked.
     *
     * @param usernameField    The text field containing the entered username.
     * @param passwordField    The text field containing the entered password.
     * @param choiceCipherBox  The choice box containing the selected encryption method.
     */
    private void handleRegistration(TextField usernameField, TextField passwordField, ChoiceBox<String> choiceCipherBox) {
        String inputUsername = usernameField.getText();
        String inputPassword = passwordField.getText();
        String cipherChoice = choiceCipherBox.getValue();

        // Validate username and password length
        if (!validateInput(inputUsername, inputPassword)) {
            return;
        }

        try {
            // Encrypt username and password
            String encryptedUser = encryptProcess.encrypt(inputUsername, cipherChoice);
            String encryptedPassword = encryptProcess.encrypt(inputPassword, cipherChoice);
            logger.debug("Encrypted Username: {}", encryptedUser);
            logger.debug("Encrypted Password: {}", encryptedPassword);

            // Write encrypted data to the database
            boolean isRegistered = databaseManager.registerUser(encryptedUser, encryptedPassword, cipherChoice);
            if (isRegistered) {
                // Show success message
                showAlert(Alert.AlertType.INFORMATION, "Registration successful",
                        "Your data has been successfully registered.");
                logger.info("Registration successful for user: {}", inputUsername);

                // Close the registration window
                registerStage.close();
            } else {
                // Show error message if registration failed
                showAlert(Alert.AlertType.ERROR, "Registration failed",
                        "An error occurred while registering your data.");
                logger.error("Registration failed for user: {}", inputUsername);
            }

        } catch (NoSuchAlgorithmException e) {
            logger.error("NoSuchAlgorithmException occurred", e);
            showAlert(Alert.AlertType.ERROR, "Encryption Error",
                    "The selected encryption method is not supported.");
        } catch (Exception e) {
            logger.error("Exception occurred", e);
            showAlert(Alert.AlertType.ERROR, "Error",
                    "An unexpected error occurred.");
        }
    }

    /**
     * Validates the length of the entered username and password.
     *
     * @param username The entered username.
     * @param password The entered password.
     * @return true if the input is valid, false otherwise.
     */
    private boolean validateInput(String username, String password) {
        if (password.length() > 200 || username.length() > 200 || username.length() < 3 || password.length() < 3) {
            showAlert(Alert.AlertType.ERROR, "Username or password too long or too short",
                    "Username and password must not exceed 200 characters and must not be less than 3 characters.");
            logger.warn("Invalid username or password entered");
            return false;
        }
        return true;
    }

    /**
     * Displays an alert dialog with the specified type, title, and message.
     *
     * @param alertType The type of alert (e.g., INFORMATION, ERROR).
     * @param title     The title of the alert dialog.
     * @param message   The message to display in the alert dialog.
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}