package siege.controller;

import java.io.*;
import java.util.*;
import siege.model.GameException;
import siege.model.ScoreEntry;
import siege.util.GameConstants;

/**
 * Controller for managing game scores
 */
public class ScoreController {
    private ArrayList<ScoreEntry> scores;
    
    /**
     * Creates a new score controller
     */
    public ScoreController() {
        this.scores = new ArrayList<>();
        loadScores();
    }
    
    /**
     * Loads scores from the scores file
     */
    private void loadScores() {
        File file = new File(GameConstants.SCORES_FILE);
        
        File dir = file.getParentFile();
        if (dir != null && !dir.exists()) {
            dir.mkdirs();
        }
        
        this.scores.clear(); 

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 3) {
                        try {
                            String username = parts[0];
                            int score = Integer.parseInt(parts[1]);
                            int gameNumber = Integer.parseInt(parts[2]);
                            this.scores.add(new ScoreEntry(username, score, gameNumber));
                        } catch (NumberFormatException e) {
                            System.err.println("Skipping malformed score line: " + line + " - " + e.getMessage());
                        }
                    } else {
                        System.err.println("Skipping malformed score line (incorrect number of parts): " + line);
                    }
                }
            } catch (IOException e) {
                System.err.println("Failed to load scores: " + e.getMessage());
            }
        }
        
        addDefaultLoremIpsumScore();
        sortScores(); 
    }
    
    private void addDefaultLoremIpsumScore() {
        boolean loremIpsumExists = false;
        for (ScoreEntry entry : scores) {
            if ("Lorem Ipsum".equals(entry.getUsername()) && entry.getGameNumber() == 1) {
                loremIpsumExists = true;
                break;
            }
        }
        
        if (!loremIpsumExists) {
            scores.add(new ScoreEntry("Lorem Ipsum", 100, 1));
        }
    }
    
    /**
     * Saves scores to the scores file
     */
    private void saveScores() {
        File file = new File(GameConstants.SCORES_FILE);
        
        File dir = file.getParentFile();
        if (dir != null && !dir.exists()) {
            dir.mkdirs();
        }
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            for (ScoreEntry entry : scores) {
                writer.println(entry.getUsername() + "," + entry.getScore() + "," + entry.getGameNumber());
            }
        } catch (IOException e) {
            System.err.println("Failed to save scores: " + e.getMessage());
        }
    }
    
    /**
     * Adds a new score entry
     */
    public void addScore(String username, int score) {
        int gameNumber = 1;
        for (ScoreEntry entry : scores) {
            if (entry.getUsername().equals(username)) {
                gameNumber++;
            }
        }
        
        ScoreEntry entry = new ScoreEntry(username, score, gameNumber);
        scores.add(entry);
        
        sortScores();
        saveScores();
    }
    
    /**
     * Sorts scores in descending order by score
     * If scores are equal, sort by username alphabetically
     */
    private void sortScores() {
        Collections.sort(scores);
    }
    
    /**
     * Gets all score entries
     */
    public ArrayList<ScoreEntry> getScores() {
        return new ArrayList<>(scores);
    }
    
    /**
     * Gets the top X scores
     */
    public ArrayList<ScoreEntry> getTopScores(int count) {
        if (count >= scores.size()) {
            return new ArrayList<>(scores);
        } else {
            return new ArrayList<>(scores.subList(0, count));
        }
    }
    
    /**
     * Gets all scores for a specific user
     */
    public ArrayList<ScoreEntry> getScoresForUser(String username) {
        ArrayList<ScoreEntry> userScores = new ArrayList<>();
        
        for (ScoreEntry entry : scores) {
            if (entry.getUsername().equals(username)) {
                userScores.add(entry);
            }
        }
        
        return userScores;
    }
    
    /**
     * Clears all scores (for testing)
     */
    public void clearScores() throws GameException {
        scores.clear();
        saveScores();
    }
} 