package utils;

import game.entites.Directions;

public class MapUtils {
    private static final int WALL = 1;
    private static final int CORRIDOR = 0;
    private static final double POSSIBILITY_OF_CORRIDOR_ENTRY = 0.2;

    public static int[][] generateMap(int width, int height) {
        if (width % 2 == 0) width++;
        if (height % 2 == 0) height++;

        int[][] map = new int[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                map[i][j] = WALL;
            }
        }

        generateCorridor(map, 1, 1);

        for (int x = 2; x < map.length - 1; x += 2) {
            for (int y = 2; y < map[0].length - 1; y += 2) {
                if (map[x][y] == WALL) {
                    if ((map[x][y - 1] == CORRIDOR && map[x][y + 1] == CORRIDOR) || (map[x - 1][y] == CORRIDOR && map[x + 1][y] == CORRIDOR)) {
                        if (Math.random() < POSSIBILITY_OF_CORRIDOR_ENTRY) {
                            map[x][y] = CORRIDOR;
                        }
                    }
                }
            }
        }
        return map;
    }

    private static void generateCorridor(int[][] map, int x, int y) {
        map[x][y] = CORRIDOR;

        Directions[] directions = Directions.values();

        for (int i = directions.length - 1; i >= 0; i--) {
            int j = (int) (Math.random() * (i + 1));
            Directions temp = directions[i];
            directions[i] = directions[j];
            directions[j] = temp;
        }

        for (Directions dir : directions) {
            int newX = x;
            int newY = y;

            switch (dir) {
                case UP -> newY -= 2;
                case DOWN -> newY += 2;
                case LEFT -> newX -= 2;
                case RIGHT -> newX += 2;
            }

            if (newX > 0 && newX < map.length - 1 && newY > 0 && newY < map[0].length - 1 && map[newX][newY] == WALL) {
                map[(x + newX) / 2][(y + newY) / 2] = CORRIDOR;
                generateCorridor(map, newX, newY);
            }
        }
    }
}
