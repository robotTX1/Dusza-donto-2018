package com.dusza;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CommandLineInterface {
    private static List<Command> menuCommandList = new ArrayList<>();
    private static List<Command> gameCommandList = new ArrayList<>();

    static {
        menuCommandList.add(new Command("beolvas", "Létező labirintus betöltése.", () -> {

        }));

        menuCommandList.add(new Command("generalas", "Új labirintus generálása.", () -> {

        }));

        menuCommandList.add(new Command("betolt", "Megkezdett játék betöltése.", () -> {

        }));

        menuCommandList.add(new Command("kilepes", "Kilépés a játékból.", () -> System.out.println("Köszönöm, hogy használtad a játékot!")));


    }

    private Labyrinth labyrinth;
    private Scanner input = new Scanner(System.in);

    public void start() {
        System.out.println("Üdv a Dusza labirintus játékban!\n");
        help(menuCommandList);

        Command exit = menuCommandList.get(menuCommandList.size()-1);

        String command;
        while(true) {
            System.out.print(">");
            command = input.nextLine().trim().toLowerCase();

            for(Command c : menuCommandList) {
                if(c.getName().equals(command)) {
                    c.run();
                    break;
                }
            }

            if(command.equals("help")) help(menuCommandList);

            if(exit.getName().equals(command)) {
                exit.run();
                break;
            }
        }
    }

    private void help(List<Command> commandList) {
        for(Command c : commandList) {
            System.out.printf("%s\t-\t%s\n", c.getName(), c.getDescription());
        }
    }

    public void startGame() {

    }
}
