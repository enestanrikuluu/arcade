package siege.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

import siege.controller.AuthController;
import siege.model.GameException;
import siege.model.User;

/**
 * Frame for user registration
 */
public class RegisterFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton chooseImageButton;
    private JLabel imagePreviewLabel;
    private JButton registerButton;
    private JButton backButton;
    
    private AuthController authController;
    
    private String profilePicturePath;
    
    /**
     * Creates a new register frame
     */
    public RegisterFrame(AuthController authController) {
        this.authController = authController;
        this.profilePicturePath = "";
        
        setTitle("Knowledge Siege - Register");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 400);
        setLocationRelativeTo(null);
        
        createComponents();
        
        layoutComponents();
        
        addEventHandlers();
    }
    
    /**
     * Creates the UI components
     */
    private void createComponents() {
        usernameField = new JTextField(20);
        
        passwordField = new JPasswordField(20);
        
        confirmPasswordField = new JPasswordField(20);
        
        chooseImageButton = new JButton("Choose Profile Picture");
        
        imagePreviewLabel = new JLabel("No image selected", JLabel.CENTER);
        imagePreviewLabel.setPreferredSize(new Dimension(100, 100));
        imagePreviewLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        registerButton = new JButton("Register");
        
        backButton = new JButton("Back to Login");
    }
    
    /**
     * Layouts the components
     */
    private void layoutComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Register New Account", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
            
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);
        
        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);
        
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Confirm Password:"), gbc);
        
        gbc.gridx = 1;
        formPanel.add(confirmPasswordField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Profile Picture:"), gbc);
        
        gbc.gridx = 1;
        formPanel.add(chooseImageButton, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        formPanel.add(imagePreviewLabel, gbc);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    /**
     * Adds event handlers to components
     */
    private void addEventHandlers() {
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                register();
            }
        });
        
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBackToLogin();
            }
        });
        
        chooseImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseProfilePicture();
            }
        });
    }
    
    /**
     * Opens a file chooser to select a profile picture
     */
    private void chooseProfilePicture() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Image files", "jpg", "jpeg", "png", "gif"));
        
        int result = fileChooser.showOpenDialog(this);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            profilePicturePath = selectedFile.getAbsolutePath();

            ImageIcon icon = new ImageIcon(profilePicturePath);
            Image image = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            imagePreviewLabel.setIcon(new ImageIcon(image));
            imagePreviewLabel.setText("");
        }
    }
    
    /**
     * Performs registration
     */
    private void register() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        
        if (username.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username cannot be empty", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password cannot be empty", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            authController.register(username, password, profilePicturePath);
            
            User user = authController.login(username, password);
            
            JOptionPane.showMessageDialog(this, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            
            openMainMenu(user);
        } catch (GameException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Registration Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Goes back to the login screen
     */
    private void goBackToLogin() {
        LoginFrame loginFrame = new LoginFrame(authController);
        loginFrame.setVisible(true);
        this.dispose();
    }
    
    /**
     * Opens the main menu
     */
    private void openMainMenu(User user) {
        MainMenuFrame mainMenuFrame = new MainMenuFrame(user);
        mainMenuFrame.setVisible(true);
        this.dispose();
    }
} 