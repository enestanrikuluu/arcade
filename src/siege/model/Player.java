package siege.model;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * Represents the player character in the game
 */
public class Player extends Actor {
    private int health;
    private int score;
    private boolean hidden;
    private String username;
    
    private static final int STARTING_HEALTH = 100;
    private static final int PLAYER_SPEED = 5;
    
    /**
     * Creates a new player with a username and sprite
     */
    public Player(String username, int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);
        this.username = username;
        this.health = STARTING_HEALTH;
        this.score = 0;
        this.hidden = false;
    }
    
    /**
     * Moves the player left
     */
    public void moveLeft(int screenWidth) {
        if (x > 0) {
            x -= PLAYER_SPEED;
            update();
        }
    }
    
    /**
     * Moves the player right
     */
    public void moveRight(int screenWidth) {
        if (x < screenWidth - width) {
            x += PLAYER_SPEED;
            update();
        }
    }
    
    /**
     * Toggles player visibility
     */
    public void toggleHidden() {
        this.hidden = !this.hidden;
    }
    
    /**
     * Decreases player health by the specified amount
     */
    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health < 0) {
            this.health = 0;
        }
    }
    
    /**
     * Increases player score by the specified amount
     */
    public void addScore(int points) {
        this.score += points;
    }
    
    /**
     * Draws the player on the screen if not hidden
     */
    @Override
    public void draw(Graphics g) {
        if (!hidden) {
            super.draw(g);
        }
    }
    
    /**
     * Checks if the player is alive
     */
    public boolean isAlive() {
        return health > 0;
    }
    
    public int getHealth() {
        return health;
    }
    
    public int getScore() {
        return score;
    }
    
    public boolean isHidden() {
        return hidden;
    }
    
    public String getUsername() {
        return username;
    }
} 