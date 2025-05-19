package server.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.managers.CollectionManager;
import server.requests.Request;
import server.response.Response;
import server.response.ResponseType;
import server.server.UDPDatagramChannel;

public class RemoveById extends Command {
    private final CollectionManager collectionManager;
    private final UDPDatagramChannel channel;

    private final Logger logger = LogManager.getLogger(RemoveById.class);

    public RemoveById(CollectionManager collectionManager, UDPDatagramChannel channel) {
        super("remove_by_id id", "удалить элемент из коллекции по его id", "remove_by_id");
        this.collectionManager = collectionManager;
        this.channel = channel;
    }

    @Override
    public Response execute(Request request) {
        logger.debug("Команда выполняется...");
        String[] args = request.getMessage().split(" ");
        if (args.length != 2) {
            return new Response(RED + "Неверный формат команды\n" + RESET, ResponseType.ERROR, request.getUID());
        }
        int id;
        try {
            id = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            return new Response(RED + "Введённый id должен быть целым числом\n" + RESET, ResponseType.ERROR, request.getUID());
        }
        logger.debug("Команда выполнена");
        return new Response(collectionManager.removeById(id, request.getLogin()), ResponseType.PRINT_MESSAGE, request.getUID());
    }
}