package server.commands;

import server.object.MovieGenre;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.managers.CollectionManager;
import server.requests.Request;
import server.response.Response;
import server.response.ResponseType;
import server.server.UDPDatagramChannel;

import java.io.IOException;

public class CountLessThanGenre extends Command {
    private final CollectionManager collectionManager;
    private final UDPDatagramChannel channel;
    private final Logger logger = LogManager.getLogger(CountLessThanGenre.class);

    public CountLessThanGenre(CollectionManager collectionManager, UDPDatagramChannel channel) {
        super("count_less_than_genre genre", "вывести количество элементов, значение поля genre которых меньше заданного");
        this.collectionManager = collectionManager;
        this.channel = channel;
    }

    @Override
    public Response execute(Request request) throws IOException {
        logger.info("Команда выполняется...");
        MovieGenre genre = (MovieGenre) request.getData();
        int count = collectionManager.countLessThanGenre(genre);
        logger.info("Команда выполнена");
        return new Response(GREEN + "Элементов с genre меньше заданного: " + count + "\n" + RESET, ResponseType.PRINT_MESSAGE);
    }
}
