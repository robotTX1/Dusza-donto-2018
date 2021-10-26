package com.dusza;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class IOHandler {
    private static final HashMap<Character, Character> charMap = new HashMap<>();
    private static final int labirintSizeX = 9;
    private static final int labirintSizeY = 9;
    private final Path path;

    static {
        charMap.put('*','█');
        charMap.put('█','*');
        charMap.put('.',' ');
        charMap.put(' ','.');
    }

    //constructors
    public IOHandler(Path path) {
        this.path = path;
    }

    //methods

    public char[][] readFile() {
        List<String> lines = new ArrayList<>();
        char[][] out = new char[labirintSizeY][labirintSizeX];

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String strCurrentLine;
            while ((strCurrentLine = reader.readLine()) != null) {
                lines.add(strCurrentLine);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int l = 0; l < lines.size(); l++) {
            char[] line = lines.get(l).toCharArray();
            for (int c = 0; c < line.length; c++) {
                out[l][c] = charMap.get(line[c]);
            }

        }



        return out;
    }

    public void saveFile(char[][] table) {
        // table karaktereinek visszakonvertálása

        for (int i = 0; i < labirintSizeY; i++) {
            for (int j = 0; j < labirintSizeX; j++) {
                table[i][j] = charMap.get(table[i][j]);
            }
        }

        // karakterek kiírása fileba:

        try(BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (char[] cA : table) {
                writer.write(cA);
                writer.write("\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // getters & setters

    public Path getPath() {
        return path;
    }

    public static int getLabirintSizeX() {
        return labirintSizeX;
    }

    public static int getLabirintSizeY() {
        return labirintSizeY;
    }
}