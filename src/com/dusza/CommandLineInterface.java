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

    private final Labyrinth labyrinth;
    private final Scanner input = new Scanner(System.in);
    private final Path workDir;
    private Pathfinding pathfinding;

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

        menuCommandList.add(new Command("generalas", "Új labirintus generálása.", () -> {
            labyrinth.generateNewLabyrinth();
            IOHandler.saveFile(workDir.resolve(CURRENT_SAVE_NAME), labyrinth.getLabyrinth());
        }));

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

        Runnable left = () -> labyrinth.getPlayer().turnLeft();
        Runnable right = () -> labyrinth.getPlayer().turnRight();
        Runnable forward = () -> {
            if(!labyrinth.getPlayer().forward()) {
                System.out.println("Erre nem tudsz lépni!");
                System.out.println("Folytatáshoz nyomj meg egy gombot!");
                input.nextLine();
            }
        };

        Runnable automatic = () -> {
            Player player = labyrinth.getPlayer();
            List<PathNode> route = pathfinding.findPath(player.getX(), player.getY(), labyrinth.getExitX(), labyrinth.getExitY());

            PathNode nextStep = route.get(1);

            int moveDirX = player.getX() - nextStep.getX();
            int moveDirY = player.getY() - nextStep.getY();

            int dir = 0;
            if(moveDirX == 1) dir = 3;
            if(moveDirX == -1) dir = 1;
            if(moveDirY == 1) dir = 0;
            if(moveDirY == -1) dir = 2;

            if(dir != player.getPlayerDirectionInt()) {
                player.turn(dir);
                displayLabyrinth();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            player.forward();
        };

        Runnable fullyAutomatic = () -> {
            while(!labyrinth.isEscaped()) {
                automatic.run();
                displayLabyrinth();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        gameCommandList.add(new Command("balra", "A karakter balra fordul", left));
        gameCommandList.add(new Command("b", "A karakter balra fordul, rövid változat", left));
        gameCommandList.add(new Command("jobbra", "A karakter jobbra fordul", right));
        gameCommandList.add(new Command("j", "A karakter jobbra fordul, rövid változat", right));
        gameCommandList.add(new Command("elore", "A karakter előre lép, ha tud", forward));
        gameCommandList.add(new Command("e", "A karakter előre lép, ha tud, rövid változat", forward));
        gameCommandList.add(new Command("automata", "A karakter lép egyet a kijárat felé.", automatic));
        gameCommandList.add(new Command("a", "A karakter lép egyet a kijárat felé, rövid változat.", automatic));
        gameCommandList.add(new Command("fullAutomata", "A karakter végig megy a labirintuson", fullyAutomatic));
        gameCommandList.add(new Command("fa", "A karakter végig megy a labirintuson, rövid verzió", fullyAutomatic));

        gameCommandList.add(new Command("kilepes", "Vissza a menübe, játék befejezése.", () -> {}));
    }

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

        pathfinding = new Pathfinding(labyrinth.getLabyrinth());
        int fastestRoute = pathfinding.findPath(Labyrinth.START_X, Labyrinth.START_Y, labyrinth.getExitX(), labyrinth.getExitY()).size() - 1;

        while(true) {
            displayLabyrinth();

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
                System.out.println("\n\n\n\nGratulálok, nyertél!");
                System.out.printf("Lépések száma: %d\n", labyrinth.getPlayer().getMoves());
                System.out.printf("Legrövidebb út hossza: %d\n", fastestRoute);

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

    private void displayLabyrinth() {
        Player player = labyrinth.getPlayer();
        int fastestRoute = pathfinding.findPath(player.getX(), player.getY(), labyrinth.getExitX(), labyrinth.getExitY()).size() - 1;

        System.out.println("\n\n\n\n\n\n\n\n");
        labyrinth.display();

        System.out.printf("Legrövidebb út: %d\n", fastestRoute);
    }
}
