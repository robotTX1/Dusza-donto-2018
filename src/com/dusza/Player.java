package com.dusza;

import java.util.HashMap;
import java.util.Map;

public class Player {
    private static Map<Integer, Character> directionMap = new HashMap<>();

    static {
        directionMap.put(0, '^');
        directionMap.put(1, '>');
        directionMap.put(2, 'V');
        directionMap.put(3, '<');
    }

    private int direction = 2;
    private int x = 1;
    private int y = 1;
    private int moves = 0;
    private Labyrinth labyrinth;


    public Player(Labyrinth labyrinth) {
        this.labyrinth = labyrinth;
    }

    public char getPlayerDirection() {
        return directionMap.get(direction);
    }

    public void turnLeft() {
        direction--;
        if(direction < 0) direction = 3;
    }

    public void turnRight() {
        direction++;
        if(direction > 3) direction = 0;
    }

    public void turn(int dir) {
        if(dir > 0) turnRight();
        if(dir < 0) turnLeft();
    }

    private int getNextX() {
        return switch (direction) {
            case 1 -> x + 1;
            case 3 -> x - 1;
            default -> x;
        };
    }

    private int getNextY() {
        return switch (direction) {
            case 0 -> y - 1;
            case 2 -> y + 1;
            default -> y;
        };
    }

    public boolean forward() {
        if(labyrinth.isWall(getNextX(), getNextY())) return false;

        this.x = getNextX();
        this.y = getNextY();
        moves++;

        return true;
    }

    public static Map<Integer, Character> getDirectionMap() {
        return directionMap;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getMoves() {
        return moves;
    }
}
