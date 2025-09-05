package persistance;

import java.io.Serializable;

public class Score implements Serializable, Comparable<Score> {
    private static final long serialVersionUID = 1L;
    private String name;
    private int score;

    public Score(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "Name " + name + ", persistance.Score " + score;
    }

    @Override
    public int compareTo(Score o) {
        return 0;
    }
}
