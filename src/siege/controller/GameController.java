package siege.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import siege.io.GameLogger;
import siege.model.*;
import siege.util.GameConstants;
import siege.util.ImageLoader;
import siege.util.RandomUtil;
import siege.view.GameFrame;

/**
 * Main controller for the game
 */
public class GameController implements ActionListener {
    // Game state
    private GameState gameState;
    private ContentRepository contentRepo;
    private GameFrame gameFrame;
    
    // Pre-loaded sprites
    private BufferedImage questionSprite;
    private BufferedImage infoSprite;
    
    // Player movement flags
    private boolean moveLeftActive = false;
    private boolean moveRightActive = false;
    
    // User playing the game
    private User user;
    
    // Controllers
    private ScoreController scoreController;
    
    // Game loop timer
    private Timer gameLoopTimer;
    
    // Level information
    private int currentLevelNumber;
    
    // Lists for available images, shuffled for variety
    private List<String> availableSlImages;
    private List<String> availableTaImages;
    private List<String> availableProfImages;

    // Lists for active keepers carried over between levels
    private List<KnowledgeKeeper> activeSLs;
    private List<KnowledgeKeeper> activeTAs;
    
    /**
     * Creates a new game controller
     */
    public GameController(User user, GameFrame gameFrame) {
        this.user = user;
        this.gameFrame = gameFrame;
        this.contentRepo = new ContentRepository();
        this.scoreController = new ScoreController();
        
        // Initialize keeper name pools and active keeper lists
        this.activeSLs = new ArrayList<>();
        this.activeTAs = new ArrayList<>();
        
        // Initialize available image lists (done in startNewGame to reset per game)

        // Load content
        try {
            contentRepo.loadContent(GameConstants.QUESTIONS_FILE, GameConstants.INFO_FILE);
        } catch (GameException e) {
            System.err.println("Failed to load content: " + e.getMessage());
        }

        // Pre-load ShotBox sprites
        try {
            questionSprite = ImageLoader.loadImage(GameConstants.QUESTION_IMAGE);
            infoSprite = ImageLoader.loadImage(GameConstants.INFO_IMAGE);
        } catch (GameException e) {
            System.err.println("Failed to load ShotBox sprites: " + e.getMessage());
            questionSprite = ImageLoader.createPlaceholderImage(GameConstants.SHOTBOX_WIDTH, GameConstants.SHOTBOX_HEIGHT, "?");
            infoSprite = ImageLoader.createPlaceholderImage(GameConstants.SHOTBOX_WIDTH, GameConstants.SHOTBOX_HEIGHT, "i");
        }
    }
    
    private void initializeAvailableImageLists() {
        availableSlImages = new ArrayList<>(GameConstants.SL_IMAGES);
        Collections.shuffle(availableSlImages, RandomUtil.getRandom());
        availableTaImages = new ArrayList<>(GameConstants.TA_IMAGES);
        Collections.shuffle(availableTaImages, RandomUtil.getRandom());
        availableProfImages = new ArrayList<>(GameConstants.PROF_IMAGES);
        Collections.shuffle(availableProfImages, RandomUtil.getRandom());
    }
    
    /**
     * Starts a new game
     */
    public void startNewGame(Player player) {
        // Setup game state
        gameState = new GameState(player);
        currentLevelNumber = 1;

        // Reset active keeper lists for the new game
        this.activeSLs.clear();
        this.activeTAs.clear();
        
        // Re-initialize keeper name pools and available image lists
        initializeAvailableImageLists();

        // Initialize level 1
        initializeLevel(currentLevelNumber);
        
        // Start the game
        gameState.startGame();
        
        // Setup game loop timer
        gameLoopTimer = new Timer(GameConstants.FRAME_DELAY, this);
        gameLoopTimer.start();
        
        // Log game start
        GameLogger.logStart(user.getUsername());
    }
    
    /**
     * Initializes a level
     */
    private void initializeLevel(int levelNumber) {
        // Create the level
        Level level = new Level(levelNumber, getLevelTargetScore(levelNumber));
        
        // Add keepers based on level number
        try {
            addKeepersToLevel(level, levelNumber);
        } catch (GameException e) {
            System.err.println("Failed to add keepers to level: " + e.getMessage());
        }
        
        // Set current level
        gameState.setCurrentLevel(level);
    }
    
    /**
     * Gets the target score for a level
     */
    private int getLevelTargetScore(int level) {
        switch (level) {
            case 1:
                return GameConstants.LEVEL1_TARGET_SCORE;
            case 2:
                return GameConstants.LEVEL2_TARGET_SCORE;
            case 3:
                return GameConstants.LEVEL3_TARGET_SCORE;
            default:
                return 0;
        }
    }
    
    /**
     * Adds keepers to a level, managing them cumulatively.
     */
    private void addKeepersToLevel(Level level, int levelNumber) throws GameException {
        List<KnowledgeKeeper> keepersForThisLevel = new ArrayList<>();

        // Add existing SLs (unless it's level 3) and TAs from previous levels
        if (levelNumber != 3) {
            keepersForThisLevel.addAll(activeSLs);
        }
        keepersForThisLevel.addAll(activeTAs); // TAs are still carried over to Level 3

        switch (levelNumber) {
            case 1:
                // Add 4 new, unique SLs. activeSLs should be empty here.
                activeSLs.clear(); // Ensure it's empty before populating for level 1
                addNewKeepers(KeeperType.SECTION_LEADER, 4, availableSlImages, activeSLs, keepersForThisLevel);
                break;
            case 2:
                // Add 2 new, unique TAs. activeTAs should be empty here if it's the first time reaching L2.
                activeTAs.clear(); // Ensure it's empty before populating for level 2
                addNewKeepers(KeeperType.TEACHING_ASSISTANT, 2, availableTaImages, activeTAs, keepersForThisLevel);
                break;
            case 3:
                // Add 2 new, unique Professors.
                // Professors are not added to an "activeProfs" list as they don't carry over.
                List<KnowledgeKeeper> newProfs = new ArrayList<>(); // Temporary list for profs this level
                addNewKeepers(KeeperType.PROFESSOR, 2, availableProfImages, newProfs, keepersForThisLevel);
                break;
        }

        // Now, arrange all keepers for this level and add them to the Level object
        for (int i = 0; i < keepersForThisLevel.size(); i++) {
            KnowledgeKeeper keeper = keepersForThisLevel.get(i);
            // Assuming KnowledgeKeeper has setX and setY or that createKeeper re-positions if called again
            // For simplicity, we'll re-calculate X and set Y, assuming these setters exist or are handled
            keeper.setX(calculateKeeperX(i, keepersForThisLevel.size()));
            keeper.setY(GameConstants.KEEPER_START_Y); // Ensure Y is reset/correct
            if (!level.getKeepers().contains(keeper)) { // Avoid re-adding if already carried over and in list
                level.addKeeper(keeper);
            }
            // Ensure timer is (re)started only if the keeper is newly added or needs re-setup
            // This might need more nuanced handling if keepers can be in keepersForThisLevel but not yet in current level's keeper list.
            // For simplicity, always setup/resetup timer. If keeper was carried, its old timer was stopped in advanceLevel.
            setupKeeperTimer(keeper);
        }
    }

    // Renamed and refactored from addNewKeepersToList
    private void addNewKeepers(KeeperType type, int count, 
                               List<String> availableImages, List<KnowledgeKeeper> activeListForType, 
                               List<KnowledgeKeeper> allKeepersForCurrentLevelLayout) throws GameException {
        int keepersAdded = 0;
        for (int i = 0; i < count && !availableImages.isEmpty(); i++) {
            String imagePath = availableImages.remove(0); // Get and remove first available image
            String name;

            // Extract name from imagePath (e.g., "SL1-Recep.png" -> "Recep")
            try {
                File imageFile = new File(imagePath);
                String fileName = imageFile.getName();
                int dotIndex = fileName.lastIndexOf('.');
                String nameWithoutExtension = (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
                
                int hyphenIndex = nameWithoutExtension.indexOf('-');
                if (hyphenIndex == -1 || hyphenIndex + 1 >= nameWithoutExtension.length()) {
                    // Check if there's any character after hyphen, if hyphen exists
                    throw new IllegalArgumentException("Filename format error: does not contain '-' followed by a name, or name part is empty.");
                }
                name = nameWithoutExtension.substring(hyphenIndex + 1);
                if (name.trim().isEmpty()) {
                     throw new IllegalArgumentException("Extracted name is empty or consists only of whitespace.");
                }
            } catch (Exception e) {
                System.err.println("Warning: Could not extract name from image path: '" + imagePath + "'. Using fallback name. Error: " + e.getMessage());
                name = type.toString() + "_Fallback_" + (keepersAdded + 1); 
            }
            
            KnowledgeKeeper keeper = createKeeper(name, type, 0, GameConstants.KEEPER_START_Y, imagePath);
            
            activeListForType.add(keeper); 
            allKeepersForCurrentLevelLayout.add(keeper);
            keepersAdded++;
        }
        if (keepersAdded < count) {
            System.err.println("Warning: Could not add " + count + " " + type + " keepers. Added " + keepersAdded + " due to insufficient unique images.");
        }
    }
    
    /**
     * Creates a knowledge keeper
     */
    private KnowledgeKeeper createKeeper(String name, KeeperType type, double x, double y, String imagePath) throws GameException {
        BufferedImage sprite;
        try {
            if (imagePath != null && !imagePath.isEmpty()) {
                sprite = ImageLoader.loadImage(imagePath);
            } else {
                System.err.println("Warning: No image path provided for keeper " + name + ". Using placeholder.");
                sprite = ImageLoader.createPlaceholderImage(GameConstants.KEEPER_WIDTH, GameConstants.KEEPER_HEIGHT, name);
            }
        } catch (GameException e) {
            System.err.println("Failed to load image " + imagePath + " for keeper " + name + ". Using placeholder. Error: " + e.getMessage());
            sprite = ImageLoader.createPlaceholderImage(GameConstants.KEEPER_WIDTH, GameConstants.KEEPER_HEIGHT, name);
        }
        return new KnowledgeKeeper(name, type, x, y, GameConstants.KEEPER_WIDTH, GameConstants.KEEPER_HEIGHT, sprite);
    }
    
    /**
     * Sets up the timer for a keeper
     */
    private void setupKeeperTimer(KnowledgeKeeper keeper) {
        keeper.setupShootTimer(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createShotBox(keeper);
            }
        });
    }
    
    /**
     * Creates a ShotBox from a keeper
     */
    private void createShotBox(KnowledgeKeeper keeper) {
        // Check if game is running
        if (gameState == null || !gameState.isRunning()) {
            return;
        }
        
        // Get keeper type
        KeeperType type = keeper.getType();
        
        // Determine if this is a question or information
        boolean isQuestion = keeper.willShootQuestion();
        
        // Get content
        String content = isQuestion ? 
                contentRepo.getRandomQuestion(type) : 
                contentRepo.getRandomInfo(type);
        
        // Use pre-loaded sprite
        BufferedImage sprite = isQuestion ? questionSprite : infoSprite;
        
        // Create ShotBox
        ShotBox box;
        double speed = isQuestion ? GameConstants.QUESTION_SPEED * 1.5 : GameConstants.INFO_SPEED;
        
        if (isQuestion) {
            box = new Question(keeper, content, 
                    keeper.getX() + (keeper.getWidth() - GameConstants.SHOTBOX_WIDTH) / 2,
                    keeper.getY() + keeper.getHeight(),
                    GameConstants.SHOTBOX_WIDTH, GameConstants.SHOTBOX_HEIGHT,
                    speed, sprite);
        } else {
            box = new Information(keeper, content, 
                    keeper.getX() + (keeper.getWidth() - GameConstants.SHOTBOX_WIDTH) / 2,
                    keeper.getY() + keeper.getHeight(),
                    GameConstants.SHOTBOX_WIDTH, GameConstants.SHOTBOX_HEIGHT,
                    speed, sprite);
        }
        
        // Add to game
        gameState.addShotBox(box);

        // Add to feed panel directly to avoid iterating all boxes in GameFrame.updateUI
        if (gameFrame != null && gameFrame.getFeedPanel() != null && !box.isAdded()) {
            gameFrame.getFeedPanel().addShotBox(box);
            box.setAdded(true);
        }
    }
    
    /**
     * Calculates x position for a keeper
     */
    private int calculateKeeperX(int index, int total) {
        if (total <= 0) return 0; // Avoid division by zero if total is 0 or 1 (for total=1, spacing is 0)
        int availableWidth = GameConstants.GAME_WIDTH - GameConstants.KEEPER_WIDTH;
        if (total == 1) return availableWidth / 2; // Center if only one
        int spacing = availableWidth / (total - 1);
        return index * spacing;
    }
    
    /**
     * Game loop update method
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Check if game is running
        if (gameState == null || !gameState.isRunning()) {
            return;
        }

        // Handle player movement based on flags
        Player player = gameState.getPlayer();
        if (player != null) {
            if (moveLeftActive) {
                player.moveLeft(GameConstants.GAME_WIDTH);
            }
            if (moveRightActive) {
                player.moveRight(GameConstants.GAME_WIDTH);
            }
        }
        
        // Update game state
        gameState.update();
        
        // Update keeper positions
        if (gameState.getCurrentLevel() != null && gameState.getCurrentLevel().getKeepers() != null) {
            for (KnowledgeKeeper keeper : gameState.getCurrentLevel().getKeepers()) {
                keeper.updateWithPlayerTracking(gameState.getPlayer(), GameConstants.GAME_WIDTH);
            }
        }
        
        // Check for level completion
        checkLevelCompletion();
        
        // Check for collisions
        checkCollisions();

        // Check for game over by health depletion
        player = gameState.getPlayer(); // Ensure we have the latest player state
        if (player != null && player.getHealth() <= 0 && gameState.isRunning()) {
            endGame(false); // Player is defeated
        }

        // Update UI
        if (this.gameFrame != null) {
            this.gameFrame.updateUI();
        }
    }
    
    /**
     * Checks for collisions between player and ShotBoxes
     */
    private void checkCollisions() {
        Player player = gameState.getPlayer();
        ArrayList<ShotBox> shotBoxes = gameState.getActiveShotBoxes();
        ArrayList<ShotBox> toRemove = new ArrayList<>();
        
        for (ShotBox box : shotBoxes) {
            if (player != null && box.collidesWith(player)) {
                // Apply effect
                if (box instanceof Question) {
                    int beforeHealth = player.getHealth();
                    box.applyEffect(player);
                    int damage = beforeHealth - player.getHealth();
                    GameLogger.logDamage(damage, player.getHealth());
                } else if (box instanceof Information) {
                    int beforeScore = player.getScore();
                    box.applyEffect(player);
                    int points = player.getScore() - beforeScore;
                    GameLogger.logInfo(points, player.getScore());
                }
                
                // Mark for removal
                toRemove.add(box);
            }
        }
        
        // Remove hit boxes
        for (ShotBox box : toRemove) {
            gameState.removeShotBox(box);
        }
    }
    
    /**
     * Checks if the current level is completed
     */
    private void checkLevelCompletion() {
        Player player = gameState.getPlayer();
        Level level = gameState.getCurrentLevel();
        
        if (player.getScore() >= level.getTargetScore()) {
            if (level.getNumber() < 3) {
                // Advance to next level
                advanceLevel();
            } else {
                // Game won
                endGame(true);
            }
        }
    }
    
    /**
     * Advances to the next level
     */
    public void advanceLevel() {
        int nextLevel = currentLevelNumber + 1;
        
        // Log level transition
        GameLogger.logLevelTransition(currentLevelNumber, nextLevel);
        
        // Stop all timers for keepers from the *previous* level's configuration
        // The new call to initializeLevel will set up timers for the new configuration
        if (gameState.getCurrentLevel() != null) {
            for (KnowledgeKeeper keeper : gameState.getCurrentLevel().getKeepers()) {
                keeper.stopTimer();
            }
        }
        
        // Clear shot boxes
        gameState.getActiveShotBoxes().clear();
        
        // Update level number
        currentLevelNumber = nextLevel;
        
        // Initialize new level
        initializeLevel(currentLevelNumber);
    }
    
    /**
     * Ends the game
     */
    private void endGame(boolean victory) {
        // Stop the game
        gameState.endGame(victory);
        
        // Stop the game loop timer
        gameLoopTimer.stop();
        
        // Stop all keeper timers
        for (KnowledgeKeeper keeper : gameState.getCurrentLevel().getKeepers()) {
            keeper.stopTimer();
        }
        
        // Log game end
        GameLogger.logEnd(victory, gameState.getPlayer().getScore(), gameState.getPlayer().getHealth());
        
        // Add score to scoreboard
        scoreController.addScore(user.getUsername(), gameState.getPlayer().getScore());
    }
    
    /**
     * Sets the state for moving the player left.
     */
    public void setMoveLeftActive(boolean active) {
        this.moveLeftActive = active;
        // If activating left, ensure right is not active (optional, depends on desired input scheme)
        // if (active) this.moveRightActive = false;
    }

    /**
     * Sets the state for moving the player right.
     */
    public void setMoveRightActive(boolean active) {
        this.moveRightActive = active;
        // If activating right, ensure left is not active (optional)
        // if (active) this.moveLeftActive = false;
    }
    
    /**
     * Toggles player visibility
     */
    public void togglePlayerHidden() {
        if (gameState.isRunning()) {
            gameState.getPlayer().toggleHidden();
        }
    }
    
    /**
     * Gets the game state
     */
    public GameState getGameState() {
        return gameState;
    }
    
    /**
     * Gets the score controller
     */
    public ScoreController getScoreController() {
        return scoreController;
    }
} 