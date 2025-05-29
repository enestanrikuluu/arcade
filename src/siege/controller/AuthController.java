package siege.controller;

import java.io.*;
import java.util.ArrayList;
import siege.model.GameException;
import siege.model.User;
import siege.util.GameConstants;

/**
 * Controller for user authentication and registration
 */
public class AuthController {
    // List of registered users
    private ArrayList<User> users;
    
    /**
     * Creates a new authentication controller
     */
    public AuthController() {
        this.users = new ArrayList<>();
        loadUsers();
    }
    
    /**
     * Loads users from individual object files in the users directory.
     */
    private void loadUsers() {
        File usersDir = new File(GameConstants.USERS_DIR);
        if (!usersDir.exists()) {
            usersDir.mkdirs(); // Create the directory if it doesn't exist
            return; // No users to load yet
        }

        File[] userFiles = usersDir.listFiles((dir, name) -> name.endsWith(GameConstants.USER_FILE_EXTENSION));

        if (userFiles != null) {
            for (File userFile : userFiles) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(userFile))) {
                    User user = (User) ois.readObject();
                    this.users.add(user);
                } catch (FileNotFoundException e) {
                    System.err.println("User file not found (should not happen if listFiles worked): " + userFile.getName() + " - " + e.getMessage());
                } catch (IOException | ClassNotFoundException e) {
                    System.err.println("Failed to load user from file: " + userFile.getName() + " - " + e.getMessage());
                    // Optionally, you might want to move or delete corrupted files.
                }
            }
        }
    }
    
    /**
     * Saves a single user to an object file.
     */
    private void saveUser(User user) {
        File usersDir = new File(GameConstants.USERS_DIR);
        if (!usersDir.exists()) {
            usersDir.mkdirs(); // Ensure directory exists
        }

        File userFile = new File(usersDir, user.getUsername() + GameConstants.USER_FILE_EXTENSION);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(userFile))) {
            oos.writeObject(user);
        } catch (IOException e) {
            System.err.println("Failed to save user to file: " + user.getUsername() + " - " + e.getMessage());
            // Consider re-throwing as a GameException if this is critical for the register flow
        }
    }
    
    /**
     * Registers a new user.
     * Throws GameException if username is empty, password is empty, or username already exists.
     */
    public void register(String username, String password, String profilePicturePath) throws GameException {
        if (username == null || username.trim().isEmpty()) {
            throw new GameException("Username cannot be empty");
        }
        
        if (password == null || password.trim().isEmpty()) {
            throw new GameException("Password cannot be empty");
        }
        
        // Check against currently loaded users (which includes all persisted users)
        if (findUserByUsername(username) != null) {
            throw new GameException("Username already exists. Please choose a different one.");
        }
        
        User newUser = new User(username, password, profilePicturePath);
        users.add(newUser);
        saveUser(newUser); // Save the new user immediately
    }
    
    /**
     * Logs in a user
     */
    public User login(String username, String password) throws GameException {
        // Special superadmin login for testing purposes
        if ("superadmin".equals(username) && "SuperAdmin132".equals(password)) {
            return new User("superadmin", "SuperAdmin132", "");
        }
        
        User user = findUserByUsername(username);
        
        if (user == null) {
            throw new GameException("User not found");
        }
        
        if (!user.checkPassword(password)) {
            throw new GameException("Incorrect password");
        }
        
        return user;
    }
    
    /**
     * Finds a user by username
     */
    private User findUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
    
    /**
     * Gets all registered users
     */
    public ArrayList<User> getUsers() {
        return new ArrayList<>(users);
    }
} 