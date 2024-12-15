package sample.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * The main application class that initializes the login window and handles user authentication.
 * It uses JavaFX for the graphical user interface and interacts with the database to validate user credentials.
 */
public class SignInWindowInitialization {
    private static final Logger logger = LogManager.getLogger(SignInWindowInitialization.class);

    private Stage loginStage;
    private Encryption encryptProcess = new Encryption();
    private MessageHandler messageHandler = new MessageHandler();
    private DatabaseManager databaseManager = new DatabaseManager(); // Object for database operations

    /**
     * Initializes and displays the login window.
     * The window contains fields for username and password input, and a button to submit the credentials.
     */
    public void SignInWindow() {
        loginStage = new Stage();

        // Create UI components
        Label usernameLabel = new Label("Input your username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Input your password:");
        TextField passwordField = new TextField();
        Button enterButton = new Button("Enter");
        enterButton.setPrefSize(50, 30);

        // Set up the layout
        VBox vbox1 = createLayout(usernameLabel, usernameField, passwordLabel, passwordField, enterButton);

        // Set up the scene and stage
        Scene scene = new Scene(vbox1, 500, 250);
        loginStage.setTitle("Sign in window");
        loginStage.setScene(scene);
        loginStage.show();

        // Set up the event handler for the "Enter" button
        enterButton.setOnAction(event -> handleLogin(usernameField, passwordField));
    }

    /**
     * Creates the layout for the login window.
     *
     * @param usernameLabel  The label for the username field.
     * @param usernameField  The text field for entering the username.
     * @param passwordLabel  The label for the password field.
     * @param passwordField  The text field for entering the password.
     * @param enterButton    The button to submit the credentials.
     * @return The VBox layout containing all UI components.
     */
    private VBox createLayout(Label usernameLabel, TextField usernameField, Label passwordLabel, TextField passwordField, Button enterButton) {
        VBox vbox1 = new VBox(10);
        VBox vbox2 = new VBox(10);
        vbox1.setPadding(new Insets(20));
        vbox2.getChildren().addAll(enterButton);
        vbox1.getChildren().addAll(usernameLabel, usernameField, passwordLabel, passwordField, vbox2);
        vbox2.setAlignment(Pos.BOTTOM_RIGHT);
        return vbox1;
    }

    /**
     * Handles the login process when the "Enter" button is clicked.
     *
     * @param usernameField The text field containing the entered username.
     * @param passwordField The text field containing the entered password.
     */
    private void handleLogin(TextField usernameField, TextField passwordField) {
        String enteredUsername = usernameField.getText();
        String enteredPassword = passwordField.getText();

        // Validate username and password length
        if (!validateInput(enteredUsername, enteredPassword)) {
            return;
        }

        // Array of encryption types to try
        String[] bufferCiphers = {"Base64", "md5", "Feistel cipher", "Salt", "Without cipher"};

        boolean loginSuccessful = false;

        try {
            // Try to match the entered credentials with the stored ones using different encryption types
            for (String cipher : bufferCiphers) {
                String encryptedUsername = encryptProcess.encrypt(enteredUsername, cipher);
                String encryptedPassword = encryptProcess.encrypt(enteredPassword, cipher);

                // Check credentials in the database
                if (databaseManager.checkCredentials(encryptedUsername, encryptedPassword)) {
                    logger.info("Login successful for user: {}", enteredUsername);

                    // Load all credentials from the database
                    Map<String, Credential> credentials = databaseManager.loadCredentialsFromDatabase();

                    // Create a list of credentials for display
                    ObservableList<Credential> credentialList = FXCollections.observableArrayList(credentials.values());

                    // Show the window with decrypted data
                    messageHandler.showDecryptedMessageWindow(credentialList);

                    loginSuccessful = true;
                    break;
                }
            }
        } catch (NoSuchAlgorithmException e) {
            logger.error("NoSuchAlgorithmException occurred", e);
            showErrorAlert("Encryption Error", "The selected encryption method is not supported.");
        } catch (Exception e) {
            logger.error("Exception occurred", e);
            showErrorAlert("Error", "An unexpected error occurred.");
        }

        // If login was not successful, show an error message
        if (!loginSuccessful) {
            showErrorAlert("Login Failed", "Invalid username or password.");
            logger.info("Login failed for user: {}", enteredUsername);
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
        if (password.length() > 200 || username.length() > 200) {
            showErrorAlert("Username or password too long", "Username and password must not exceed 200 characters.");
            logger.warn("Too long username or password entered");
            return false;
        }
        return true;
    }

    /**
     * Displays an error alert with the specified title and message.
     *
     * @param title   The title of the alert.
     * @param message The message to display in the alert.
     */
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}