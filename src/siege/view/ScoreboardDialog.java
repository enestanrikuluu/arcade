package siege.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import siege.controller.ScoreController;
import siege.model.ScoreEntry;

/**
 * Dialog for displaying the scoreboard
 */
public class ScoreboardDialog extends JDialog {
    private JTable scoresTable;
    private JButton closeButton;
    
    private ScoreController scoreController;
    
    /**
     * Creates a new scoreboard dialog
     */
    public ScoreboardDialog(JFrame parent, ScoreController scoreController) {
        super(parent, "Knowledge Siege - Scoreboard", true);
        
        this.scoreController = scoreController;
        
        setSize(500, 400);
        setLocationRelativeTo(parent);
        
        createComponents();
        
        layoutComponents();
        
        addEventHandlers();
    }
    
    /**
     * Creates the UI components
     */
    private void createComponents() {
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        model.addColumn("Rank");
        model.addColumn("Player");
        model.addColumn("Score");
        
        scoresTable = new JTable(model);
        scoresTable.getTableHeader().setReorderingAllowed(false);
        
        scoresTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        scoresTable.getColumnModel().getColumn(1).setPreferredWidth(300);
        scoresTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        
        populateTable();
        
        closeButton = new JButton("Close");
    }
    
    /**
     * Layouts the components
     */
    private void layoutComponents() {       
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setLayout(new BorderLayout());
        
        JLabel titleLabel = new JLabel("Top Scores", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        JScrollPane scrollPane = new JScrollPane(scoresTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(closeButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    /**
     * Adds event handlers to components
     */
    private void addEventHandlers() {
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        getRootPane().registerKeyboardAction(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                    }
                },
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );
    }
    
    /**
     * Populates the table with scores
     */
    private void populateTable() {
        ArrayList<ScoreEntry> scores = scoreController.getScores();
        
        DefaultTableModel model = (DefaultTableModel) scoresTable.getModel();
        
        model.setRowCount(0);
        
        for (int i = 0; i < scores.size(); i++) {
            ScoreEntry entry = scores.get(i);
            
            String playerDisplay = "Game" + entry.getGameNumber() + " by " + entry.getUsername();
            
            model.addRow(new Object[] {
                i + 1,                 
                playerDisplay,         
                entry.getScore()       
            });
        }
    }
} 