package sample.demo;

/**
 * Represents a credential object containing a username, password, and encryption type.
 * This class is used to store and manage user credentials along with the encryption
 * method used to secure them.
 */
public class Credential {
    private String username;
    private String password;
    private String encryptionType;

    /**
     * Constructs a new Credential object with the specified username, password, and encryption type.
     *
     * @param username       The username associated with the credential.
     * @param password       The password associated with the credential.
     * @param encryptionType The type of encryption used for the credential.
     */
    public Credential(String username, String password, String encryptionType) {
        this.username = username;
        this.password = password;
        this.encryptionType = encryptionType;
    }

    /**
     * Returns the username associated with this credential.
     *
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username for this credential.
     *
     * @param username The username to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the password associated with this credential.
     *
     * @return The password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password for this credential.
     *
     * @param password The password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the encryption type used for this credential.
     *
     * @return The encryption type.
     */
    public String getEncryptionType() {
        return encryptionType;
    }

    /**
     * Sets the encryption type for this credential.
     *
     * @param encryptionType The encryption type to set.
     */
    public void setEncryptionType(String encryptionType) {
        this.encryptionType = encryptionType;
    }
}
