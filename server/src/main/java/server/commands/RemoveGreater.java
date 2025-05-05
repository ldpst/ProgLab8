package server.commands;

import server.object.Movie;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.managers.CollectionManager;
import server.requests.Request;
import server.response.Response;
import server.response.ResponseType;
import server.server.UDPDatagramChannel;

import java.io.IOException;

public class RemoveGreater extends Command {
    private final CollectionManager collectionManager;
    private final UDPDatagramChannel channel;

    private final Logger logger = LogManager.getLogger(RemoveGreater.class);

    public RemoveGreater(CollectionManager collectionManager, UDPDatagramChannel channel) {
        super("remove_greater {element}", "удалить из коллекции все элементы, превышающие заданный");
        this.collectionManager = collectionManager;
        this.channel = channel;
    }

    @Override
    public Response execute(Request request) throws IOException {
        logger.info("Команда выполняется...");
        Movie movie = (Movie) request.getData();
        int count = collectionManager.removeGreater(movie, request.getLogin());
        logger.info("Команда выполнена");
        return new Response(GREEN + "Удалено " + count + " элементов\n" + RESET, ResponseType.PRINT_MESSAGE);
    }
}