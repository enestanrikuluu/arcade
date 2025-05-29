package siege.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * Abstract class representing a projectile shot by Knowledge Keepers
 */
public abstract class ShotBox {
    protected double x;
    protected double y;
    protected int width;
    protected int height;
    protected double speed;
    protected BufferedImage sprite;
    protected String text;
    protected Rectangle boundingBox;
    protected KnowledgeKeeper source;
    protected boolean added;
    
    /**
     * Creates a new ShotBox
     */
    public ShotBox(KnowledgeKeeper source, String text, double x, double y, int width, int height, 
                  double speed, BufferedImage sprite) {
        this.source = source;
        this.text = text;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.sprite = sprite;
        
        this.boundingBox = new Rectangle((int)x, (int)y, width, height);
        this.added = false;
    }
    
    /**
     * Updates the position of the ShotBox
     */
    public void update() {
        y += speed;
        
        boundingBox.x = (int)x;
        boundingBox.y = (int)y;
    }
    
    /**
     * Draws the ShotBox on the screen
     */
    public void draw(Graphics g) {
        g.drawImage(sprite, (int)x, (int)y, width, height, null);

        Color originalColor = g.getColor();
        Color borderColor = null;

        if (this.source != null) {
            KeeperType sourceType = this.source.getType();
            switch (sourceType) {
                case SECTION_LEADER:
                    borderColor = Color.GREEN;
                    break;
                case TEACHING_ASSISTANT:
                    borderColor = Color.YELLOW;
                    break;
                case PROFESSOR:
                    borderColor = Color.RED;
                    break;
                default:
                    break;
            }
        }

        if (borderColor != null) {
            g.setColor(borderColor);
            g.drawRect((int)x, (int)y, width, height);
            g.setColor(originalColor);
        }
    }
    
    /**
     * Checks if this ShotBox collides with a player
     */
    public boolean collidesWith(Player player) {
        return boundingBox.intersects(player.getBoundingBox());
    }
    
    /**
     * Applies the effect of this ShotBox to the player
     */
    public abstract void applyEffect(Player player);
    
    /**
     * Checks if the ShotBox is out of the screen
     */
    public boolean isOutOfBounds(int screenHeight) {
        return y > screenHeight;
    }
    
    /**
     * Checks if this ShotBox has been added to the feed
     */
    public boolean isAdded() {
        return added;
    }
    
    /**
     * Sets the added flag
     */
    public void setAdded(boolean added) {
        this.added = added;
    }
    
    public String getText() {
        return text;
    }
    
    public KnowledgeKeeper getSource() {
        return source;
    }
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    public Rectangle getBoundingBox() {
        return boundingBox;
    }
} 