package server.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.managers.CollectionManager;
import server.requests.Request;
import server.response.Response;
import server.response.ResponseType;
import server.server.UDPDatagramChannel;
import server.utils.Languages;

import java.io.IOException;

public class Clear extends Command {
    private final Logger logger = LogManager.getLogger(Clear.class);
    private final UDPDatagramChannel channel;
    private final CollectionManager collectionManager;

    public Clear(CollectionManager collectionManager, UDPDatagramChannel channel) {
        super("clear", "очистить коллекцию", "clear");
        this.collectionManager = collectionManager;
        this.channel = channel;
    }

    @Override
    public Response execute(Request request) throws IOException {
        collectionManager.clear(request.getLogin());

        StringBuilder ru = new StringBuilder();
        StringBuilder sl = new StringBuilder();
        StringBuilder fr = new StringBuilder();
        StringBuilder es = new StringBuilder();

        ru.append(Languages.get("collectionCleared", "Russian"));
        sl.append(Languages.get("collectionCleared", "Slovenian"));
        fr.append(Languages.get("collectionCleared", "French"));
        es.append(Languages.get("collectionCleared", "Spanish"));

        Response response = new Response(ru.toString(), ResponseType.PRINT_MESSAGE, request.getUID());
        response.setTranslate(new String[]{ru.toString(), sl.toString(), fr.toString(), es.toString()});
        return response;
    }
}