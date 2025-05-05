package server.managers;

import server.commands.*;
import server.server.UDPDatagramChannel;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {
    private final Map<String, Command> commands = new HashMap<>();

    public CommandManager(CollectionManager collectionManager, UDPDatagramChannel channel) {
        commands.put("help", new Help(this));
        commands.put("show", new Show(collectionManager));
        commands.put("add", new Add(collectionManager, channel));
        commands.put("update", new Update(collectionManager, channel));
        commands.put("remove_by_id", new RemoveById(collectionManager, channel));
        commands.put("clear", new Clear(collectionManager, channel));
        commands.put("execute_script", new ExecuteScript());
        commands.put("exit", new Exit());
        commands.put("head", new Head(collectionManager));
        commands.put("add_if_max", new AddIfMax(collectionManager, channel));
        commands.put("remove_greater", new RemoveGreater(collectionManager, channel));
        commands.put("max_by_operator", new MaxByOperator(collectionManager));
        commands.put("count_by_operator", new CountByOperator(collectionManager, channel));
        commands.put("count_less_than_genre", new CountLessThanGenre(collectionManager, channel));
    }

    public Map<String, Command> getCommands() {
        return commands;
    }
}
