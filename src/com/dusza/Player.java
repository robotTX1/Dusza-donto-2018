package com.dusza;

import java.util.HashMap;
import java.util.Map;

public class Player {
    private static final Map<Integer, Character> DIRECTION_MAP = new HashMap<>();

    static {
        DIRECTION_MAP.put(0, '^');
        DIRECTION_MAP.put(1, '>');
        DIRECTION_MAP.put(2, 'V');
        DIRECTION_MAP.put(3, '<');
    }

    private int direction = 2;
    private int x = Labyrinth.START_X;
    private int y = Labyrinth.START_Y;
    private int moves = 0;
    private final Labyrinth labyrinth;


    public Player(Labyrinth labyrinth) {
        this.labyrinth = labyrinth;
    }

    public char getPlayerDirection() {
        return DIRECTION_MAP.get(direction);
    }

    public int getPlayerDirectionInt() {
        return direction;
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
        this.direction = dir;
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
