package ui;

import persistance.HighScores;
import persistance.Score;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HighScoresScreen extends JDialog {
    private static final String[] COLUMN_NAMES = {"Rank", "Nick", "Score"};
    private static final String CHAMP_IMAGE = "data/logos/champion.png";
    private ImageIcon backgroundIcon = new ImageIcon("data/background/space_pacman.jpg");
    JLabel titleLabel;
    JPanel backgroundPanel;
    JPanel tablePanel;
    JLayeredPane layeredPane;
    JScrollPane scrollPane;

    public HighScoresScreen(HighScores highScores) {
        highScores.loadScoresFromFile();

        initializeUI(highScores);

        this.setVisible(true);
    }

    private void initializeUI(HighScores highScores) {
        this.setSize(500,500);
        this.setTitle("High Scores");
        this.setIconImage(new ImageIcon(CHAMP_IMAGE).getImage());
        this.setLayout(new BorderLayout());

        backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundIcon != null) {
                    g.drawImage(backgroundIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        titleLabel = new JLabel();

        tablePanel = new JPanel(new BorderLayout());

        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(500, 500));

        titleLabel.setBounds((getWidth()-500)/2, 25, 500, 75);

        titleLabel.setText("HIGH SCORES");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 50));
        titleLabel.setForeground(Color.YELLOW);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setVerticalAlignment(SwingConstants.BOTTOM);

        DefaultTableModel tableModel = new DefaultTableModel(COLUMN_NAMES, 0){
          @Override
          public boolean isCellEditable(int row, int column) {
              return false;
          }
        };

        List<Score> sortedScores = new ArrayList<>(highScores.getScores());
        sortedScores.sort(Comparator.comparingInt(Score::getScore).reversed());

        for (int i = 0; i < sortedScores.size(); i++) {
            Score score = sortedScores.get(i);
            tableModel.addRow(new Object[]{i + 1, score.getName(), score.getScore()});
        }

        JTable table = new JTable(tableModel);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setForeground(Color.YELLOW);
                c.setBackground(Color.BLACK);
                table.setOpaque(false);
                return c;
            }
        });

        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setBackground(Color.BLACK);
        table.getTableHeader().setForeground(Color.WHITE);

        scrollPane = new JScrollPane(table);

        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.setBounds((getWidth()-450)/2, 130, 450, 200);
        tablePanel.setOpaque(false);

        setContentPane(backgroundPanel);

        layeredPane.add(titleLabel, Integer.valueOf(1));
        layeredPane.add(tablePanel, Integer.valueOf(2));
        add(layeredPane, BorderLayout.CENTER);

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
    }
}
