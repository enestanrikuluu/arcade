package siege.model;

import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 * Represents a user account in the game
 */
public class User implements Serializable {
    private String username;
    private String password;
    private transient BufferedImage profilePicture; 
    private String profilePicturePath;
    
    /**
     * Creates a new user
     */
    public User(String username, String password, String profilePicturePath) {
        this.username = username;
        this.password = password;
        this.profilePicturePath = profilePicturePath;
    }
    
    /**
     * Checks if the provided password matches
     */
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
    
    /**
     * Sets the profile picture
     */
    public void setProfilePicture(BufferedImage picture) {
        this.profilePicture = picture;
    }
    
    // Getters
    public String getUsername() {
        return username;
    }
    
    public BufferedImage getProfilePicture() {
        return profilePicture;
    }
    
    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public String getPassword() {
        return password;
    }
    
    /**
     * Returns a string representation of the user
     */
    @Override
    public String toString() {
        return username;
    }
} 