package siege.view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import siege.controller.AuthController;
import siege.model.GameException;
import siege.model.User;

/**
 * Frame for user login
 */
public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    
    private AuthController authController;
    
    /**
     * Creates a new login frame
     */
    public LoginFrame(AuthController authController) {
        this.authController = authController;
        
        setTitle("Knowledge Siege - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
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
        
        loginButton = new JButton("Login");
        
        registerButton = new JButton("Register");
    }
    
    /**
     * Layouts the components
     */
    private void layoutComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setLayout(new BorderLayout());
        
        JLabel titleLabel = new JLabel("Knowledge Siege", JLabel.CENTER);
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
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    /**
     * Adds event handlers to components
     */
    private void addEventHandlers() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openRegisterFrame();
            }
        });
        
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    login();
                }
            }
        });
    }
    
    /**
     * Performs login
     */
    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        try {
            User user = authController.login(username, password);
            
            openMainMenu(user);
        } catch (GameException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Opens the register frame
     */
    private void openRegisterFrame() {
        RegisterFrame registerFrame = new RegisterFrame(authController);
        registerFrame.setVisible(true);
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