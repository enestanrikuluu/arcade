package siege.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Represents a score entry for the scoreboard
 */
public class ScoreEntry implements Serializable, Comparable<ScoreEntry> {
    private String username;
    private int score;
    private int gameNumber;
    private Date timestamp;
    
    /**
     * Creates a new score entry
     */
    public ScoreEntry(String username, int score, int gameNumber) {
        this.username = username;
        this.score = score;
        this.gameNumber = gameNumber;
        this.timestamp = new Date();
    }
    
    // Getters
    public String getUsername() {
        return username;
    }
    
    public int getScore() {
        return score;
    }
    
    public int getGameNumber() {
        return gameNumber;
    }
    
    public Date getTimestamp() {
        return timestamp;
    }
    
    /**
     * Returns a string representation of the score entry
     */
    @Override
    public String toString() {
        return "Game" + gameNumber + " by " + username + ": " + score;
    }
    
    /**
     * Compares this score entry with another
     * For sorting in descending order of score
     */
    @Override
    public int compareTo(ScoreEntry other) {
        int scoreCompare = Integer.compare(other.score, this.score);
        
        if (scoreCompare == 0) {
            int userCompare = this.username.compareTo(other.username);
            if (userCompare == 0) {
                // If scores and usernames are the same, sort by game number (ascending)
                return Integer.compare(this.gameNumber, other.gameNumber);
            }
            return userCompare;
        }
        
        return scoreCompare;
    }
} 