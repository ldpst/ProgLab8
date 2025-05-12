package server.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.managers.CommandManager;
import server.requests.Request;
import server.response.Response;
import server.response.ResponseType;
import server.utils.Languages;

import java.io.IOException;
import java.util.Map;

public class Help extends Command {
    private final Logger logger = LogManager.getLogger(Help.class);
    private final CommandManager commandManager;

    public Help(CommandManager commandManager) {
        super("help", "вывести справку по доступным командам", "help");
        this.commandManager = commandManager;
    }

    @Override
    public Response execute(Request request) throws IOException {
        Map<String, Command> commands = commandManager.getCommands();
        StringBuilder ru = new StringBuilder();
        StringBuilder sl = new StringBuilder();
        StringBuilder fr = new StringBuilder();
        StringBuilder es = new StringBuilder();
        for (var command : commands.values()) {
            if (command.getTask() != null) {
                ru.append(command.getTask()).append(" : ").append(Languages.get(command.getTask(), "Russian")).append("\n");
                sl.append(command.getTask()).append(" : ").append(Languages.get(command.getTask(), "Slovenian")).append("\n");
                fr.append(command.getTask()).append(" : ").append(Languages.get(command.getTask(), "French")).append("\n");
                es.append(command.getTask()).append(" : ").append(Languages.get(command.getTask(), "Spanish")).append("\n");
            }
        }
        Response response = new Response(ru.toString(), ResponseType.PRINT_MESSAGE);
        response.setTranslate(new String[]{ru.toString(), sl.toString(), fr.toString(), es.toString()});
        return response;
    }
}
