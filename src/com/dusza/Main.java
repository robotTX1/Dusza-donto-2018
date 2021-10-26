package com.dusza;

import java.nio.file.FileSystems;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) {
        Labyrinth labyrinth = new Labyrinth();

        Path workDir = FileSystems.getDefault().getPath("Data");

	    CommandLineInterface cli = new CommandLineInterface(labyrinth, workDir);

        cli.start();
    }
}
