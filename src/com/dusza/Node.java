package com.dusza;

import java.util.List;

public class Node {
    private int x,y;
    private int parentIndex;
    private int px, py;

    public Node(int x, int y, int parentIndex, int px, int py) {
        this.x = x;
        this.y = y;
        this.parentIndex = parentIndex;
        this.py = py;
        this.px = px;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getParentIndex() {
        return parentIndex;
    }

    public void setParentIndex(int parent) {
        this.parentIndex = parent;
    }

    public int getPx() {
        return px;
    }

    public int getPy() {
        return py;
    }
}
