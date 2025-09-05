package ui;

import persistance.HighScores;
import persistance.Score;

import javax.swing.*;

public class ScoreSavingScreen{
    HighScores highScores;
    HighScoresScreen highScoresScreen;

    public ScoreSavingScreen(int score) {
        highScores = new HighScores();

        initializeSavingScreen(score);
    }

    private void initializeSavingScreen(int score) {
        while(true){
            String name = JOptionPane.showInputDialog("Enter your nickname: ");

            if(name == null){
                JOptionPane.showMessageDialog(
                        null,
                        "Saving cancelled, \nYour result will not be saved!",
                        "Operation cancelled",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            name = name.trim();

            if (!name.isEmpty()){
                if (highScoresScreen != null) {
                    highScoresScreen.setName(name);
                }

                Score scoreToSave = new Score(name, score);

                highScores.addScore(scoreToSave);
                highScores.saveScoresToFile();
                break;
            } else {
                JOptionPane.showMessageDialog(null,
                        "Nickname cannot be empty!",
                        "Saving Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}
