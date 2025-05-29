package siege.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import siege.model.GameException;

/**
 * Utility class for loading and managing images
 */
public class ImageLoader {
    
    /**
     * Loads an image from a file
     */
    public static BufferedImage loadImage(String path) throws GameException {
        try {
            File file = new File(path);
            if (!file.exists()) {
                throw new GameException("Image file not found: " + path);
            }
            
            return ImageIO.read(file);
        } catch (IOException e) {
            throw new GameException("Failed to load image: " + path, e);
        }
    }
    
    /**
     * Loads an image from resources
     */
    public static BufferedImage loadResourceImage(String resourcePath) throws GameException {
        try {
            return ImageIO.read(ImageLoader.class.getResourceAsStream(resourcePath));
        } catch (IOException | NullPointerException e) {
            throw new GameException("Failed to load resource image: " + resourcePath, e);
        }
    }
    
    /**
     * Scales an image to the specified dimensions
     */
    public static BufferedImage scaleImage(BufferedImage original, int width, int height) {
        Image scaledImage = original.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        
        Graphics2D g2d = result.createGraphics();
        g2d.drawImage(scaledImage, 0, 0, null);
        g2d.dispose();
        
        return result;
    }
    
    /**
     * Creates an ImageIcon from a file path
     */
    public static ImageIcon createImageIcon(String path) {
        try {
            BufferedImage img = loadImage(path);
            return new ImageIcon(img);
        } catch (GameException e) {
            System.err.println("Failed to create ImageIcon: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Creates a placeholder image with specified dimensions and text
     */
    public static BufferedImage createPlaceholderImage(int width, int height, String text) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(new java.awt.Color(200, 200, 200, 255)); // light gray
        g2d.fillRect(0, 0, width, height);
        g2d.setColor(java.awt.Color.DARK_GRAY);
        g2d.drawRect(0, 0, width - 1, height - 1);
        g2d.setColor(java.awt.Color.BLACK);
        java.awt.Font font = new java.awt.Font("Arial", java.awt.Font.BOLD, Math.max(12, height / 8));
        g2d.setFont(font);
        java.awt.FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        int x = (width - textWidth) / 2;
        int y = (height - textHeight) / 2 + fm.getAscent();
        g2d.drawString(text, x, y);
        g2d.dispose();
        return image;
    }
} 