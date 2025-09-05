package persistance;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HighScores {
    private static final String FILE_NAME = "wyniki.txt";
    private List<Score> scores;
    JList<String> list;

    public HighScores() {
        scores = new ArrayList<>();
        list = new JList<>();

        loadScoresFromFile();
        saveScoresToFile();
    }

    public List<Score> getScores() {
        return scores;
    }

    public void addScore(Score score) {
        scores.add(score);
    }

    public void loadScoresFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists() || file.length() == 0) {
            scores = new ArrayList<>();
            return;
        }
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))){
            scores = (List<Score>) ois.readObject();

        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean saveScoresToFile() {
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))){
            oos.writeObject(scores);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}

