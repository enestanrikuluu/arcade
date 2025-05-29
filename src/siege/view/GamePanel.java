package siege.view;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.*;
import siege.controller.GameController;
import siege.model.*;
import siege.util.GameConstants;
import siege.util.ImageLoader;

/**
 * Panel for rendering the game
 */
public class GamePanel extends JPanel {
    private GameController gameController;
    private BufferedImage backgroundImage;
    
    /**
     * Creates a new game panel
     */
    public GamePanel() {
        try {
            this.backgroundImage = ImageLoader.loadImage(GameConstants.BACKGROUND_IMAGE);
        } catch (GameException e) {
            System.err.println("Failed to load background image: " + e.getMessage());
            this.backgroundImage = null; 
        }
        
        setPreferredSize(new Dimension(GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setFocusable(true);
        
        setDoubleBuffered(true);
        
    }
    
    /**
     * Sets the game controller and initializes dependent components.
     */
    public void setGameController(GameController gameController) {
        this.gameController = gameController;
        if (this.gameController != null) {
            addKeyListener();
        }
    }
    
    /**
     * Adds keyboard listener for controlling the player
     */
    private void addKeyListener() {
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (gameController == null) return;
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_LEFT) {
                    gameController.setMoveLeftActive(true);
                }
                if (keyCode == KeyEvent.VK_RIGHT) {
                    gameController.setMoveRightActive(true);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (gameController == null) return;
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_LEFT) {
                    gameController.setMoveLeftActive(false);
                }
                if (keyCode == KeyEvent.VK_RIGHT) {
                    gameController.setMoveRightActive(false);
                }
            }
        });

        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();
        
        inputMap.remove(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0));
        actionMap.remove("left");
        inputMap.remove(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0));
        actionMap.remove("right");

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_H, 0), "hide");
        actionMap.put("hide", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameController != null) { 
                    gameController.togglePlayerHidden();
                }
            }
        });
    }
    
    /**
     * Paints the game components
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(Color.DARK_GRAY); 
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        
        GameState gameState = gameController.getGameState();
        
        if (gameState == null) {
            drawCenteredText(g, "Game not started", getWidth() / 2, getHeight() / 2);
            return;
        }
        
        Player player = gameState.getPlayer();
        player.draw(g);
        
        Level level = gameState.getCurrentLevel();
        ArrayList<KnowledgeKeeper> keepers = level.getKeepers();
        for (KnowledgeKeeper keeper : keepers) {
            keeper.draw(g);
        }
        
        ArrayList<ShotBox> shotBoxes = gameState.getActiveShotBoxes();
        for (ShotBox box : shotBoxes) {
            box.draw(g);
        }
        
        drawPlayerStats(g, player);
        
        drawLevelInfo(g, level);
        
        if (gameState.isGameOver()) {
            drawGameOver(g, gameState.isVictory());
        }
    }
    
    /**
     * Draws text centered at a position
     */
    private void drawCenteredText(Graphics g, String text, int x, int y) {
        FontMetrics metrics = g.getFontMetrics();
        int textWidth = metrics.stringWidth(text);
        g.drawString(text, x - textWidth / 2, y);
    }
    
    /**
     * Draws player stats (health and score)
     */
    private void drawPlayerStats(Graphics g, Player player) {
        Font originalFont = g.getFont();
        g.setFont(new Font("Arial", Font.BOLD, 14));
        
        g.setColor(Color.RED);
        g.drawString("Health: " + player.getHealth(), 10, 20);
        
        g.setColor(Color.BLUE);
        g.drawString("Score: " + player.getScore(), 10, 40);
        
        g.setFont(originalFont);
    }
    
    /**
     * Draws level information
     */
    private void drawLevelInfo(Graphics g, Level level) {
        Font originalFont = g.getFont();
        g.setFont(new Font("Arial", Font.BOLD, 14));

        g.setColor(Color.BLACK);
        g.drawString("Level: " + level.getNumber(), getWidth() - 100, 20);
        
        g.setColor(Color.GREEN.darker());
        g.drawString("Target: " + level.getTargetScore(), getWidth() - 100, 40);
        
        g.setFont(originalFont);
    }
    
    /**
     * Draws game over message
     */
    private void drawGameOver(Graphics g, boolean victory) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, getWidth(), getHeight());
        
        String message = victory ? "Victory!" : "Game Over";
        
        Font originalFont = g.getFont();
        g.setFont(new Font("Arial", Font.BOLD, 48));
        
        g.setColor(Color.BLACK);
        drawCenteredText(g, message, getWidth() / 2 + 2, getHeight() / 2 + 2);
        
        g.setColor(victory ? Color.GREEN : Color.RED);
        drawCenteredText(g, message, getWidth() / 2, getHeight() / 2);
        
        g.setFont(originalFont);
    }
} 