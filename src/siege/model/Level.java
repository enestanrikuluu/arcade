package siege.model;

import java.util.ArrayList;

/**
 * Represents a game level with its difficulty and requirements
 */
public class Level {
    private int number;
    private int targetScore;
    private ArrayList<KnowledgeKeeper> keepers;
    
    /**
     * Creates a new level
     */
    public Level(int number, int targetScore) {
        this.number = number;
        this.targetScore = targetScore;
        this.keepers = new ArrayList<>();
    }
    
    /**
     * Adds a keeper to this level
     */
    public void addKeeper(KnowledgeKeeper keeper) {
        keepers.add(keeper);
    }
    
    /**
     * Gets the level number
     */
    public int getNumber() {
        return number;
    }
    
    /**
     * Gets the target score required to complete this level
     */
    public int getTargetScore() {
        return targetScore;
    }
    
    /**
     * Gets all keepers in this level
     */
    public ArrayList<KnowledgeKeeper> getKeepers() {
        return keepers;
    }
} 