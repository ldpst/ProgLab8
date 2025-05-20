package server.commands;

import server.object.MovieGenre;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.managers.CollectionManager;
import server.requests.Request;
import server.response.Response;
import server.response.ResponseType;
import server.server.UDPDatagramChannel;
import server.utils.Languages;

import java.io.IOException;

public class CountLessThanGenre extends Command {
    private final CollectionManager collectionManager;
    private final UDPDatagramChannel channel;
    private final Logger logger = LogManager.getLogger(CountLessThanGenre.class);

    public CountLessThanGenre(CollectionManager collectionManager, UDPDatagramChannel channel) {
        super("count_less_than_genre genre", "вывести количество элементов, значение поля genre которых меньше заданного", "count_less_than_genre");
        this.collectionManager = collectionManager;
        this.channel = channel;
    }

    @Override
    public Response execute(Request request) throws IOException {
        logger.debug("Команда выполняется...");
        MovieGenre genre = (MovieGenre) request.getData();
        int count = collectionManager.countLessThanGenre(genre);

        StringBuilder ru = new StringBuilder();
        StringBuilder sl = new StringBuilder();
        StringBuilder fr = new StringBuilder();
        StringBuilder es = new StringBuilder();

        ru.append(Languages.get("elementLessThanGenre", "Russian")).append(": ").append(count);
        sl.append(Languages.get("elementLessThanGenre", "Slovenian")).append(": ").append(count);
        fr.append(Languages.get("elementLessThanGenre", "French")).append(": ").append(count);
        es.append(Languages.get("elementLessThanGenre", "Spanish")).append(": ").append(count);

        Response response = new Response(ru.toString(), ResponseType.PRINT_MESSAGE, request.getUID());
        response.setTranslate(new String[]{ru.toString(), sl.toString(), fr.toString(), es.toString()});
        logger.debug("Команда выполнена");
        return response;
    }
}
