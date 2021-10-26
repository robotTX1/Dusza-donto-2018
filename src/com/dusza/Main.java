package com.dusza;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Labyrinth labyrinth = new Labyrinth();

        Path workDir = FileSystems.getDefault().getPath("Data");

        labyrinth.setLabyrinth(IOHandler.readFile(workDir.resolve("labirint.txt")));

        Tree tree = new Tree(labyrinth);
        for (char[] cA : tree.getLab()) {
            System.out.println(cA);
        }
        List<int[]> out = tree.getShortestPath(1,1);

        System.out.println(" ");
	    //CommandLineInterface cli = new CommandLineInterface(labyrinth, workDir);

        //cli.start();
    }
}
