package com.dusza;

import java.util.List;

public class Node {
    private int x,y;
    private List<Node> children;


    //constructors
    public Node(int x, int y, List<Node> children) {
        this.x = x;
        this.y = y;
        this.children = children;
    }



    // getters & setters
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

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }
}
