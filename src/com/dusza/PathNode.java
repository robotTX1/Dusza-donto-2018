package com.dusza;

public class PathNode {
    private final int x;
    private final int y;

    private final boolean walkable;

    private int gCost;
    private int hCost;
    private int fCost;

    private PathNode cameFromNode;

    public PathNode(int x, int y) {
        this(x, y, true);
    }

    public PathNode(int x, int y, boolean walkable) {
        this.x = x;
        this.y = y;
        this.walkable = walkable;
    }

    public void calculateFCost() {
        fCost = gCost + hCost;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getGCost() {
        return gCost;
    }

    public void setGCost(int gCost) {
        this.gCost = gCost;
    }

    public int getHCost() {
        return hCost;
    }

    public void setHCost(int hCost) {
        this.hCost = hCost;
    }

    public int getFCost() {
        return fCost;
    }

    public void setFCost(int fCost) {
        this.fCost = fCost;
    }

    public PathNode getCameFromNode() {
        return cameFromNode;
    }

    public void setCameFromNode(PathNode cameFromNode) {
        this.cameFromNode = cameFromNode;
    }

    @Override
    public String toString() {
        return String.format("[%d, %d]", x, y);
    }

    public boolean isWalkable() {
        return walkable;
    }
}
