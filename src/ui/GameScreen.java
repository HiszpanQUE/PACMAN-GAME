package ui;

import game.core.*;
import game.entites.Directions;
import game.entites.GhostType;
import game.maps.Map;
import utils.ImageUtils;
import utils.MapUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GameScreen extends JFrame implements KeyListener {
    private static final String PACMAN = "data/logos/pacmanGameLogo.png";
    private static final String POINT = "data/points/smallPoint.png";
    private static final String BACKGROUND = "data/background/pacmanBackground1920x1080.png";
    private static String monitor = new String();
    private JPanel mainPanel;
    private JPanel gamePanel;
    private JPanel upperPanel;
    private JPanel centerPanel;
    private JPanel lifePanel;
    private JLabel scoreLabel;
    private JLabel timeLabel;
    private int [][] spawnPoints;
    private Ghost[] ghosts;
    private int time = 0;
    private boolean running = true;
    private boolean gameWon = false;
    Map gameMap;
    ResizableTable mapTable;
    Pacman pacman;
    Ghost blinky;
    Ghost inky;
    Ghost pinky;
    Ghost clyde;
    Random random;

    public GameScreen(int width, int height) {
        int[][] grid = MapUtils.generateMap(width, height);

        spawnPoints = new int[4][2];
        random = new Random();

        for (int i = 0; i < spawnPoints.length; i++) {
            while (true) {
                int x = random.nextInt(grid.length);
                int y = random.nextInt(grid[0].length);
                if (grid[x][y] != 1) {
                    spawnPoints[i][0] = x;
                    spawnPoints[i][1] = y;
                    break;
                }
            }
        }

        blinky = new Ghost(spawnPoints[0][0], spawnPoints[0][1], 1, GhostType.BLINKY, grid);
        inky = new Ghost(spawnPoints[1][0], spawnPoints[1][1], 1, GhostType.INKY, grid);
        pinky = new Ghost(spawnPoints[2][0], spawnPoints[2][1], 1, GhostType.PINKY, grid);
        clyde = new Ghost(spawnPoints[3][0], spawnPoints[3][1], 1, GhostType.CLYDE, grid);
        pacman = new Pacman(5,5, 1, grid);

        pacman.setGameScreen(this);

        this.gameMap = new Map(grid);

        ghosts = new Ghost[]{blinky, inky, pinky, clyde};
        spawnStrawberry(ghosts, grid);

        initalizeGameScreen();
        setupMapTable(grid);
        gameTimer();
        gameLife();
        startGame();
        showUpgradeDetails("");

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                gameOver();
            }
        });
    }

    public void initalizeGameScreen() {
        this.setTitle("Pac-Man");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(800, 600);
        setIconImage(new ImageIcon(PACMAN).getImage());
        this.setLocationRelativeTo(null);

        mainPanel = new JPanel();
        gamePanel = new JPanel();
        upperPanel = new JPanel();
        lifePanel = new JPanel();

        mainPanel.setLayout(new BorderLayout());
        gamePanel.setLayout(new BorderLayout());

        upperPanel.setLayout(new GridLayout(1,3));
        lifePanel.setLayout(new FlowLayout());

        timeLabel = new JLabel();
        scoreLabel = new JLabel();

        upperPanel.setBackground(Color.BLACK);

        timeLabel.setText("Time: ");
        timeLabel.setForeground(new Color(237, 28, 36));
        timeLabel.setBackground(Color.BLACK);
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timeLabel.setFont(new Font("Serif", Font.BOLD, 20));

        scoreLabel.setText("Score: ");
        scoreLabel.setForeground(Color.ORANGE);
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Serif", Font.BOLD, 28));

        upperPanel.add(timeLabel);
        upperPanel.add(scoreLabel);
        upperPanel.add(lifePanel);

        mainPanel.add(upperPanel, BorderLayout.NORTH);
        mainPanel.add(gamePanel, BorderLayout.CENTER);

        add(mainPanel);

        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        this.setVisible(true);
    }

    private void setupMapTable(int[][] grid) {
        GameMapTableModel model = new GameMapTableModel(grid);

        mapTable = new ResizableTable(model);
        mapTable.setShowGrid(false);
        mapTable.setBackground(Color.BLACK);
        mapTable.setIntercellSpacing(new Dimension(0, 0));
        mapTable.setTableHeader(null);
        mapTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        mapTable.setDefaultRenderer(Integer.class, new DefaultTableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                c.setText(null);
                c.setOpaque(true);
                c.setIcon(null);

                int cellSize = table.getRowHeight();

                if (pacman.getX() == row && pacman.getY() == column) {
                    ImageIcon icon = ImageUtils.loadScaledIcon(pacman.getIcon(), (int)(cellSize * 0.5));
                    c.setIcon(icon);
                    c.setBackground(Color.BLACK);
                } else if (isGhostAt(blinky, row, column)) {
                    setGhostIcon(c, blinky, cellSize);
                } else if (isGhostAt(inky, row, column)) {
                    setGhostIcon(c, inky, cellSize);
                } else if (isGhostAt(pinky, row, column)) {
                    setGhostIcon(c, pinky, cellSize);
                } else if (isGhostAt(clyde, row, column)) {
                    setGhostIcon(c, clyde, cellSize);
                } else {
                    int cellValue = (value != null) ? (int) value : 0;
                    switch (cellValue) {
                        case 0:
                            c.setIcon(new ImageIcon(POINT));
                            c.setBackground(Color.BLACK);
                            break;
                        case 1:
                            c.setBackground(Color.BLUE);
                            break;
                        case 2:
                            c.setIcon(null);
                            c.setBackground(Color.BLACK);
                            checkIfAllPointsCollected();
                            break;
                        case 3:
                            ImageIcon icon = new ImageIcon("data/points/strawberry.png");
                            Image scaledImage = icon.getImage().getScaledInstance((int) (cellSize * 0.5), (int) (cellSize * 0.5), Image.SCALE_SMOOTH);
                            c.setIcon(new ImageIcon(scaledImage));
                            c.setBackground(Color.BLACK);
                            break;
                        default:
                            c.setBackground(Color.BLACK);
                    }
                }
                return c;

            }
        });

        mapTable.setBorder(BorderFactory.createEmptyBorder());

        centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(mapTable, BorderLayout.CENTER);
        gamePanel.add(centerPanel, BorderLayout.CENTER);

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeCells();
            }
        });
        resizeCells();
        gamePanel.revalidate();
        gamePanel.repaint();
    }

    private boolean isGhostAt(Ghost ghost, int row, int column) {
        return ghost.getX() == row && ghost.getY() == column;
    }

    private void setGhostIcon(JLabel c, Ghost ghost, int cellSize) {
        ImageIcon icon = (ImageIcon) ghost.getIcon();
        Image scaledImage = icon.getImage().getScaledInstance((int)(cellSize * 0.6), (int)(cellSize * 0.6), Image.SCALE_SMOOTH);
        c.setIcon(new ImageIcon(scaledImage));
        c.setBackground(Color.BLACK);
    }
    
    private void resizeCells(){
        int tableWidth = gamePanel.getWidth();
        int tableHeight = gamePanel.getHeight();
        int cols = mapTable.getColumnCount();
        int rows = mapTable.getRowCount();

        double cellRadio = (double)cols / rows;
        double panelRadio = (double)tableWidth / tableHeight;

        int cellSize;
        if(panelRadio > cellRadio) {
            cellSize = tableWidth / rows;
        } else {
            cellSize = tableWidth / cols;
        }

        mapTable.setRowHeight(cellSize);

        for(int i = 0; i < cols; i++) {
            mapTable.getColumnModel().getColumn(i).setPreferredWidth(cellSize);
        }

        mapTable.repaint();
        mapTable.revalidate();
    }

    private void checkIfAllPointsCollected() {
        if (gameWon) {
            return;
        }

        int countCellValueZero = 0;

        for (int i = 0; i < mapTable.getRowCount(); i++) {
            for (int j = 0; j < mapTable.getColumnCount(); j++) {
                int cellValue = (int) mapTable.getValueAt(i, j);

                if (cellValue == 0) {
                    countCellValueZero++;
                }
            }
        }

        if (countCellValueZero == 0) {
            gameWon = true;
            winGame();
        }
    }

    private void spawnStrawberry(Ghost[] ghosts, int[][] mapGrid) {
        Thread strawberrySpawner = new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(5000);

                    double chance = Math.random();
                    if (chance < 0.25) {
                        int randomIndex = (int)(Math.random() * ghosts.length);
                        Ghost randomGhost = ghosts[randomIndex];

                        int x = randomGhost.getX();
                        int y = randomGhost.getY();

                        if (mapGrid[x][y] != 1) {
                            mapGrid[x][y] = 3;
                            mapTable.repaint();
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        strawberrySpawner.start();
    }

    public void showUpgradeDetails(String info){
        Thread upgradeDetailsThread = new Thread(() -> {
           try{
               scoreLabel.setText(info);
               Thread.sleep(10000);

               scoreLabel.setText("Score: " + pacman.getScores());
           } catch (Exception e){
               e.printStackTrace();
           }
            scoreLabel.setText("Score: " + pacman.getScores());
        });
        upgradeDetailsThread.start();
    }

    private void characterCollision() {
        for (Ghost ghost : ghosts) {
            if (pacman.getX() == ghost.getX() && pacman.getY() == ghost.getY()) {
                if (Pacman.isImmortal()){
                    pacman.setScores(pacman.getScores() + 200);
                } else {
                    looseLife();
                }
            } else if (pacman.getX() == ghost.getPreviousX() && pacman.getY() == ghost.getPreviousY() &&
                    pacman.getPreviousX() == ghost.getX() && pacman.getPreviousY() == ghost.getY()) {
                if (Pacman.isImmortal()){
                    pacman.setScores(pacman.getScores() + 200);
                } else {
                    looseLife();
                }
            }
        }
    }

    private void gameTimer(){
        Thread gameTimer = new Thread(() -> {
            while (running){
                try{
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                synchronized (monitor){
                    time++;

                    int minutes = time / 60;
                    int seconds = time % 60;

                    timeLabel.setText(String.format("Time: %d:%02d", minutes, seconds));
                }
            }
        });
        gameTimer.start();
    }

    public void gameLife() {
        lifePanel.removeAll();
        ImageIcon heartIcon = new ImageIcon("data/logos/heart.png");
        ImageIcon heart = ImageUtils.loadScaledIcon(heartIcon, 23);

        for (int i = 0; i < Pacman.getLives(); i++) {
            JLabel heartLabel = new JLabel(heart);
            heartLabel.setOpaque(true);
            heartLabel.setBackground(Color.BLACK);
            lifePanel.add(heartLabel);
        }

        lifePanel.setBackground(Color.BLACK);
        lifePanel.revalidate();
        lifePanel.repaint();
    }

    public void looseLife() {
        Pacman.setLives( Pacman.getLives() - 1);

        if (lifePanel.getComponentCount() > 0) {
            lifePanel.remove(lifePanel.getComponentCount() - 1);
            lifePanel.revalidate();
            lifePanel.repaint();
        }

        if (pacman.getLives() <= 0) {
            gameOver();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        closeGameWindow(e);

        switch (e.getKeyCode()){
            case KeyEvent.VK_UP:
                pacman.setDirection(Directions.UP);
                break;
            case KeyEvent.VK_DOWN:
                pacman.setDirection(Directions.DOWN);
                break;
            case KeyEvent.VK_LEFT:
                pacman.setDirection(Directions.LEFT);
                break;
            case KeyEvent.VK_RIGHT:
                pacman.setDirection(Directions.RIGHT);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private void closeGameWindow(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_Q &&
            (e.getModifiersEx() & KeyEvent.SHIFT_DOWN_MASK) != 0 &&
                (e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) != 0) {
            gameOver();
        }
    }

    private void startGame() {
        Thread game = new Thread(() -> {
            while (running) {
                try {
                    Thread.sleep(500);
                    synchronized (monitor) {
                        pacman.move();
                        blinky.move();
                        inky.move();
                        pinky.move();
                        clyde.move();
                        characterCollision();
                        updateScoreLabel();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread mapPaint = new Thread(() -> {
            while (running) {
                try{
                    Thread.sleep(250);
                    SwingUtilities.invokeLater(() -> {
                        mapTable.repaint();
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        mapPaint.start();
        game.start();
    }

    private void updateScoreLabel() {
        scoreLabel.setText("Score: " + pacman.getScores());
    }

    private void winGame() {

        running = false;

        ImageIcon backgroundImage = new ImageIcon(BACKGROUND);
        ImageIcon winGameImage = new ImageIcon("data/endGame/youwin.png");

        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setLayout(new BorderLayout());

        JPanel winPanel = new JPanel();
        winPanel.setLayout(new BorderLayout());
        winPanel.setOpaque(false);

        JLabel winLabel = new JLabel("", winGameImage, SwingConstants.CENTER);
        winLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel scoreLabel = new JLabel("Score: " + pacman.getScores());
        scoreLabel.setFont(new Font("Serif", Font.BOLD, 70));
        scoreLabel.setForeground(Color.YELLOW);
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);

        winPanel.add(winLabel, BorderLayout.PAGE_START);
        winPanel.add(scoreLabel, BorderLayout.CENTER);

        backgroundLabel.add(winPanel, BorderLayout.CENTER);

        setContentPane(backgroundLabel);
        revalidate();
        repaint();
        endGame();
}

    private void gameOver() {
        running = false;

        ImageIcon backgroundImage = new ImageIcon(BACKGROUND);
        ImageIcon gameOverImage = new ImageIcon("data/endGame/gameover.png");

        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setLayout(new BorderLayout());

        JPanel gameOverPanel = new JPanel();
        gameOverPanel.setLayout(new BorderLayout());
        gameOverPanel.setOpaque(false);

        JLabel gameOverLabel = new JLabel("", gameOverImage, JLabel.CENTER);

        JLabel scoreLabel = new JLabel("Score: " + pacman.getScores());
        scoreLabel.setFont(new Font("Serif", Font.BOLD, 70));
        scoreLabel.setForeground(Color.YELLOW);
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gameOverPanel.add(gameOverLabel, BorderLayout.PAGE_START);
        gameOverPanel.add(scoreLabel, BorderLayout.CENTER);

        backgroundLabel.add(gameOverPanel, BorderLayout.CENTER);

        setContentPane(backgroundLabel);
        revalidate();
        repaint();

        endGame();
    }

    private void endGame() {
        System.out.println("GAME OVER");

        Thread setWinningScore = new Thread(() -> {
            try{
                Thread.sleep(2000);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
            new ScoreSavingScreen(pacman.getScores());
            dispose();
        });
        setWinningScore.start();
    }
}
