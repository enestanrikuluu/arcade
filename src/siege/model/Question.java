package siege.model;

import java.awt.image.BufferedImage;

/**
 * Represents a question shot by a Knowledge Keeper
 * These deal damage to the player when hit
 */
public class Question extends ShotBox {
    /**
     * Creates a new Question
     */
    public Question(KnowledgeKeeper source, String text, double x, double y, int width, int height, 
                   double speed, BufferedImage sprite) {
        super(source, text, x, y, width, height, speed, sprite);
    }
    
    /**
     * Applies damage to the player based on the source keeper's type
     */
    @Override
    public void applyEffect(Player player) {
        int damage = source.getType().getDamage();
        player.takeDamage(damage);
    }
} 