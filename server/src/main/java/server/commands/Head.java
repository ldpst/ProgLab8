package server.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.managers.CollectionManager;
import server.object.Movie;
import server.requests.Request;
import server.response.Response;
import server.response.ResponseType;

import java.io.IOException;

public class Head extends Command {
    private final Logger logger = LogManager.getLogger(Head.class);
    private final CollectionManager collectionManager;

    public Head(CollectionManager collectionManager) {
        super("head", "вывести первый элемент коллекции", "head");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) throws IOException {
        logger.debug("Команда выполняется...");
        Movie head = collectionManager.getHead();
        logger.debug("Команда выполнена");
        if (head == null) {
            return new Response(GREEN + "Коллекция пуста\n" + RESET, ResponseType.PRINT_MESSAGE, request.getUID());
        }
        return new Response(GREEN + "Первый элемент:\n" + RESET + head, ResponseType.PRINT_MESSAGE, request.getUID());
    }
}