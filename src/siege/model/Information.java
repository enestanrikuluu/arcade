package siege.model;

import java.awt.image.BufferedImage;

/**
 * Represents information shot by a Knowledge Keeper
 * These give points to the player when collected
 */
public class Information extends ShotBox {
    /**
     * Creates a new Information
     */
    public Information(KnowledgeKeeper source, String text, double x, double y, int width, int height, 
                      double speed, BufferedImage sprite) {
        super(source, text, x, y, width, height, speed, sprite);
    }
    
    /**
     * Gives points to the player based on the source keeper's type
     */
    @Override
    public void applyEffect(Player player) {
        int points = source.getType().getInfoPoints();
        player.addScore(points);
    }
} 