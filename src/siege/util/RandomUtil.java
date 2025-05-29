package siege.util;

import java.security.SecureRandom;

/**
 * Utility class for secure random number generation
 * Project requirement specifies SecureRandom must be used for randomization
 */
public class RandomUtil {
    // Single instance of SecureRandom for the entire application
    private static final SecureRandom random = new SecureRandom();
    
    /**
     * Gets a random integer between min (inclusive) and max (exclusive)
     */
    public static int getRandomInt(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("Max must be greater than min");
        }
        
        return random.nextInt(max - min) + min;
    }
    
    /**
     * Gets a random integer from 0 (inclusive) to bound (exclusive)
     */
    public static int getRandomInt(int bound) {
        return random.nextInt(bound);
    }
    
    /**
     * Gets a random boolean
     */
    public static boolean getRandomBoolean() {
        return random.nextBoolean();
    }
    
    /**
     * Gets a random double between 0.0 (inclusive) and 1.0 (exclusive)
     */
    public static double getRandomDouble() {
        return random.nextDouble();
    }
    
    /**
     * Returns true with the given probability (0-100)
     */
    public static boolean chance(int percentChance) {
        if (percentChance < 0 || percentChance > 100) {
            throw new IllegalArgumentException("Percent chance must be between 0 and 100");
        }
        
        return random.nextInt(100) < percentChance;
    }
    
    /**
     * Gets the SecureRandom instance directly
     */
    public static SecureRandom getRandom() {
        return random;
    }
} 