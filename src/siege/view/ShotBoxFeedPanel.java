package siege.view;

import java.awt.*;
import javax.swing.*;
import siege.model.*;

/**
 * Panel for displaying textual content of shot boxes
 */
public class ShotBoxFeedPanel extends JPanel {
    private JLabel titleLabel;
    private DefaultListModel<String> listModel;
    private JList<String> contentList;
    
    private static final int MAX_ITEMS = 10;
    
    /**
     * Creates a new shot box feed panel
     */
    public ShotBoxFeedPanel() {
        setPreferredSize(new Dimension(200, 0));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(new BorderLayout());
        
        createComponents();
        
        layoutComponents();
    }
    
    /**
     * Creates the UI components
     */
    private void createComponents() {
        titleLabel = new JLabel("Shot Box Content", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        listModel = new DefaultListModel<>();
        
        contentList = new JList<>(listModel);
        contentList.setCellRenderer(new FeedCellRenderer());
    }
    
    /**
     * Layouts the components
     */
    private void layoutComponents() {
        add(titleLabel, BorderLayout.NORTH);
        
        JScrollPane scrollPane = new JScrollPane(contentList);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Adds a shot box to the feed
     */
    public void addShotBox(ShotBox box) {
        String displayText = createDisplayText(box);
        
        listModel.add(0, displayText);
        
        if (listModel.getSize() > MAX_ITEMS) {
            listModel.remove(listModel.getSize() - 1);
        }
    }
    
    /**
     * Creates display text for a shot box
     */
    private String createDisplayText(ShotBox box) {
        StringBuilder sb = new StringBuilder();
        
        if (box instanceof Question) {
            sb.append("❓ ");
        } else if (box instanceof Information) {
            sb.append("💡 ");
        }

        KeeperType type = box.getSource().getType();
        sb.append("[").append(typeToString(type)).append("] ");
        
        sb.append(box.getText());
        
        return sb.toString();
    }
    
    /**
     * Converts keeper type to display string
     */
    private String typeToString(KeeperType type) {
        switch (type) {
            case SECTION_LEADER:
                return "SL";
            case TEACHING_ASSISTANT:
                return "TA";
            case PROFESSOR:
                return "Prof";
            default:
                return "?";
        }
    }
    
    /**
     * Clears the feed
     */
    public void clear() {
        listModel.clear();
    }
    
    /**
     * Custom cell renderer for feed items
     */
    private class FeedCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            
            if (c instanceof JLabel) {
                JLabel label = (JLabel) c;
                
                String text = label.getText();
                label.setText("<html>" + text + "</html>");
                
                label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                
                if (text.startsWith("❓")) {
                    label.setForeground(Color.RED.darker());
                } else if (text.startsWith("💡")) {
                    label.setForeground(Color.BLUE.darker());
                }
            }
            
            return c;
        }
    }
} 