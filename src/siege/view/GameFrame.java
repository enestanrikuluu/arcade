package siege.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import siege.controller.GameController;
import siege.io.GameLogger;
import siege.model.*;
import siege.util.GameConstants;

/**
 * Frame for the main game
 */
public class GameFrame extends JFrame {
    private GamePanel gamePanel;
    private ShotBoxFeedPanel feedPanel;
    private JPanel statusBar;
    private JLabel levelLabel;
    private JLabel healthLabel;
    private JLabel scoreLabel;
    private JButton pauseButton;
    private JButton exitButton;
    
    private User user;
    private Player player;
    private GameController gameController;
    
    private boolean paused;
    
    /**
     * Creates a new game frame
     */
    public GameFrame(User user, Player player) {
        this.user = user;
        this.player = player;
        this.paused = false;
        
        setTitle("Knowledge Siege - Game");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(GameConstants.WINDOW_WIDTH, GameConstants.WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);
        
        createComponents();
        
        layoutComponents();
        
        addEventHandlers();
        
        gamePanel.requestFocusInWindow();
    }
    
    /**
     * Creates the UI components
     */
    private void createComponents() {
        gamePanel = new GamePanel(); 
        
        feedPanel = new ShotBoxFeedPanel();
        
        statusBar = new JPanel();
        levelLabel = new JLabel("Level: 1");
        healthLabel = new JLabel("Health: " + player.getHealth());
        scoreLabel = new JLabel("Score: " + player.getScore());
        pauseButton = new JButton("Pause");
        exitButton = new JButton("Exit");
    }
    
    /**
     * Layouts the components
     */
    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        add(gamePanel, BorderLayout.CENTER);
        
        add(feedPanel, BorderLayout.EAST);
        
        statusBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        statusBar.setBorder(BorderFactory.createEtchedBorder());

        statusBar.add(levelLabel);
        statusBar.add(Box.createHorizontalStrut(20));
        statusBar.add(healthLabel);
        statusBar.add(Box.createHorizontalStrut(20));
        statusBar.add(scoreLabel);
        statusBar.add(Box.createHorizontalStrut(50));
        statusBar.add(pauseButton);
        statusBar.add(exitButton);
        
        add(statusBar, BorderLayout.SOUTH);
    }
    
    /**
     * Adds event handlers to components
     */
    private void addEventHandlers() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmExit();
            }
        });
        
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                togglePause();
            }
        });
        
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmExit();
            }
        });
    }
    
    /**
     * Updates the UI components with current game state
     */
    public void updateUI() {
        gamePanel.repaint();
        
        GameState gameState = gameController.getGameState();
        if (gameState != null) {
            Player player = gameState.getPlayer();
            Level level = gameState.getCurrentLevel();
            
            levelLabel.setText("Level: " + level.getNumber());
            healthLabel.setText("Health: " + player.getHealth());
            scoreLabel.setText("Score: " + player.getScore());
            
            if (gameState.isGameOver()) {
                pauseButton.setEnabled(false);
                pauseButton.setText("Game Over");
            }
        }
    }
    
    /**
     * Toggles the game pause state
     */
    private void togglePause() {
        GameState gameState = gameController.getGameState();
        if (gameState != null) {
            if (paused) {
                gameState.resumeGame();
                pauseButton.setText("Pause");
                paused = false;
            } else {
                gameState.pauseGame();
                pauseButton.setText("Resume");
                paused = true;
            }
        }
    }
    
    /**
     * Confirms exit and returns to main menu
     */
    private void confirmExit() {
        int option = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to exit the game?",
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        
        if (option == JOptionPane.YES_OPTION) {
            GameState gameState = gameController.getGameState();
            if (gameState != null && gameState.isRunning()) {
                gameState.endGame(false);
                
                GameLogger.logEnd(false, player.getScore(), player.getHealth());
            }
            
            MainMenuFrame mainMenuFrame = new MainMenuFrame(user);
            mainMenuFrame.setVisible(true);
            this.dispose();
        }
    }

    /**
     * Sets the game controller and starts the game if the controller is set.
     */
    public void setGameController(GameController gameController) {
        this.gameController = gameController;
        if (this.gamePanel != null) {
            this.gamePanel.setGameController(gameController); 
        }                   
        if (this.gameController != null && this.player != null) {
            this.gameController.startNewGame(this.player);
        }
    }

    /**
     * Gets the feed panel.
     */
    public ShotBoxFeedPanel getFeedPanel() {
        return feedPanel;
    }
} 