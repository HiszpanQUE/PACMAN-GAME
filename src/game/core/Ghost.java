package game.core;

import game.entites.GhostType;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Ghost extends Character {
    private GhostType ghostType;
    private ImageIcon[][] frightenedIcons;
    private int previousX, previousY;

    public Ghost(int x, int y, double speed, GhostType ghostType, int[][] mapGrid) {
        super(x, y, mapGrid);
        this.ghostType = ghostType;
        setSpeed(speed);
        loadImages();
        loadFrightenedImages();
    }

    public int getPreviousY() {
        return previousY;
    }

    public int getPreviousX() {
        return previousX;
    }

    @Override
    public void loadImages() {
        icons = new ImageIcon[4][2];
        String ghostName = ghostType.name().toLowerCase();
        File dir = new File("data/ghosts/" + ghostName);
        File[] files = dir.listFiles();
        for (int j = 0; j < 2; j++) {
            if (files != null && files.length > j && files[j].isFile()) {
                ImageIcon icon = new ImageIcon(files[j].getPath());
                Image scaledImage = icon.getImage().getScaledInstance(icon.getIconWidth(), icon.getIconHeight(), Image.SCALE_SMOOTH);
                icons[0][j] = new ImageIcon(scaledImage);
            }
        }
        setIcon(icons[0][0]);
    }

    private void loadFrightenedImages() {
        frightenedIcons = new ImageIcon[1][2];
        File dir = new File("data/ghosts/Frightened/");
        File[] files = dir.listFiles();
        for (int j = 0; j < 2; j++) {
            if (files != null && files.length > j && files[j].isFile()) {
                ImageIcon icon = new ImageIcon(files[j].getPath());
                Image scaledImage = icon.getImage().getScaledInstance(icon.getIconWidth(), icon.getIconHeight(), Image.SCALE_SMOOTH);
                frightenedIcons[0][j] = new ImageIcon(scaledImage);
            }
        }
    }

    @Override
    public void move() {
        previousX = getX();
        previousY = getY();

        int newX = getX();
        int newY = getY();

        switch (direction) {
            case UP -> newY--;
            case DOWN -> newY++;
            case LEFT -> newX--;
            case RIGHT -> newX++;
        }

        if(isWall(newX, newY)) {
            randomizeDirection();
            move();
        } else {
            setX(newX);
            setY(newY);
        }
    }

    @Override
    public void updateAnimation() {
        Thread updateAnimation = new Thread(()-> {
           try{
               currentFrame = currentFrame == 1 ? 0 : 1;
               if(!Pacman.isImmortal()) {
                   setIcon(icons[0][currentFrame]);
               } else {
                   setIcon(frightenedIcons[0][currentFrame]);
               }
           } catch (Exception e){
               e.printStackTrace();
           }
        });
        updateAnimation.start();
    }
}