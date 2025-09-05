package ui;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;

public class MapBuilderScreen extends JFrame {
    private static final String MAP_LOGO = "data/logos/mapIcon.png";

    public MapBuilderScreen() {
        initializeMapBuilderFrame();
    }

    private void initializeMapBuilderFrame() {
        this.setTitle("Map Builder");
        this.setSize(600,220);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setIconImage(new ImageIcon(MAP_LOGO).getImage());
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        JPanel pageCenterPanel = new JPanel();
        JPanel pageEndPanel = new JPanel();

        JLabel xLabel = new JLabel("Choose number of rows (10-100):");
        JLabel yLabel = new JLabel("Choose number of columns (10-100):");

        xLabel.setFont(new Font("Serif", Font.BOLD, 15));
        yLabel.setFont(new Font("Serif", Font.BOLD, 15));

        xLabel.setHorizontalAlignment(SwingConstants.CENTER);
        yLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton playButton = new JButton("Play");
        JButton cancelButton = new JButton("Cancel");

        NumberFormatter numberFormatter = new NumberFormatter(NumberFormat.getIntegerInstance());

        JFormattedTextField xField = new JFormattedTextField(numberFormatter);
        JFormattedTextField yField = new JFormattedTextField(numberFormatter);

        panel.setLayout(new BorderLayout());
        pageCenterPanel.setLayout(new GridLayout(2,2, 5, 10));
        pageEndPanel.setLayout(new FlowLayout());

        pageCenterPanel.add(xLabel);
        pageCenterPanel.add(xField);
        pageCenterPanel.add(yLabel);
        pageCenterPanel.add(yField);

        pageEndPanel.add(playButton);
        pageEndPanel.add(cancelButton);

        playButton.addActionListener(e -> {
            int x = ((Number) xField.getValue()).intValue();
            int y = ((Number) yField.getValue()).intValue();

            if(x >= 10 && x <= 100 && y >= 10 && y <= 100) {
                new GameScreen(x, y);
                MapBuilderScreen.this.dispose();
            } else {
                JOptionPane.showMessageDialog(
                        null,
                        "Please enter a number between 10 and 100 for columns and rows",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        } );

        cancelButton.addActionListener(e -> {
           dispose();
        });

        panel.add(pageCenterPanel, BorderLayout.CENTER);
        panel.add(pageEndPanel, BorderLayout.PAGE_END);

        add(panel);
        pack();
        this.setVisible(true);
    }
}
