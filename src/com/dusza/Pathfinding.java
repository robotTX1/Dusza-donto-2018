package com.dusza;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Pathfinding {
    private static final int STRAIGHT_MOVE_COST = 1;

    private final PathNode[][] grid;
    private List<PathNode> openList;
    private List<PathNode> closedList;

    public Pathfinding(char[][] inputGrid) {
        this.grid = new PathNode[inputGrid.length][inputGrid[0].length];

        for(int h = 0; h < inputGrid.length; h++) {
            for(int w = 0; w < inputGrid[0].length; w++) {
                grid[h][w] = new PathNode(w, h, inputGrid[h][w] == Labyrinth.PATH_CHAR);
            }
        }
    }

    public List<PathNode> findPath(int startX, int startY, int endX, int endY) {
        PathNode startNode = grid[startY][startX];
        PathNode endNode = grid[endY][endX];

        openList = new ArrayList<>();
        closedList = new ArrayList<>();

        openList.add(startNode);

        // Initialize grid
        for(int h = 0; h < grid.length; h++) {
            for(int w = 0; w < grid[0].length; w++) {
                PathNode node = grid[h][w];
                node.setGCost(Integer.MAX_VALUE);
                node.calculateFCost();
                node.setCameFromNode(null);
            }
        }

        startNode.setGCost(0);
        startNode.setHCost(calculateDistanceCost(startNode, endNode));
        startNode.calculateFCost();

        while(openList.size() > 0) {
            PathNode currentNode = getLowestFCostNode();

            if(currentNode == endNode) {
                // reached final node
                return calculatePath(endNode);
            }

            openList.remove(currentNode);
            closedList.add(currentNode);

            for(PathNode neighbourNode : getNeighbourList(currentNode)) {
                if(closedList.contains(neighbourNode)) continue;
                if(!neighbourNode.isWalkable()) {
                    closedList.add(neighbourNode);
                    continue;
                }

                int tentativeGCost = currentNode.getGCost() + calculateDistanceCost(currentNode, neighbourNode);
                if(tentativeGCost < neighbourNode.getGCost()) {
                    neighbourNode.setCameFromNode(currentNode);
                    neighbourNode.setGCost(tentativeGCost);
                    neighbourNode.setHCost(calculateDistanceCost(neighbourNode, endNode));
                    neighbourNode.calculateFCost();

                    if(!openList.contains(neighbourNode)) {
                        openList.add(neighbourNode);
                    }
                }
            }

        }

        // Out of nodes to search in openList, no path found

        return null;
    }

    private List<PathNode> getNeighbourList(PathNode node) {
        List<PathNode> neighbourList = new ArrayList<>();

        // Left
        if(node.getX() - 1 >= 0) neighbourList.add(grid[node.getY()][node.getX() - 1]);
        // Right
        if(node.getX() + 1 < grid[0].length) neighbourList.add(grid[node.getY()][node.getX() + 1]);
        // Up
        if(node.getY() - 1 >= 0) neighbourList.add(grid[node.getY() - 1][node.getX()]);
        // Down
        if(node.getY() + 1 < grid.length) neighbourList.add(grid[node.getY() + 1][node.getX()]);

        return neighbourList;
    }

    private List<PathNode> calculatePath(PathNode endNode) {
        List<PathNode> path = new ArrayList<>();
        path.add(endNode);
        PathNode currentNode = endNode;

        while(currentNode.getCameFromNode() != null) {
            path.add(currentNode.getCameFromNode());
            currentNode = currentNode.getCameFromNode();
        }

        Collections.reverse(path);

        return path;
    }

    private PathNode getLowestFCostNode() {
        return openList.stream()
                .sorted(Comparator.comparingInt(PathNode::getFCost))
                .collect(Collectors.toList())
                .get(0);
    }

    private int calculateDistanceCost(PathNode a, PathNode b) {
        int xDistance = Math.abs(a.getX() - b.getX());
        int yDistance = Math.abs(a.getY() - b.getY());
        int remaining = Math.abs(xDistance - yDistance);
        return STRAIGHT_MOVE_COST * remaining;
    }
}
