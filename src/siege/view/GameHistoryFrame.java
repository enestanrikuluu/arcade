package siege.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import siege.model.User;
import siege.model.ScoreEntry;
import siege.controller.ScoreController;

public class GameHistoryFrame extends JDialog {
    private User user;
    private ScoreController scoreController;
    private JTable historyTable;
    private DefaultTableModel tableModel;

    public GameHistoryFrame(Frame owner, User user, ScoreController scoreController) {
        super(owner, "Game History for " + user.getUsername(), true);
        this.user = user;
        this.scoreController = scoreController;

        initComponents();
        loadHistory();
        
        setSize(500, 300);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        String[] columnNames = {"Game #", "Score", "Date"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        historyTable = new JTable(tableModel);
        historyTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        historyTable.setAutoCreateRowSorter(true); 

        JScrollPane scrollPane = new JScrollPane(historyTable);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(closeButton);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadHistory() {
        ArrayList<ScoreEntry> userScores = scoreController.getScoresForUser(user.getUsername());
                        
        userScores.sort(Comparator.comparing(ScoreEntry::getGameNumber, Comparator.reverseOrder())
                                  .thenComparing(ScoreEntry::getTimestamp, Comparator.reverseOrder()));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (ScoreEntry entry : userScores) {
            Object[] rowData = {
                entry.getGameNumber(),
                entry.getScore(),
                dateFormat.format(entry.getTimestamp())
            };
            tableModel.addRow(rowData);
        }
    }
} 