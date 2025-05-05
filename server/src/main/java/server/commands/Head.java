package server.commands;

import server.object.Movie;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.managers.CollectionManager;
import server.requests.Request;
import server.response.Response;
import server.response.ResponseType;

import java.io.IOException;

public class Head extends Command {
    private final Logger logger = LogManager.getLogger(Head.class);
    private final CollectionManager collectionManager;

    public Head(CollectionManager collectionManager) {
        super("head", "вывести первый элемент коллекции");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) throws IOException {
        logger.info("Команда выполняется...");
        Movie head = collectionManager.getHead();
        logger.info("Команда выполнена");
        if (head == null) {
            return new Response(GREEN + "Коллекция пуста\n" + RESET, ResponseType.PRINT_MESSAGE);
        }
        return new Response(GREEN + "Первый элемент:\n" + RESET + head, ResponseType.PRINT_MESSAGE);
    }
}