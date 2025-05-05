package server.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.managers.CommandManager;
import server.requests.Request;
import server.response.Response;
import server.response.ResponseType;

import java.io.IOException;
import java.util.Map;

public class Help extends Command {
    private final Logger logger = LogManager.getLogger(Help.class);
    private final CommandManager commandManager;

    public Help(CommandManager commandManager) {
        super("help", "вывести справку по доступным командам");
        this.commandManager = commandManager;
    }

    @Override
    public Response execute(Request request) throws IOException {
        Map<String, Command> commands = commandManager.getCommands();
        StringBuilder msg = new StringBuilder(GREEN + "Справка по доступным командам:\n" + RESET);
        for (var command : commands.values()) {
            msg.append(command.getName()).append(" : ").append(command.getDescription()).append("\n");
        }
        return new Response(msg.toString(), ResponseType.PRINT_MESSAGE);
    }
}
