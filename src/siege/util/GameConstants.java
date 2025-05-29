package siege.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Constants for the game
 */
public class GameConstants {
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;
    
    public static final int GAME_WIDTH = 600;
    public static final int GAME_HEIGHT = 500;
    
    public static final int PLAYER_WIDTH = 40;
    public static final int PLAYER_HEIGHT = 50;
    public static final double PLAYER_SPEED = 5.0;
    public static final int PLAYER_START_X = GAME_WIDTH / 2 - PLAYER_WIDTH / 2;
    public static final int PLAYER_START_Y = GAME_HEIGHT - PLAYER_HEIGHT - 10;
    public static final int PLAYER_START_HEALTH = 100;
    
    public static final int KEEPER_WIDTH = 40;
    public static final int KEEPER_HEIGHT = 50;
    public static final int KEEPER_START_Y = 30;
    public static final int KEEPER_SPACING = 100;
    
    public static final int SHOTBOX_WIDTH = 30;
    public static final int SHOTBOX_HEIGHT = 30;
    public static final double QUESTION_SPEED = 3.0;
    public static final double INFO_SPEED = 2.0;
    
    public static final int LEVEL1_TARGET_SCORE = 50;
    public static final int LEVEL2_TARGET_SCORE = 100;
    public static final int LEVEL3_TARGET_SCORE = 150;
    
    public static final int FRAME_DELAY = 16; 
    
    public static final String QUESTIONS_FILE = "src/resources/data/questions.txt";
    public static final String INFO_FILE = "src/resources/data/info.txt";
    public static final String USERS_DIR = "src/resources/data/users/";
    public static final String USER_FILE_EXTENSION = ".ser";
    public static final String SCORES_FILE = "src/resources/data/scores.txt";
    public static final String IMAGES_DIR = "src/resources/images/";
    
    public static final String DEFAULT_PLAYER_IMAGE = IMAGES_DIR + "player.png";
    
    public static final List<String> TA_IMAGES;
    public static final List<String> SL_IMAGES;
    public static final List<String> PROF_IMAGES;

    static {
        TA_IMAGES = loadImagePathsFromDirectory(IMAGES_DIR + "TA/");
        SL_IMAGES = loadImagePathsFromDirectory(IMAGES_DIR + "SL/");
        PROF_IMAGES = loadImagePathsFromDirectory(IMAGES_DIR + "Prof/");
    }

    public static final String QUESTION_IMAGE = IMAGES_DIR + "question.png";
    public static final String INFO_IMAGE = IMAGES_DIR + "info.png";
    public static final String BACKGROUND_IMAGE = IMAGES_DIR + "background.png";
    
    public static final int KEY_LEFT = 37;  
    public static final int KEY_RIGHT = 39; 
    public static final int KEY_H = 72;     
    public static final int KEY_ESC = 27;   

    private static List<String> loadImagePathsFromDirectory(String directoryPath) {
        List<String> imagePaths = new ArrayList<>();
        File dir = new File(directoryPath);

        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".png"));
            if (files != null) {
                Arrays.sort(files, (f1, f2) -> f1.getName().compareTo(f2.getName()));
                for (File file : files) {
                    imagePaths.add(directoryPath + file.getName());
                }
            } else {
                System.err.println("Warning: No PNG files found or error reading directory: " + directoryPath);
            }
        } else {
            System.err.println("Warning: Image directory not found or is not a directory: " + directoryPath);
        }
        return Collections.unmodifiableList(imagePaths);
    }
    
    private GameConstants() {
    }
} 