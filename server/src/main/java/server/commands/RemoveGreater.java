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
import java.util.Objects;

public class RemoveGreater extends Command {
    private final CollectionManager collectionManager;
    private final UDPDatagramChannel channel;

    private final Logger logger = LogManager.getLogger(RemoveGreater.class);

    public RemoveGreater(CollectionManager collectionManager, UDPDatagramChannel channel) {
        super("remove_greater {element}", "удалить из коллекции все элементы, превышающие заданный", "remove_greater");
        this.collectionManager = collectionManager;
        this.channel = channel;
    }

    @Override
    public Response execute(Request request) throws IOException {
        Movie movie = (Movie) request.getData();
        int count = collectionManager.removeGreater(movie, request.getLogin());

        StringBuilder ru = new StringBuilder();
        StringBuilder sl = new StringBuilder();
        StringBuilder fr = new StringBuilder();
        StringBuilder es = new StringBuilder();

        ru.append(Objects.requireNonNull(Languages.get("deletedNElements", "Russian")).replace("{}", Integer.toString(count)));
        sl.append(Objects.requireNonNull(Languages.get("deletedNElements", "Slovenian")).replace("{}", Integer.toString(count)));
        fr.append(Objects.requireNonNull(Languages.get("deletedNElements", "French")).replace("{}", Integer.toString(count)));
        es.append(Objects.requireNonNull(Languages.get("deletedNElements", "Spanish")).replace("{}", Integer.toString(count)));

        Response response = new Response(ru.toString(), ResponseType.PRINT_MESSAGE, request.getUID());
        response.setTranslate(new String[]{ru.toString(), sl.toString(), fr.toString(), es.toString()});
        return response;
    }
}