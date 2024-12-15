package sample.demo;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * The class responsible for initializing and displaying the main application window.
 * It sets up the main window's UI components, including buttons and labels, and handles
 * user interactions such as button clicks.
 */
public class MainWindowInitialization {
    private static final Logger logger = LogManager.getLogger(MainWindowInitialization.class);

    /**
     * Initializes and displays the main application window.
     * This method sets up the UI components, such as buttons and labels, and defines
     * their behavior when clicked. It also configures the layout and displays the window.
     *
     * @param stage The primary stage for the application, representing the main window.
     * @throws IOException If an error occurs while loading data or initializing the window.
     */
    public void MainWindow(Stage stage) throws IOException {
        logger.info("Initializing Main Window");

        // Initialize main window components
        Button loginButton = createButton("Sign in", 100, 50);
        Button registerButton = createButton("Register", 100, 50);
        Button exitButton = createButton("Exit", 80, 30); // Уменьшенная кнопка выхода
        Label mainLabel = createLabel("\n Password Manager", 20);
        Label subLabel = createLabel("Choose an action:", 15);

        // Set button actions
        loginButton.setOnAction(event -> handleLoginButtonClick());
        registerButton.setOnAction(event -> handleRegisterButtonClick());
        exitButton.setOnAction(event -> handleExitButtonClick(stage));

        // Layout setup
        HBox hbox = createHBox(20, loginButton, registerButton); // Кнопка выхода не добавляется сюда
        VBox vbox = createVBox(20, mainLabel, subLabel, hbox);

        // Добавляем кнопку выхода внизу справа
        HBox bottomHBox = new HBox(exitButton);
        bottomHBox.setAlignment(Pos.BOTTOM_RIGHT);
        vbox.getChildren().add(bottomHBox); // Добавляем HBox с кнопкой выхода внизу

        // Align components
        vbox.setAlignment(Pos.TOP_CENTER);
        hbox.setAlignment(Pos.CENTER);

        // Create and set the scene
        Scene scene = new Scene(vbox, 525, 225);
        stage.setTitle("Password Manager");
        stage.setScene(scene);
        stage.show();

        logger.info("Main Window initialized and shown");
    }

    /**
     * Creates a button with the specified text and size.
     *
     * @param text The text to display on the button.
     * @param width The width of the button.
     * @param height The height of the button.
     * @return The created Button object.
     */
    private Button createButton(String text, int width, int height) {
        Button button = new Button(text);
        button.setPrefSize(width, height);
        return button;
    }

    /**
     * Creates a label with the specified text and font size.
     *
     * @param text The text to display on the label.
     * @param fontSize The font size for the label.
     * @return The created Label object.
     */
    private Label createLabel(String text, int fontSize) {
        Label label = new Label(text);
        label.setFont(new Font(fontSize));
        return label;
    }

    /**
     * Creates an HBox layout with the specified spacing and child components.
     *
     * @param spacing The spacing between child components.
     * @param children The child components to add to the HBox.
     * @return The created HBox layout.
     */
    private HBox createHBox(int spacing, Node... children) {
        HBox hbox = new HBox(spacing);
        hbox.getChildren().addAll(children);
        return hbox;
    }

    /**
     * Creates a VBox layout with the specified spacing and child components.
     *
     * @param spacing The spacing between child components.
     * @param children The child components to add to the VBox.
     * @return The created VBox layout.
     */
    private VBox createVBox(int spacing, Node... children) {
        VBox vbox = new VBox(spacing);
        vbox.getChildren().addAll(children);
        return vbox;
    }

    /**
     * Handles the action when the "Log in" button is clicked.
     * It initializes and displays the login window.
     */
    private void handleLoginButtonClick() {
        logger.info("Sign in button clicked");
        SignInWindowInitialization loginWindow = new SignInWindowInitialization();
        loginWindow.SignInWindow();
    }

    /**
     * Handles the action when the "Register" button is clicked.
     * It initializes and displays the registration window.
     */
    private void handleRegisterButtonClick() {
        logger.info("Register button clicked");
        RegistrationWindowInitialization registrationWindow = new RegistrationWindowInitialization();
        registrationWindow.RegisterWindow();
    }

    /**
     * Handles the action when the "Exit" button is clicked.
     * It closes the application window.
     *
     * @param stage The primary stage of the application.
     */
    private void handleExitButtonClick(Stage stage) {
        logger.info("Exit button clicked");
        stage.close();
    }
}