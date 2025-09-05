package game.core;

import game.entites.Directions;
import javax.swing.*;

public abstract class Character extends JLabel{
    ImageIcon[][] icons;
    Directions direction;
    private int x;
    private int y;
    private double speed;
    Thread animationThread;
    int currentFrame;
    public int[][] mapGrid;

    public Character(int x, int y, int[][] mapGrid) {
        this.x = x;
        this.y = y;
        this.direction = Directions.UP;
        this.mapGrid = mapGrid;
        initAnimationThread();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setDirection(Directions direction) {
        this.direction = direction;
    }

    private void initAnimationThread() {
        animationThread = new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(250);
                    updateAnimation();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        animationThread.start();
    }

    public abstract void updateAnimation();

    public boolean isWall(int x, int y) {
        return mapGrid[x][y] == 1;
    }

    public void randomizeDirection() {
        Directions[] directions = Directions.values();
        direction = directions[(int)(Math.random() * directions.length)];
    }

    public abstract void loadImages();

    public abstract void move();
}
