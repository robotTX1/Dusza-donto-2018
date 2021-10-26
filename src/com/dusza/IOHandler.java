package com.dusza;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class IOHandler {
    private static final HashMap<Character, Character> charMap = new HashMap<>();

    static {
        charMap.put('*','█');
        charMap.put('█','*');
        charMap.put('.',' ');
        charMap.put(' ','.');
    }

    //methods

    public static char[][] readFile(Path path) {
        List<String> lines = new ArrayList<>();
        char[][] out = new char[Labyrinth.LABYRINTH_HEIGHT][Labyrinth.LABYRINTH_WIDTH];

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

    public static void saveFile(Path path, char[][] table) {
        // table karaktereinek visszakonvertálása

//        for (int i = 0; i < Labyrinth.LABYRINTH_HEIGHT; i++) {
//            for (int j = 0; j < Labyrinth.LABYRINTH_WIDTH; j++) {
//                table[i][j] = charMap.get(table[i][j]);
//            }
//        }

        // karakterek kiírása fileba:

        try(BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (char[] charArr : table) {
                for(char c : charArr) {
                    writer.write(charMap.get(c));
                }
                writer.write("\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Path> getFiles(Path dir) {
        List<Path> result = new ArrayList<>();

        try(DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for(Path p : stream) {
                if(!Files.isDirectory(p)) result.add(p);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}