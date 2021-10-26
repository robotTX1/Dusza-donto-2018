package com.dusza;

import java.util.ArrayList;
import java.util.List;

public class Tree {
    private List<Node> nodes;
    private char[][] lab;
    private static final char explored = 'X';
    private static final char unexplored = ' ';

    // constructors

    public Tree(Labyrinth labyrinth) {
        lab = labyrinth.getLabyrinth();
        generateTree(labyrinth.getExitX(), labyrinth.getExitY());
    }



    // methods
    private char[][] generateTree(int exitX, int exitY) {
        nodes = new ArrayList<>();

        // Első node elhelyezése az exit-hez:
        nodes.add(new Node(exitX, exitY, -1));

        // A kijárattól indulva a labirintus bejárása és az elágazásoknál újabb node-ok elhelyezése
        int posX = exitX;
        int posY = exitY;
        int parentIndex = 0;
        List<int[]> free;

        while (true) {
            lab[posX][posY] = 'X'; // bejárva
            free= getFreeSpaces(posX, posY);

            // ha csak egy irányba lehet tovább menni, akkor arra lépünk
            if (free.size() == 1) {
                posX = free.get(0)[0];
                posY = free.get(0)[1];
            }
            else if (free.size() == 0) {
                // backtrack az előző még szabad kimenettel rendelkező nodehoz
                do {
                    parentIndex = nodes.get(parentIndex).getParentIndex();
                    if (parentIndex == -1) break;
                }
                while (isCrossingExplored(nodes.get(parentIndex)));
            }
            else {
                nodes.add(new Node(posX, posY, parentIndex));
                // tovább haladni az egyik szabad úton:
                posX = free.get(0)[0];
                posY = free.get(0)[1];
            }
        }

    }

    private boolean isCrossingExplored(int posX, int posY) {
        return lab[posY][posX+1] != unexplored && lab[posY][posX-1] != unexplored && lab[posY+1][posX] != unexplored && lab[posY-1][posX] != unexplored;
    }

    private boolean isCrossingExplored(Node nd) {
        return isCrossingExplored(nd.getX(), nd.getY());
    }

    private List<int[]> getFreeSpaces(int posX, int posY) {
        List<int[]> out = new ArrayList<>();
        if (lab[posY][posX+1] == unexplored) out.add(new int[] {posX+1, posY});
        if (lab[posY][posX-1] == unexplored) out.add(new int[] {posX-1, posY});
        if (lab[posY+1][posX] == unexplored) out.add(new int[] {posX, posY+1});
        if (lab[posY-1][posX] == unexplored) out.add(new int[] {posX, posY-1});

        return out;
    }

    //getters & setters

}
