package siege.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import siege.model.GameException;

/**
 * Handles logging of game events to a file
 */
public class GameLogger {
    private static final String LOG_FILE = "game.log";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    
    /**
     * Logs a message to the log file
     */
    public static void log(String type, String message) {
        try {
            File logFile = new File(LOG_FILE);
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            
            try (PrintWriter writer = new PrintWriter(new FileWriter(logFile, true))) {
                String timestamp = DATE_FORMAT.format(new Date());
                writer.println(timestamp + " " + type + " " + message);
            }
        } catch (IOException e) {
            System.err.println("Failed to write to log file: " + e.getMessage());
        }
    }
    
    /**
     * Logs game start
     */
    public static void logStart(String username) {
        log("START", "user=" + username);
    }
    
    /**
     * Logs collection of information
     */
    public static void logInfo(int points, int total) {
        log("INFO", "+" + points + " pts (total " + total + ")");
    }
    
    /**
     * Logs damage taken
     */
    public static void logDamage(int damage, int remainingHealth) {
        log("DMG", "-" + damage + " hp (remain " + remainingHealth + ")");
    }
    
    /**
     * Logs level transition
     */
    public static void logLevelTransition(int fromLevel, int toLevel) {
        log("LEVEL", fromLevel + "→" + toLevel);
    }
    
    /**
     * Logs game end
     */
    public static void logEnd(boolean victory, int score, int health) {
        String result = victory ? "victory" : "defeat";
        log("END", result + " score=" + score + " hp=" + health);
    }
    
    /**
     * Logs player movement restriction
     */
    public static void logMovementRestriction() {
        log("MOVE", "User is unable to move.");
    }
    
    /**
     * Clears the log file 
     */
    public static void clearLog() throws GameException {
        try {
            File logFile = new File(LOG_FILE);
            if (logFile.exists()) {
                try (PrintWriter writer = new PrintWriter(new FileWriter(logFile, false))) {
                }
            }
        } catch (IOException e) {
            throw new GameException("Failed to clear log file: " + e.getMessage(), e);
        }
    }
} 