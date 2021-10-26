package com.dusza;

import javax.xml.stream.events.NotationDeclaration;
import java.util.ArrayList;
import java.util.List;

public class Tree {
    private List<Node> nodes;
    private char[][] lab;
    private static final char explored = 'X';
    private static final char unexplored = ' ';

    // constructors

    public Tree(Labyrinth labyrinth) {
        lab = new char[Labyrinth.LABYRINTH_HEIGHT][Labyrinth.LABYRINTH_WIDTH];
        for(int h = 0; h < Labyrinth.LABYRINTH_HEIGHT; h++) {
            for(int w = 0; w < Labyrinth.LABYRINTH_WIDTH; w++) {
                lab[h][w] = labyrinth.getLabyrinth()[h][w];
            }
        }
        generateTree(labyrinth.getExitX(), labyrinth.getExitY());
    }



    // methods

    public List<int[]> getShortestPath(int x, int y) {
        List<int[]> out = new ArrayList<>();

        // legközelebbi elágazás megtalálása
        int posX = x;
        int posY = y;
        List<int[]> free;
        int[] breakPoint = {x,y};
        boolean nodeFound = false;
        int i = 0;

        while (! nodeFound) {
            do {
                lab[posY][posX] = unexplored;
                free = getFreeSpaces(posX, posY, explored);
                out.add(new int[]{posX, posY});
                if (free.size() == 1) {
                    posX = free.get(0)[0];
                    posY = free.get(0)[1];
                }
                else if (free.size() == 0) {
                    out.clear();
                    posX = breakPoint[0];
                    posY = breakPoint[1];
                }


            } while (free.size() == 1);


            while (i < nodes.size() && !(nodes.get(i).getX() == posX && nodes.get(i).getY() == posY)) {
                i++;
            }

            if (i < nodes.size()) {nodeFound = true;} else {
                posX = free.get(0)[0];
                posY = free.get(0)[1];
                i = 0;
            }
        }

        while (i >0) {
            Node nd = nodes.get(i);
            posX = nd.getPx();
            posY = nd.getPy();

            do {
                lab[posY][posX] = unexplored;
                free = getFreeSpaces(posX, posY, explored);
                out.add(new int[]{posX, posY});
                if (free.size()==1) {
                    posX = free.get(0)[0];
                    posY = free.get(0)[1];
                }


            } while(free.size()==1);
            i = nd.getParentIndex();
        }

        return out;
        /*
        i = 0;
        while (i < out.size() && !(out.get(i)[0] == x && out.get(i)[1]==y)) {
            i++;
        }
        if (i == out.size()) {
            subListofOut.addAll(out);

        } else {

            return out;
        }

         */
    }


    private void generateTree(int exitX, int exitY) {
        nodes = new ArrayList<>();

        // Első node elhelyezése az exit-hez:
        nodes.add(new Node(exitX, exitY, -1, 0,0));

        // A kijárattól indulva a labirintus bejárása és az elágazásoknál újabb node-ok elhelyezése
        int posX = exitX;
        int px = -1;
        int py = -1;
        int posY = exitY;
        int parentIndex = 0;
        List<int[]> free;
        boolean exit = false;


        while (!exit) {
            lab[posY][posX] = explored; // bejárva
            free= getFreeSpaces(posX, posY, unexplored);

            // ha csak egy irányba lehet tovább menni, akkor arra lépünk
            if (free.size() == 1) {
                px = posX;
                py = posY;
                posX = free.get(0)[0];
                posY = free.get(0)[1];

            }
            else if (free.size() == 0) {
                // backtrack az előző még szabad kimenettel rendelkező nodehoz
                while (isCrossingExplored(nodes.get(parentIndex))){
                    parentIndex = nodes.get(parentIndex).getParentIndex();
                    if (parentIndex == -1) {
                        exit=true;
                        break;
                    }
                }
                if (parentIndex != -1) {
                    free = getFreeSpaces(nodes.get(parentIndex).getX(), nodes.get(parentIndex).getY(), unexplored);
                    px = posX;
                    py = posY;
                    posX = free.get(0)[0];
                    posY = free.get(0)[1];
                }
            }
            else {
                nodes.add(new Node(posX, posY, parentIndex, px, py));
                parentIndex = nodes.size()-1;
                // tovább haladni az egyik szabad úton:
                px = posX;
                py = posY;
                posX = free.get(0)[0];
                posY = free.get(0)[1];
            }
        }

    }

    private boolean isCrossingExplored(int posX, int posY) {
        boolean right = posX + 1 >= lab[0].length || lab[posY][posX + 1] != unexplored;
        boolean up = posY + 1 >= lab.length || lab[posY+1][posX] != unexplored;
        boolean left = posX <= 0 || lab[posY][posX - 1] != unexplored;
        boolean down = posY <= 0 || lab[posY-1][posX] != unexplored;
        return right && up && left && down;
    }

    private boolean isCrossingExplored(Node nd) {
        return isCrossingExplored(nd.getX(), nd.getY());
    }

    private List<int[]> getFreeSpaces(int posX, int posY, char unExp) {
        List<int[]> out = new ArrayList<>();
        if (posX+1 < lab[0].length && lab[posY][posX+1] == unExp) out.add(new int[] {posX+1, posY});
        if (posX > 0 && lab[posY][posX-1] == unExp) out.add(new int[] {posX-1, posY});
        if (posY+1 < lab.length && lab[posY+1][posX] == unExp) out.add(new int[] {posX, posY+1});
        if (posY > 0 && lab[posY-1][posX] == unExp) out.add(new int[] {posX, posY-1});

        return out;
    }

    //getters & setters

    public List<Node> getNodes() {
        return nodes;
    }

    public char[][] getLab() {
        return lab;
    }
}
