package server.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.managers.CollectionManager;
import server.object.Movie;
import server.requests.Request;
import server.response.Response;
import server.response.ResponseType;
import server.server.UDPDatagramChannel;
import server.utils.Languages;

import java.io.IOException;

public class Update extends Command {
    private final CollectionManager collectionManager;
    private final UDPDatagramChannel channel;

    private final Logger logger = LogManager.getLogger(Update.class);

    public Update(CollectionManager collectionManager, UDPDatagramChannel channel) {
        super("update id {element} ", "обновить значение элемента коллекции, id которого равен заданному", "update");
        this.collectionManager = collectionManager;
        this.channel = channel;
    }

    @Override
    public Response execute(Request request) throws IOException {
        logger.debug("Выполнение команды...");

        StringBuilder ru = new StringBuilder();
        StringBuilder sl = new StringBuilder();
        StringBuilder fr = new StringBuilder();
        StringBuilder es = new StringBuilder();

        String[] message = request.getMessage().split(" ");
        int id = Integer.parseInt(message[1]);
        if (collectionManager.checkIfIdExists(id)) {
            Movie newMovie = (Movie) request.getData();
            newMovie.setId(id);
            boolean f = collectionManager.updateById(id, newMovie, request.getLogin());
            if (f) {
                ru.append(Languages.get("elementUpdated", "Russian"));
                sl.append(Languages.get("elementUpdated", "Slovenian"));
                fr.append(Languages.get("elementUpdated", "French"));
                es.append(Languages.get("elementUpdated", "Spanish"));

                Response response = new Response(ru.toString(), ResponseType.PRINT_MESSAGE, request.getUID());
                response.setTranslate(new String[]{ru.toString(), sl.toString(), fr.toString(), es.toString()});
                logger.debug("Команда выполнена");
                return response;
            }
        }

        ru.append(Languages.get("elementDoesNotAppear", "Russian"));
        sl.append(Languages.get("elementDoesNotAppear", "Slovenian"));
        fr.append(Languages.get("elementDoesNotAppear", "French"));
        es.append(Languages.get("elementDoesNotAppear", "Spanish"));

        Response response = new Response(ru.toString(), ResponseType.ERROR, request.getUID());
        response.setTranslate(new String[]{ru.toString(), sl.toString(), fr.toString(), es.toString()});
        logger.debug("Команда выполнена");
        return response;
    }
}
