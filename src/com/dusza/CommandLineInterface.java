package com.dusza;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
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
                    IOHandler.saveFile(workDir.resolve(CURRENT_SAVE_NAME), Arrays.copyOf(in, in.length));
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

        Runnable balra = () -> labyrinth.getPlayer().turnLeft();
        Runnable jobbra = () -> labyrinth.getPlayer().turnRight();
        Runnable elore = () -> {
            if(!labyrinth.getPlayer().forward()) {
                System.out.println("Erre nem tudsz lépni!");
                System.out.println("Folytatáshoz nyomj meg egy gombot!");
                input.nextLine();
            }
        };

        gameCommandList.add(new Command("balra", "A karakter balra fordul", balra));
        gameCommandList.add(new Command("b", "A karakter balra fordul, rövid változat", balra));
        gameCommandList.add(new Command("jobbra", "A karakter jobbra fordul", jobbra));
        gameCommandList.add(new Command("j", "A karakter jobbra fordul, rövid változat", jobbra));
        gameCommandList.add(new Command("elore", "A karakter előre lép, ha tud", elore));
        gameCommandList.add(new Command("e", "A karakter előre lép, ha tud, rövid változat", elore));

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

            if(labyrinth.isEscaped()) {
                Tree tree = new Tree(labyrinth);
                List<int[]> list = tree.getShortestPath(1,1);

                System.out.println("\n\n\n\nGratulálok, nyertél!");
                System.out.printf("Lépések száma: %d\n", labyrinth.getPlayer().getMoves());
                System.out.printf("Legrövidebb út hossza: %d\n", list.size()-1);

                System.out.println("Nyomj meg egy gombot a folytatáshoz.");
                input.nextLine();

                System.out.println("\n\n\n");
                help(menuCommandList);

                break;
            }

            if(command.equals("help")) {
                help(gameCommandList);
                System.out.println("Nyomj meg egy gombot a folytatáshoz.");
                input.nextLine();
            }

            if(exit.getName().equals(command)) {
                System.out.println("Játék befejezve.\n");
                help(menuCommandList);
                break;
            }
        }
    }

    public void setLabyrinth(Labyrinth labyrinth) {
        this.labyrinth = labyrinth;
    }
}
