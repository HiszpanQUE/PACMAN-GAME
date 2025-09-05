package ui;

import persistance.HighScores;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class MenuPanel extends JPanel {
    private static final String BACKGROUND_PATH = "data/background/pacmanBackground1920x1080.png";
    private static final String GAME_LOGO_HUGE = "data/logos/PacmanHugeGameLogo.png";
    private static final String BLINKY = "data/ghosts/Blinky/blinky1.png";
    private static final String INKY = "data/ghosts/Inky/inky2.png";
    private static final String PINKY = "data/ghosts/Pinky/pinky2.png";
    private static final String CLYDE  ="data/ghosts/Clyde/clyde1.png";
    private HighScores highScores;
    private JButton newGameButton;
    private JButton highScoreButton;
    private JButton exitButton;
    private BufferedImage backgroundImage;
    private BufferedImage logoImage;
    private BufferedImage blinky;
    private BufferedImage inky;
    private BufferedImage pinky;
    private BufferedImage clyde;
    private int imageHeight;
    private int imageWidth;
    private Map<String, Point> ghostPositions;
    private Map<String, Point> ghostVelocities;

    public MenuPanel() {
        highScores = new HighScores();

        ghostPositions = new HashMap<>();
        ghostVelocities = new HashMap<>();

        setupGhosts();
        loadImages();
        specifyButton();
        actionPerformed();

        imageWidth = 75;
        imageHeight = 75;

        setLayout(null);
        this.setPreferredSize(new Dimension(800, 800));
        this.setVisible(true);
    }

    private void loadImages(){
        try {
            backgroundImage = ImageIO.read(new File(BACKGROUND_PATH));
            logoImage = ImageIO.read(new File(GAME_LOGO_HUGE));
            blinky = ImageIO.read(new File(BLINKY));
            inky = ImageIO.read(new File(INKY));
            pinky = ImageIO.read(new File(PINKY));
            clyde = ImageIO.read(new File(CLYDE));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupGhosts(){
        ghostPositions.put("Blinky", new Point(400, 400));
        ghostPositions.put("Inky", new Point(300, 75));
        ghostPositions.put("Pinky", new Point(675, 300));
        ghostPositions.put("Clyde", new Point(100, 300));

        ghostVelocities.put("Blinky", new Point(1, -1));
        ghostVelocities.put("Inky", new Point(1, 1));
        ghostVelocities.put("Pinky", new Point(-1, -1));
        ghostVelocities.put("Clyde", new Point(-1, 1));
    }

    private void setupButton(JButton button) {
        button.setFont(new Font("Serif", Font.BOLD, 16));
        button.setForeground(new Color(251, 198, 3));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
    }

    private void specifyButton(){
        newGameButton = new JButton("New Game");
        highScoreButton = new JButton("High Score");
        exitButton = new JButton("Exit");

        setupButton(newGameButton);
        setupButton(highScoreButton);
        setupButton(exitButton);

        add(newGameButton);
        add(highScoreButton);
        add(exitButton);

        newGameButton.addActionListener(e -> new MapBuilderScreen());
        highScoreButton.addActionListener(e -> new HighScoresScreen(highScores));
        exitButton.addActionListener(e -> System.exit(0));

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                setButtonsPositions();
            }
        });
    }

    private void setButtonsPositions() {
        int buttonWidth = getWidth() / 6;
        int buttonHeight = getHeight() / 10;
        newGameButton.setBounds((getWidth() - (buttonWidth)) / 2, getHeight() / 2  , buttonWidth, buttonHeight);
        highScoreButton.setBounds((getWidth() - (buttonWidth)) / 2, getHeight() / 2 + buttonHeight + 20, buttonWidth, buttonHeight);
        exitButton.setBounds((getWidth() - (buttonWidth)) / 2, (getHeight() / 2 + (buttonHeight + 20)*2), buttonWidth, buttonHeight);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
        if (blinky != null) {
            Point pos = ghostPositions.get("Blinky");
            g.drawImage(blinky, pos.x,pos.y, imageWidth, imageHeight,this);
        }
        if (inky != null) {
            Point pos = ghostPositions.get("Inky");
            g.drawImage(inky, pos.x,pos.y, imageWidth, imageHeight,this);
        }
        if (pinky != null) {
            Point pos = ghostPositions.get("Pinky");
            g.drawImage(pinky, pos.x,pos.y, imageWidth, imageHeight,this);
        }
        if (clyde != null) {
            Point pos = ghostPositions.get("Clyde");
            g.drawImage(clyde, pos.x,pos.y, imageWidth, imageHeight,this);
        }
        if (logoImage != null) {
            g.drawImage(logoImage, (getWidth() - logoImage.getWidth()) / 2, (getHeight() / 4 - logoImage.getHeight() / 2) , 500, 300, this);
        }
    }

    private void actionPerformed() {
        Thread animation = new Thread(() -> {
            while (true) {
                for (String ghost : ghostPositions.keySet()) {
                    Point position = ghostPositions.get(ghost);
                    Point velocity = ghostVelocities.get(ghost);

                    int newX = position.x + velocity.x;
                    int newY = position.y + velocity.y;

                    if (newX < 0 || newX > getWidth() - imageWidth) {
                        velocity.x = -velocity.x;
                        newX = position.x + velocity.x;
                    }

                    if (newY < 0 || newY > getHeight() - imageHeight) {
                        velocity.y = -velocity.y;
                        newY = position.y + velocity.y;
                    }

                    position.setLocation(newX, newY);
                }

                repaint();

                try {
                    Thread.sleep(20);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        });
        animation.start();
    }
}