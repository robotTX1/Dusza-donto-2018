package com.dusza;

import java.util.List;

public class Node {
    private int x,y;
    private int parentIndex;

    public Node(int x, int y, int parentIndex) {
        this.x = x;
        this.y = y;
        this.parentIndex = parentIndex;
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
}
