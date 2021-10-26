package com.dusza;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CommandLineInterface {
    private static final String CURRENT_SAVE_NAME = "currentLabyrinth.txt";

    private final List<Command> menuCommandList = new ArrayList<>();
    private final List<Command> gameCommandList = new ArrayList<>();

    public CommandLineInterface(Labyrinth labyrinth, Path workDir) {
        this.labyrinth = labyrinth;
        this.workDir = workDir;

        // Menu Commands

        menuCommandList.add(new Command("beolvas", "Létező labirintus betöltése.", () -> {
            List<Path> files = IOHandler.getFiles(workDir);

            for(int i=0; i<files.size(); i++) {
                System.out.printf("%d. %s\n", i+1, files.get(i).getFileName());
            }

            System.out.printf("%d. Vissza\n", files.size()+1);

            String command;

            while(true) {
                System.out.print("> ");
                command = input.nextLine().trim();

                if(!command.matches("^\\d$")) {
                    System.out.println("Hibás bemenet! Csak egy számot adj meg!");
                    continue;
                }

                int commandNumber = Integer.parseInt(command);

                if(commandNumber > 0 && commandNumber <= files.size()) {
                    char[][] in = IOHandler.readFile(files.get(commandNumber - 1));
                    IOHandler.saveFile(workDir.resolve(CURRENT_SAVE_NAME), in);
                    labyrinth.setLabyrinth(in);
                    System.out.println("Beolvasás sikeres!");
                    return;
                }

                if(commandNumber == files.size()+1) {
                    help(menuCommandList);
                    return;
                }
            }

        }));

        menuCommandList.add(new Command("generalas", "Új labirintus generálása.", labyrinth::generate));

        menuCommandList.add(new Command("betolt", "Megkezdett játék betöltése.", () -> {
            List<Path> files = IOHandler.getFiles(workDir);

            boolean found = false;
            for(Path p : files) {
                if(p.getFileName().toString().equals(CURRENT_SAVE_NAME)){
                    found = true;
                    break;
                }
            }

            if(found) {
                char[][] in = IOHandler.readFile(workDir.resolve(CURRENT_SAVE_NAME));
                labyrinth.setLabyrinth(in);
                System.out.println("Játék betöltve!");
            } else {
                System.out.println("Nem volt még megkezdett játékod!");
            }
        }));

        menuCommandList.add(new Command("kilepes", "Kilépés a játékból.", () -> System.out.println("Köszönöm, hogy használtad a játékot!")));

        // Game Commands

        gameCommandList.add(new Command("", "", () -> {}));
        gameCommandList.add(new Command("kilepes", "Vissza a menübe, játék befejezése.", () -> {

        }));

    }

    private Labyrinth labyrinth;
    private final Scanner input = new Scanner(System.in);
    private Path workDir;

    public void start() {
        System.out.println("Üdv a Dusza labirintus játékban!\n");
        help(menuCommandList);

        Command exit = menuCommandList.get(menuCommandList.size()-1);

        String command;
        while(true) {
            System.out.print("> ");
            command = input.nextLine().trim().toLowerCase();

            for(Command c : menuCommandList) {
                if(c == exit) continue;
                if(c.getName().equals(command)) {
                    c.run();
                    startGame();
                    break;
                }
            }

            if(command.equals("help")) help(menuCommandList);

            if(exit.getName().equals(command)) {
                exit.run();
                break;
            }
        }

        input.close();
    }

    private void help(List<Command> commandList) {
        for(Command c : commandList) {
            System.out.printf("%s\t-\t%s\n", c.getName(), c.getDescription());
        }
        System.out.println();
    }

    public void startGame() {
        String command;

        Command exit = gameCommandList.get(gameCommandList.size()-1);

        while(true) {
            System.out.println("\n\n\n\n\n\n\n\n");
            labyrinth.display();
            System.out.println();

            System.out.print("> ");
            command = input.nextLine().trim().toLowerCase();

            for(Command c : gameCommandList) {
                if(c == exit) continue;
                if(c.getName().equals(command)) {
                    c.run();
                    break;
                }
            }

            if(exit.getName().equals(command)) {
                System.out.println("Játék befejezve.");


                break;
            }
        }




    }

    public void setLabyrinth(Labyrinth labyrinth) {
        this.labyrinth = labyrinth;
    }
}
