package siege.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;
import javax.swing.Timer;
import siege.util.GameConstants;

/**
 * Represents a Knowledge Keeper enemy in the game
 */
public class KnowledgeKeeper extends Actor {
    private KeeperType type;
    private String name;
    private Timer shootTimer;
    private int moveDirection; 
    
    private SecureRandom random;
    
    /**
     * Creates a new Knowledge Keeper
     */
    public KnowledgeKeeper(String name, KeeperType type, double x, double y, int width, int height, 
                          BufferedImage sprite) {
        super(x, y, width, height, sprite);
        this.name = name;
        this.type = type;
        this.random = new SecureRandom();
        
        this.moveDirection = random.nextBoolean() ? 1 : -1;
    }
    
    /**
     * Sets up the shooting timer for this keeper
     */
    public void setupShootTimer(ActionListener shootAction) {
        int delay = 3000 + random.nextInt(4000); 
        
        shootTimer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int roll = random.nextInt(100);
                if (roll < type.getQuestionChance() + type.getInfoChance()) {
                    shootAction.actionPerformed(e);
                }
                
                int newDelay = 3000 + random.nextInt(4000);
                shootTimer.setDelay(newDelay);
            }
        });
        
        shootTimer.start();
    }
    
    /**
     * Updates the keeper's position and behavior
     */
    @Override
    public void update() {
        x += moveDirection * type.getMoveSpeed();
        
        if (x <= 0) {
            x = 0;
            moveDirection = 1; 
        } else if (x >= GameConstants.GAME_WIDTH - width) {
            x = GameConstants.GAME_WIDTH - width;
            moveDirection = -1; 
        } 
        else if (random.nextInt(100) < 5) {
            moveDirection *= -1;
        }
        
        super.update();
    }
    
    /**
     * Updates keeper movement considering player position
     */
    public void updateWithPlayerTracking(Player player, int screenWidth) {
        if (type == KeeperType.TEACHING_ASSISTANT && random.nextInt(100) < 15) {
            moveTowardPlayer(player);
        } 
        else if (type == KeeperType.PROFESSOR && random.nextInt(100) < 30) {
            moveTowardPlayer(player);
        } 
        else {
            update();
        }
    }
    
    /**
     * Move toward player's position
     */
    private void moveTowardPlayer(Player player) {

        double playerCenterX = player.getX() + (player.getWidth() / 2.0);
        double targetOffsetX = (random.nextDouble() * 60.0) - 30.0; 
        double targetX = playerCenterX + targetOffsetX - (this.width / 2.0); 

        if (targetX < x) {
            moveDirection = -1;
        } else {
            moveDirection = 1;
        }
        
        double trackingSpeed = type.getMoveSpeed() * 0.2;
        x += moveDirection * trackingSpeed;
        
        if (x <= 0) {
            x = 0;
        } else if (x >= GameConstants.GAME_WIDTH - width) {
            x = GameConstants.GAME_WIDTH - width;
        }
        
        super.update();
    }
    
    /**
     * Stops the shooting timer
     */
    public void stopTimer() {
        if (shootTimer != null) {
            shootTimer.stop();
        }
    }
    
    /**
     * Determines if this keeper will shoot a question
     */
    public boolean willShootQuestion() {
        int roll = random.nextInt(100);
        return roll < type.getQuestionChance();
    }
    
    public KeeperType getType() {
        return type;
    }
    
    public String getName() {
        return name;
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);

        Color originalColor = g.getColor();

        Color borderColor = null;
        switch (this.type) {
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

        if (borderColor != null && this.boundingBox != null) {
            g.setColor(borderColor);
            g.drawRect(this.boundingBox.x, this.boundingBox.y, this.boundingBox.width, this.boundingBox.height);
            g.setColor(originalColor);
        }
    }
} 