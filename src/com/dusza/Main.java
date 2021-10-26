package com.dusza;

import java.nio.file.Path;

public class Main {

    public static void main(String[] args) {
        IOHandler io = new IOHandler(Path.of("Data/labirint.txt"));

        char[][] lab = io.readFile();

        for (char[] ca : lab) {
            System.out.println(ca);
        }

        io.saveFile(lab);
    }
}
