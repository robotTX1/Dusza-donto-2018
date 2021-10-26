package com.dusza;

public class Labyrinth {
    public static final int LABYRINTH_WIDTH = 9;
    public static final int LABYRINTH_HEIGHT = 9;
    public static final char WALL_CHAR = '█';
    public static final char PATH_CHAR = ' ';

    private char[][] labyrinth = new char[LABYRINTH_HEIGHT][LABYRINTH_WIDTH];
    private Player player;
    private int exitX;
    private int exitY;

    public Labyrinth() {
        generate();
    }

    public Labyrinth(char[][] labyrinth) {
        this.labyrinth = labyrinth;
    }

    public void display() {
        for(int h = 0; h < LABYRINTH_HEIGHT; h++) {
            for(int w = 0; w < LABYRINTH_WIDTH; w++) {
                System.out.print(labyrinth[h][w]);
            }
            System.out.println();
        }
    }

    public void generate() {

    }

    public boolean isEscaped() {
        return player.getX() == exitX && player.getY() == exitY;
    }

    public boolean isWall(int x, int y) {
        return labyrinth[y][x] == WALL_CHAR;
    }

    public void setLabyrinth(char[][] labyrinth) {
        this.labyrinth = labyrinth;
    }

    public char[][] getLabyrinth() {
        return labyrinth;
    }

    public int getExitX() {
        return exitX;
    }

    public int getExitY() {
        return exitY;
    }
}
