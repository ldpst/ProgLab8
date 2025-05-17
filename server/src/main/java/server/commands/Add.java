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

public class Add extends Command {
    private final Logger logger = LogManager.getLogger(Add.class);

    private final CollectionManager collectionManager;
    private final UDPDatagramChannel channel;


    public Add(CollectionManager collectionManager, UDPDatagramChannel channel) {
        super("add", "добавить новый элемент в коллекцию", "add");
        this.collectionManager = collectionManager;
        this.channel = channel;
    }

    @Override
    public Response execute(Request request) throws IOException {
        Movie movie = (Movie) request.getData();
        movie.setId(collectionManager.getAndIncreaseNextID());
        collectionManager.add(movie);
        return new Response(GREEN + "Элемент успешно добавлен\n" + RESET, ResponseType.PRINT_MESSAGE, request.getUID());
    }
}