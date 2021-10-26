package com.dusza;

import java.util.ArrayList;
import java.util.List;

public class Tree {
    private List<Node> nodes;

    // constructors

    public Tree(Labyrinth labyrinth) {
        this.nodes = generateTree(labyrinth.getLabyrinth());
    }



    // methods
    private List<Node> generateTree(char[][] lab) {
        List<Node> nodes = new ArrayList<>();


        return nodes;

    }

    //getters & setters

}
