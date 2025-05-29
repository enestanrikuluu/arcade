package siege.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import siege.controller.AuthController;
import siege.controller.GameController;
import siege.controller.ScoreController;
import siege.model.GameException;
import siege.model.Player;
import siege.model.User;
import siege.util.GameConstants;
import siege.util.ImageLoader;

/**
 * Frame for the main menu
 */
public class MainMenuFrame extends JFrame {
    // UI Components
    private JButton playButton;
    private JButton scoresButton;
    private JButton gameHistoryButton;
    private JButton exitButton;
    private JLabel welcomeLabel;
    private JLabel profilePictureLabel;
    
    private User user;
    
    private ScoreController scoreController;
    
    /**
     * Creates a new main menu frame
     */
    public MainMenuFrame(User user) {
        this.user = user;
        this.scoreController = new ScoreController();
        
        setTitle("Knowledge Siege - Main Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        
        createComponents();
        
        layoutComponents();
        
        addEventHandlers();
    }
    
    /**
     * Creates the UI components
     */
    private void createComponents() {
        welcomeLabel = new JLabel("Welcome, " + user.getUsername() + "!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        
        profilePictureLabel = new JLabel();
        profilePictureLabel.setPreferredSize(new Dimension(100, 100));
        profilePictureLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        profilePictureLabel.setHorizontalAlignment(JLabel.CENTER);
        
        if (user.getProfilePicturePath() != null && !user.getProfilePicturePath().isEmpty()) {
            try {
                BufferedImage profileImage = ImageLoader.loadImage(user.getProfilePicturePath());
                BufferedImage scaledImage = ImageLoader.scaleImage(profileImage, 100, 100);
                profilePictureLabel.setIcon(new ImageIcon(scaledImage));
            } catch (GameException e) {
                profilePictureLabel.setText(user.getUsername().substring(0, 1).toUpperCase());
                profilePictureLabel.setFont(new Font("Arial", Font.BOLD, 36));
            }
        } else {
            profilePictureLabel.setText(user.getUsername().substring(0, 1).toUpperCase());
            profilePictureLabel.setFont(new Font("Arial", Font.BOLD, 36));
        }
        
        playButton = new JButton("Play Game");
        playButton.setFont(new Font("Arial", Font.BOLD, 16));
        playButton.setPreferredSize(new Dimension(200, 50));
        
        scoresButton = new JButton("View Scores");
        scoresButton.setFont(new Font("Arial", Font.PLAIN, 16));
        scoresButton.setPreferredSize(new Dimension(200, 50));
        
        gameHistoryButton = new JButton("My Game History");
        gameHistoryButton.setFont(new Font("Arial", Font.PLAIN, 16));
        gameHistoryButton.setPreferredSize(new Dimension(200, 50));

        exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.PLAIN, 16));
        exitButton.setPreferredSize(new Dimension(200, 50));
    }
    
    /**
     * Layouts the components
     */
    private void layoutComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setLayout(new BorderLayout());
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.add(profilePictureLabel);
        topPanel.add(welcomeLabel);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        JLabel titleLabel = new JLabel("Knowledge Siege", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        mainPanel.add(titleLabel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        
        buttonPanel.add(Box.createVerticalStrut(10));
        
        JPanel playButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        playButtonPanel.add(playButton);
        buttonPanel.add(playButtonPanel);
        
        buttonPanel.add(Box.createVerticalStrut(10));
        
        JPanel scoresButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        scoresButtonPanel.add(scoresButton);
        buttonPanel.add(scoresButtonPanel);
        
        buttonPanel.add(Box.createVerticalStrut(10));
        
        JPanel gameHistoryButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        gameHistoryButtonPanel.add(gameHistoryButton);
        buttonPanel.add(gameHistoryButtonPanel);
        
        buttonPanel.add(Box.createVerticalStrut(10));
        
        JPanel exitButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        exitButtonPanel.add(exitButton);
        buttonPanel.add(exitButtonPanel);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    /**
     * Adds event handlers to components
     */
    private void addEventHandlers() {
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });
        
        scoresButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showScoreboard();
            }
        });
        
        gameHistoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showGameHistory();
            }
        });
        
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exitGame();
            }
        });
    }
    
    /**
     * Starts a new game
     */
    private void startGame() {
        try {
            BufferedImage playerImage;
            if (user.getProfilePicturePath() != null && !user.getProfilePicturePath().isEmpty()) {
                playerImage = ImageLoader.loadImage(user.getProfilePicturePath());
            } else {
                playerImage = ImageLoader.loadImage(GameConstants.DEFAULT_PLAYER_IMAGE);
            }
            
            playerImage = ImageLoader.scaleImage(playerImage, 
                    GameConstants.PLAYER_WIDTH, GameConstants.PLAYER_HEIGHT);
            
            Player player = new Player(
                    user.getUsername(),
                    GameConstants.PLAYER_START_X,
                    GameConstants.PLAYER_START_Y,
                    GameConstants.PLAYER_WIDTH,
                    GameConstants.PLAYER_HEIGHT,
                    playerImage
            );
            
            GameFrame gameFrame = new GameFrame(user, player);
            GameController gameController = new GameController(user, gameFrame);
            gameFrame.setGameController(gameController);
            
            gameFrame.setVisible(true);
            this.dispose();
        } catch (GameException e) {
            JOptionPane.showMessageDialog(this, 
                    "Failed to start game: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Shows the scoreboard
     */
    private void showScoreboard() {
        ScoreboardDialog dialog = new ScoreboardDialog(this, scoreController);
        dialog.setVisible(true);
    }
    
    /**
     * Shows the current user's game history
     */
    private void showGameHistory() {
        GameHistoryFrame historyDialog = new GameHistoryFrame(this, user, scoreController);
        historyDialog.setVisible(true);
    }
    
    /**
     * Exits the game
     */
    private void exitGame() {
        int option = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to exit?",
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        
        if (option == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
} 