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

public class AddIfMax extends Command {
    private final Logger logger = LogManager.getLogger(AddIfMax.class);
    private final CollectionManager collectionManager;
    private final UDPDatagramChannel channel;

    public AddIfMax(CollectionManager collectionManager, UDPDatagramChannel channel) {
        super("add_if_max {element}", "добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции");
        this.collectionManager = collectionManager;
        this.channel = channel;
    }

    @Override
    public Response execute(Request request) throws IOException {
        logger.info("Команда выполняется...");
        Movie movie = (Movie) request.getData();
        Response response = new Response(collectionManager.addIfMax(movie), ResponseType.PRINT_MESSAGE);
        logger.info("Команда выполнена");
        return response;
    }
}