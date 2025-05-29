package siege;

import javax.swing.UIManager;
import siege.controller.AuthController;
import siege.view.LoginFrame;

/************** Pledge of Honor ******************************************
I hereby certify that I have completed this programming project on my own without
any help from anyone else. The effort in the project thus belongs completely to me.
I did not search for a solution, or I did not consult any program written by others
or did not copy any program from other sources. I read and followed the guidelines
provided in the project description.
READ AND SIGN BY WRITING YOUR NAME SURNAME AND STUDENT ID
SIGNATURE: <Enes Şaban Tanrıkulu, 0079378>
*************************************************************************/

/**
 * Main class for Knowledge Siege game
 */
public class KnowledgeSiege {
    
    /**
     * Main method that starts the application
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Failed to set look and feel: " + e.getMessage());
        }
        
        
        AuthController authController = new AuthController();
        
        LoginFrame loginFrame = new LoginFrame(authController);
        loginFrame.setVisible(true);
    }
} 