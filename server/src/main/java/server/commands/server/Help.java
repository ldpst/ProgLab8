package server.commands.server;

import server.managers.RunManager;

import java.io.PrintStream;

public class Help extends Command {
    private final RunManager runManager;
    private final PrintStream stream;

    public Help(PrintStream stream, RunManager runManager) {
        super("help", "вывести справку по доступным командам");
        this.runManager = runManager;
        this.stream = stream;
    }

    @Override
    public void run(String[] args) {
        var commands = runManager.commands;
        stream.print("Справка по доступным командам:\n");
        for(Command command : commands.values()) {
            stream.print(command.getName() + " : " + command.getDescription() + "\n");
        }
    }
}
