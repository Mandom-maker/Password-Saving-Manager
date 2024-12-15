package sample.demo;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



public class MessageHandler {
    private static final Logger logger = LogManager.getLogger(MessageHandler.class);

    /**
     * Displays a window with decrypted credentials in a table.
     *
     * @param credentials The list of credentials to display.
     */
    public void showDecryptedMessageWindow(ObservableList<Credential> credentials) {
        Stage messageStage = new Stage();

        TableView<Credential> tableView = new TableView<>();
        tableView.setItems(credentials);

        // Define table columns
        TableColumn<Credential, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<Credential, String> passwordColumn = new TableColumn<>("Password");
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));

        TableColumn<Credential, String> encryptionTypeColumn = new TableColumn<>("Encryption Type");
        encryptionTypeColumn.setCellValueFactory(new PropertyValueFactory<>("encryptionType"));

        // Add columns to the table
        tableView.getColumns().addAll(usernameColumn, passwordColumn, encryptionTypeColumn);

        // Layout setup
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));
        vbox.getChildren().addAll(new Label("Credentials:"), tableView);

        // Scene and stage setup
        Scene scene = new Scene(vbox, 500, 300);
        messageStage.setTitle("Credentials");
        messageStage.setScene(scene);
        messageStage.show();

        logger.info("Credentials window shown");
    }
}