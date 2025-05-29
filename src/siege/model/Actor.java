package siege.model;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * Base class for all game entities that appear on the screen
 */
public abstract class Actor {
    protected double x;
    protected double y;
    protected int width;
    protected int height;
    
    protected BufferedImage sprite;
    
    protected Rectangle boundingBox;
    
    /**
     * Creates a new Actor with position, size and sprite
     */
    public Actor(double x, double y, int width, int height, BufferedImage sprite) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.sprite = sprite;
        
        this.boundingBox = new Rectangle((int)x, (int)y, width, height);
    }
    
    /**
     * Updates actor position
     */
    public void update() {
        this.boundingBox.x = (int)this.x;
        this.boundingBox.y = (int)this.y;
    }
    
    /**
     * Draws the actor on the screen
     */
    public void draw(Graphics g) {
        g.drawImage(sprite, (int)x, (int)y, width, height, null);
    }
    
    /**
     * Checks if this actor collides with another actor
     */
    public boolean collidesWith(Actor other) {
        return this.boundingBox.intersects(other.boundingBox);
    }
    
    public double getX() {
        return x;
    }
    
    public void setX(double x) {
        this.x = x;
    }
    
    public double getY() {
        return y;
    }
    
    public void setY(double y) {
        this.y = y;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public Rectangle getBoundingBox() {
        return boundingBox;
    }
} 