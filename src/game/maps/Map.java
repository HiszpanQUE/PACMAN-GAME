package game.maps;

public class Map {
    private final int[][] grid;
    private final boolean[][] upgrades;
    private final boolean[][] points;
    private int width;
    private int height;

    public Map(int[][] grid) {
        this.grid = grid;
        this.width = grid.length;
        this.height = grid[0].length;
        this.upgrades = new boolean[width][height];
        this.points = new boolean[width][height];

        initalize();
    }

    private void initalize() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (grid[i][j] == 1) {
                    points[i][j] = true;
                    upgrades[i][j] = false;
                }
            }
        }
    }
}
