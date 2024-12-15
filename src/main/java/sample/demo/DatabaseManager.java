package sample.demo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages database operations such as establishing a connection, registering users,
 * checking credentials, and loading credentials from the database.
 */
public class    DatabaseManager {
    private static final Logger logger = LogManager.getLogger(DatabaseManager.class);
    private static final String URL = "jdbc:h2:./data/mydatabase"; // Relative path
    private static final String USER = "sa"; // Default user for H2
    private static final String PASSWORD = ""; // Default password for H2
    private static Connection conn;

    static {
        try {
            // Load the H2 JDBC driver
            Class.forName("org.h2.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            logger.info("Database connection established");
        } catch (ClassNotFoundException e) {
            logger.error("H2 JDBC driver not found", e);
            throw new RuntimeException("H2 JDBC driver not found", e);
        } catch (SQLException e) {
            logger.error("Failed to establish database connection", e);
            throw new RuntimeException("Failed to establish database connection", e);
        }
    }

    /**
     * Registers a new user in the database.
     *
     * @param username       The username of the user.
     * @param password       The password of the user.
     * @param encryptionType The encryption type used for the password.
     * @return true if the registration was successful, false otherwise.
     */
    public boolean registerUser(String username, String password, String encryptionType) {
        try {
            createUsersTableIfNotExists();
            String sql = "INSERT INTO users (username, password, encryption_type) VALUES (?, ?, ?)";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                pstmt.setString(3, encryptionType);

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    logger.info("User registered successfully: {}", username);
                    return true;
                } else {
                    logger.warn("Failed to register user: {}", username);
                    return false;
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException occurred while registering user", e);
            return false;
        }
    }

    /**
     * Creates the 'users' table if it does not already exist.
     *
     * @throws SQLException if a database access error occurs.
     */
    private void createUsersTableIfNotExists() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "username VARCHAR(200) NOT NULL UNIQUE, " +
                "password VARCHAR(200) NOT NULL, " +
                "encryption_type VARCHAR(50) NOT NULL" +
                ")";
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createTableSQL);
        }
    }

    /**
     * Checks if a user with the specified credentials exists in the database.
     *
     * @param username The username to check.
     * @param password The password to check.
     * @return true if the user exists, false otherwise.
     */
    public boolean checkCredentials(String username, String password) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ? AND password = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException occurred while checking credentials", e);
        }
        return false;
    }

    /**
     * Loads all user credentials from the database.
     *
     * @return A map containing the credentials (username -> Credential object).
     */
    public Map<String, Credential> loadCredentialsFromDatabase() {
        Map<String, Credential> credentials = new HashMap<>();
        String sql = "SELECT username, password, encryption_type FROM users";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("password");
                String encryptionType = rs.getString("encryption_type");

                Credential credential = new Credential(username, password, encryptionType);
                credentials.put(username, credential);
            }
            logger.info("Credentials loaded successfully");
        } catch (SQLException e) {
            logger.error("SQLException occurred while loading credentials", e);
        }
        return credentials;
    }
}