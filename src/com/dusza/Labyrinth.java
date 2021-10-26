package com.dusza;

public class Labyrinth {
    public static final int LABYRINTH_WIDTH = 9;
    public static final int LABYRINTH_HEIGHT = 9;
    public static final char WALL_CHAR = '█';
    public static final char PATH_CHAR = ' ';

    private char[][] labyrinth = new char[LABYRINTH_HEIGHT][LABYRINTH_WIDTH];
    private final Player player;
    private int exitX;
    private int exitY;

    public Labyrinth() {
        this.player = new Player(this);
    }

    public void display() {
        for(int h = 0; h < LABYRINTH_HEIGHT; h++) {
            for(int w = 0; w < LABYRINTH_WIDTH; w++) {
                if(player.getX() == w && player.getY() == h) System.out.print(player.getPlayerDirection());
                else System.out.print(labyrinth[h][w]);
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
  
    public Player getPlayer() {
        return player;
    }

    public void setExitX(int exitX) {
        this.exitX = exitX;
    }

    public void setExitY(int exitY) {
        this.exitY = exitY;
    }
}
