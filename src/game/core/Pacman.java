package game.core;

import game.entites.Directions;
import ui.GameScreen;
import javax.swing.*;
import java.awt.*;

public class Pacman extends Character{
    private int scores, boost;
    private static boolean immortal;
    private int previousX, previousY;
    private static int lives;
    GameScreen gameScreen;

    public Pacman(int x, int y,
                  double speed, int[][] mapGrid) {
        super(x, y, mapGrid);
        this.scores = 0;
        this.boost = 1;
        this.immortal = false;
        this.lives = 3;
        setSpeed(speed);
        loadImages();
    }

    public static int getLives() {
        return lives;
    }

    public static void setLives(int lives) {
        Pacman.lives = lives;
    }

    public static boolean isImmortal() {
        return immortal;
    }

    public void setImmortal(boolean immortal) {
        this.immortal = immortal;
    }

    public void setBoost(int boost) {
        this.boost = boost;
    }

    public void setScores(int scores) {
        this.scores = scores;
    }

    public int getScores() {
        return scores;
    }

    public int getPreviousX() {
        return previousX;
    }

    public int getPreviousY() {
        return previousY;
    }

    public void setGameScreen(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Override
    public void loadImages() {
        icons = new ImageIcon[4][4];
        Directions[] directions = Directions.values();
        for (int i = 0; i < directions.length; i++) {
            for (int j = 0; j < 4; j++) {
                String imagePath = "data/pacman/" + directions[i].name().toLowerCase() + "/" + (j+1) + ".png";
                ImageIcon icon = new ImageIcon(imagePath);
                Image scaledImage = icon.getImage().getScaledInstance(icon.getIconWidth(), icon.getIconHeight(), Image.SCALE_SMOOTH);
                icons[i][j] = new ImageIcon(scaledImage);
            }
        }
        setIcon(icons[0][0]);
    }

    @Override
    public void move() {
        previousX = getX();
        previousY = getY();

        int newX = getX();
        int newY = getY();

        switch (direction) {
            case UP -> newX--;
            case DOWN -> newX++;
            case LEFT -> newY--;
            case RIGHT -> newY++;
        }

        if(isWall(newX, newY)) {
            randomizeDirection();
            move();
        } else {
            setX(newX);
            setY(newY);
            collectScore(newX, newY);
        }
    }

    @Override
    public void updateAnimation() {
        int test = direction.ordinal();
        currentFrame = (currentFrame + 1) % icons.length;
        Icon iconToSet = icons[test][currentFrame];
        setIcon(iconToSet);
    }

    private void collectScore(int x, int y){
        if(mapGrid[x][y] == 0){
            scores += 10 * boost;
            mapGrid[x][y] = 2;
        }

        if(mapGrid[x][y] == 3){
            int choice = (int)(Math.random()*5);
            mapGrid[x][y] = 2;
            Thread upgradeThread = new Thread(() -> {
                try {
                    switch (choice) {
                        case 0: {
                            setImmortal(true);
                            System.out.println("Immortal");
                            gameScreen.showUpgradeDetails("FRIGHTENED!");
                            Thread.sleep(5000);
                            setImmortal(false);
                        }
                        break;
                        case 1: {
                            setBoost(2);
                            System.out.println("Boostx2");
                            gameScreen.showUpgradeDetails("BOOST POINTS x2");
                            Thread.sleep(5000);
                            setBoost(1);
                        }
                        break;
                        case 2: {
                            setBoost(3);
                            System.out.println("Boostx3");
                            gameScreen.showUpgradeDetails("BOOST POINTS x3!");
                            Thread.sleep(5000);
                            setBoost(1);
                        }
                        break;
                        case 3: {
                            System.out.println("BoostxRANDOM");
                            setScores(getScores() + (int)(Math.random() * 100));
                            gameScreen.showUpgradeDetails("POINTS BOOST!");
                            Thread.sleep(5000);
                        }
                        break;
                        case 4: {
                            System.out.println("LIFE");
                            if (getLives() <= 2)
                            setLives(getLives() + 1);
                            gameScreen.gameLife();
                            gameScreen.showUpgradeDetails("LIFE ADDED!");
                            Thread.sleep(5000);
                        }
                        break;
                        default: {
                            break;
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            upgradeThread.start();
        }
    }
}
