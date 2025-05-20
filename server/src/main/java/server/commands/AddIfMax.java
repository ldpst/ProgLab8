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

public class AddIfMax extends Command {
    private final Logger logger = LogManager.getLogger(AddIfMax.class);
    private final CollectionManager collectionManager;
    private final UDPDatagramChannel channel;

    public AddIfMax(CollectionManager collectionManager, UDPDatagramChannel channel) {
        super("add_if_max {element}", "добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции", "add_if_max");
        this.collectionManager = collectionManager;
        this.channel = channel;
    }

    @Override
    public Response execute(Request request) throws IOException {
        Movie movie = (Movie) request.getData();

        StringBuilder ru = new StringBuilder();
        StringBuilder sl = new StringBuilder();
        StringBuilder fr = new StringBuilder();
        StringBuilder es = new StringBuilder();

        if (collectionManager.addIfMax(movie)) {
            ru.append(Languages.get("elementAdded", "Russian"));
            sl.append(Languages.get("elementAdded", "Slovenian"));
            fr.append(Languages.get("elementAdded", "French"));
            es.append(Languages.get("elementAdded", "Spanish"));
        }
        else {
            ru.append(Languages.get("elementNotAdded", "Russian"));
            sl.append(Languages.get("elementNotAdded", "Slovenian"));
            fr.append(Languages.get("elementNotAdded", "French"));
            es.append(Languages.get("elementNotAdded", "Spanish"));
        }
        Response response = new Response(ru.toString(), ResponseType.PRINT_MESSAGE, request.getUID());
        response.setTranslate(new String[]{ru.toString(), sl.toString(), fr.toString(), es.toString()});
        return response;
    }
}