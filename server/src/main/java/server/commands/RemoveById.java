package server.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.managers.CollectionManager;
import server.requests.Request;
import server.response.Response;
import server.response.ResponseType;
import server.server.UDPDatagramChannel;
import server.utils.Languages;

public class RemoveById extends Command {
    private final CollectionManager collectionManager;
    private final UDPDatagramChannel channel;

    private final Logger logger = LogManager.getLogger(RemoveById.class);

    public RemoveById(CollectionManager collectionManager, UDPDatagramChannel channel) {
        super("remove_by_id id", "удалить элемент из коллекции по его id", "remove_by_id");
        this.collectionManager = collectionManager;
        this.channel = channel;
    }

    @Override
    public Response execute(Request request) {
        logger.debug("Команда выполняется...");
        String[] args = request.getMessage().split(" ");
        int id = Integer.parseInt(args[1]);

        StringBuilder ru = new StringBuilder();
        StringBuilder sl = new StringBuilder();
        StringBuilder fr = new StringBuilder();
        StringBuilder es = new StringBuilder();

        Response response = new Response(Languages.get("elementRemoved", "Russian"), ResponseType.PRINT_MESSAGE, request.getUID());

        if (collectionManager.removeById(id, request.getLogin())) {
            ru.append(Languages.get("elementRemoved", "Russian"));
            sl.append(Languages.get("elementRemoved", "Slovenian"));
            fr.append(Languages.get("elementRemoved", "French"));
            es.append(Languages.get("elementRemoved", "Spanish"));
        } else {
            ru.append(Languages.get("elementDoesNotAppear", "Russian"));
            sl.append(Languages.get("elementDoesNotAppear", "Slovenian"));
            fr.append(Languages.get("elementDoesNotAppear", "French"));
            es.append(Languages.get("elementDoesNotAppear", "Spanish"));
            response.setType(ResponseType.ERROR);
        }

        response.setTranslate(new String[]{ru.toString(), sl.toString(), fr.toString(), es.toString()});
        logger.debug("Команда выполнена");
        return response;
    }
}