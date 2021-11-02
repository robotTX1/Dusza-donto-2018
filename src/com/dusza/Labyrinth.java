package com.dusza;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Labyrinth {
    public static final int START_X = 1;
    public static final int START_Y = 1;
    public static final int LABYRINTH_WIDTH = 9;
    public static final int LABYRINTH_HEIGHT = 9;
    public static final char WALL_CHAR = '█';
    public static final char PATH_CHAR = ' ';

    private char[][] labyrinth = new char[LABYRINTH_HEIGHT][LABYRINTH_WIDTH];
    private Player player;
    private int exitX = 8;
    private int exitY = 1;

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

    public void generateNewLabyrinth() {
        // Inicializáljuk a táblát
        for(int h = 0; h < LABYRINTH_HEIGHT; h++) {
            for(int w = 0; w < LABYRINTH_WIDTH; w++) {
                labyrinth[h][w] = WALL_CHAR;
            }
        }

        generate(START_X, START_Y);
        createExit();
        player = new Player(this);
    }

    private void generate(int x, int y) {
        setPath(x, y);

        ArrayList<int[]> allDirections = new ArrayList<>(Arrays.asList(
                new int[]{1,0},
                new int[]{-1,0},
                new int[]{0,1},
                new int[]{0,-1}
        ));

        Collections.shuffle(allDirections);

        while(allDirections.size() > 0) {
            int[] directionToTry = allDirections.get(0);
            allDirections.remove(0);

            int nodeX = x + (directionToTry[0] * 2); // 1
            int nodeY = y + (directionToTry[1] * 2); // 1 + (-2)

            if(isWall(nodeX, nodeY)) {
                int linkCellX = x + directionToTry[0];
                int linkCellY = y + directionToTry[1];
                setPath(linkCellX, linkCellY);

                generate(nodeX, nodeY);
            }
        }
    }

    private void createExit() {
        List<int[]> sideWalls = new ArrayList<>();

        for(int h = 0; h < LABYRINTH_HEIGHT; h++) {
            for (int w = 0; w < LABYRINTH_WIDTH; w++) {
                if ((w == 0 || w == LABYRINTH_WIDTH - 1) || (h == 0 || h == LABYRINTH_HEIGHT - 1)) {
                    if(isConnectedToPath(w, h)) {
                        sideWalls.add(new int[]{w,h});
                    }
                }
            }
        }

        int[] exit = sideWalls.get((int)(Math.random() * sideWalls.size()));

        exitX = exit[0];
        exitY = exit[1];

        setPath(exitX, exitY);
    }

    private boolean isConnectedToPath(int x, int y) {
        return isPath(x+1,y) ||
                isPath(x-1,y) ||
                isPath(x,y+1) ||
                isPath(x,y-1);
    }

    private boolean isPath(int x, int y) {
        if(x >= 0 && x < LABYRINTH_WIDTH && y >= 0 && y < LABYRINTH_HEIGHT) {
            return labyrinth[y][x] == PATH_CHAR;
        }
        return false;
    }

    public boolean isWall(int x, int y) {
        if(x >= 0 && x < LABYRINTH_WIDTH && y >= 0 && y < LABYRINTH_HEIGHT) {
            return labyrinth[y][x] == WALL_CHAR;
        }
        return false;
    }

    private void setPath(int x, int y) {
        labyrinth[y][x] = PATH_CHAR;
    }

    private void setWall(int x, int y) {
        labyrinth[y][x] = WALL_CHAR;
    }

    public boolean isEscaped() {
        return player.getX() == exitX && player.getY() == exitY;
    }

    public void setLabyrinth(char[][] labyrinth) {
        this.labyrinth = labyrinth;

        for(int h=0; h < LABYRINTH_HEIGHT; h++) {
            for(int w = 0; w < LABYRINTH_WIDTH; w++) {
                if((w == 0 || w == LABYRINTH_WIDTH-1) || (h == 0 || h == LABYRINTH_HEIGHT-1)) {
                    if(labyrinth[h][w] == PATH_CHAR) {
                        exitX = w;
                        exitY = h;
                        break;
                    }
                }
            }
        }
        player = new Player(this);
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
}
