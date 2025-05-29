package siege.model;

import java.util.ArrayList;

/**
 * Manages the overall state of the game
 */
public class GameState {
    private Player player;
    private Level currentLevel;
    private ArrayList<ShotBox> activeShotBoxes;
    
    private boolean running;
    private boolean gameOver;
    private boolean victory;
    private long startTime;
    
    public static final int LEVEL1_TARGET = 50;
    public static final int LEVEL2_TARGET = 100;
    public static final int LEVEL3_TARGET = 150;
    
    /**
     * Creates a new game state
     */
    public GameState(Player player) {
        this.player = player;
        this.activeShotBoxes = new ArrayList<>();
        this.running = false;
        this.gameOver = false;
        this.victory = false;
    }
    
    /**
     * Starts the game
     */
    public void startGame() {
        running = true;
        startTime = System.currentTimeMillis();
    }
    
    /**
     * Pauses the game
     */
    public void pauseGame() {
        running = false;
    }
    
    /**
     * Resumes the game
     */
    public void resumeGame() {
        running = true;
    }
    
    /**
     * Ends the game
     */
    public void endGame(boolean win) {
        running = false;
        gameOver = true;
        victory = win;
    }
    
    /**
     * Updates the game state
     */
    public void update() {
        if (!running) {
            return;
        }
        
        if (!player.isAlive()) {
            endGame(false);
            return;
        }
        
        if (currentLevel.getNumber() == 3 && player.getScore() >= LEVEL3_TARGET) {
            endGame(true);
            return;
        }
        
        if (player.getScore() >= currentLevel.getTargetScore()) {
            return; 
        }
        
        ArrayList<ShotBox> boxesToRemove = new ArrayList<>();
        for (ShotBox box : activeShotBoxes) {
            box.update();
            if (box.isOutOfBounds(600)) { 
                boxesToRemove.add(box);
            }
        }
        activeShotBoxes.removeAll(boxesToRemove);
    }
    
    /**
     * Adds a ShotBox to the game
     */
    public void addShotBox(ShotBox box) {
        activeShotBoxes.add(box);
    }
    
    /**
     * Removes a ShotBox from the game
     */
    public void removeShotBox(ShotBox box) {
        activeShotBoxes.remove(box);
    }
    
    /**
     * Sets the current level
     */
    public void setCurrentLevel(Level level) {
        this.currentLevel = level;
    }
    
    // Getters
    public Player getPlayer() {
        return player;
    }
    
    public Level getCurrentLevel() {
        return currentLevel;
    }
    
    public ArrayList<ShotBox> getActiveShotBoxes() {
        return activeShotBoxes;
    }
    
    public boolean isRunning() {
        return running;
    }
    
    public boolean isGameOver() {
        return gameOver;
    }
    
    public boolean isVictory() {
        return victory;
    }
    
    public long getStartTime() {
        return startTime;
    }
    
    public long getElapsedTimeSeconds() {
        return (System.currentTimeMillis() - startTime) / 1000;
    }
} 