package siege.model;

/**
 * Custom exception class for game-related exceptions
 */
public class GameException extends RuntimeException {
    
    /**
     * Creates a new GameException with a message
     */
    public GameException(String message) {
        super(message);
    }
    
    /**
     * Creates a new GameException with a message and cause
     */
    public GameException(String message, Throwable cause) {
        super(message, cause);
    }
} 