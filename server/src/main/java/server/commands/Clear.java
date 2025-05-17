package server.commands;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.managers.CollectionManager;
import server.requests.Request;
import server.response.Response;
import server.response.ResponseType;
import server.server.UDPDatagramChannel;

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
        logger.info("Команда выполняется...");
        collectionManager.clear(request.getLogin());
        logger.info("Команда выполнена");
        return new Response(GREEN + "Коллекция очищена\n" + RESET, ResponseType.PRINT_MESSAGE, request.getUID());
    }
}