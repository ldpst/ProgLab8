package server.commands;

import server.object.Movie;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.managers.CollectionManager;
import server.requests.Request;
import server.response.Response;
import server.response.ResponseType;
import server.server.UDPDatagramChannel;
import server.utils.Languages;

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
        movie.setId(collectionManager.getNextID());
        collectionManager.add(movie);

        StringBuilder ru = new StringBuilder();
        StringBuilder sl = new StringBuilder();
        StringBuilder fr = new StringBuilder();
        StringBuilder es = new StringBuilder();

        ru.append(Languages.get("elementAdded", "Russian"));
        sl.append(Languages.get("elementAdded", "Slovenian"));
        fr.append(Languages.get("elementAdded", "French"));
        es.append(Languages.get("elementAdded", "Spanish"));

        Response response = new Response(ru.toString(), ResponseType.PRINT_MESSAGE, request.getUID());
        response.setTranslate(new String[]{ru.toString(), sl.toString(), fr.toString(), es.toString()});

        System.out.println(response.getMessage());

        return response;
    }
}