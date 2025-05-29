package siege.model;

/**
 * Enum representing different types of Knowledge Keepers
 */
public enum KeeperType {
    SECTION_LEADER(3, 30, 70, 10, 10),
    
    TEACHING_ASSISTANT(4, 50, 50, 20, 20),
    
    PROFESSOR(5, 70, 30, 30, 30);
    
    private final double moveSpeed;
    private final int questionChance; 
    private final int infoChance;     
    private final int damage;
    private final int infoPoints;
    
    /**
     * Creates a new KeeperType with the specified properties
     */
    KeeperType(double moveSpeed, int questionChance, int infoChance, int damage, int infoPoints) {
        this.moveSpeed = moveSpeed;
        this.questionChance = questionChance;
        this.infoChance = infoChance;
        this.damage = damage;
        this.infoPoints = infoPoints;
    }
    
    public double getMoveSpeed() {
        return moveSpeed;
    }
    
    public int getQuestionChance() {
        return questionChance;
    }
    
    public int getInfoChance() {
        return infoChance;
    }
    
    public int getDamage() {
        return damage;
    }
    
    public int getInfoPoints() {
        return infoPoints;
    }
    
    /**
     * Returns a string representation of the keeper type
     */
    @Override
    public String toString() {
        switch (this) {
            case SECTION_LEADER:
                return "Section Leader";
            case TEACHING_ASSISTANT:
                return "Teaching Assistant";
            case PROFESSOR:
                return "Professor";
            default:
                return "Unknown";
        }
    }
} 