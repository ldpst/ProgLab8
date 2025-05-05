package client.commands.buildcommands;

import client.builders.MovieBuilder;
import client.client.UDPClient;
import client.commands.Command;
import client.managers.ScannerManager;
import client.managers.StreamManager;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.object.Movie;
import server.response.Response;

import java.io.IOException;

public class RemoveGreater extends Command {
    private final ScannerManager scanner;
    private final Logger logger = LogManager.getLogger(RemoveGreater.class);
    private final UDPClient client;

    public RemoveGreater(UDPClient client, StreamManager stream, ScannerManager scanner) {
        super("remove_greater {element}", "удалить из коллекции все элементы, превышающие заданный", stream);
        this.scanner = scanner;
        this.client = client;
    }

    @Override
    public void execute(String[] args) throws IOException {
        logger.info("Команда выполняется...");
        Movie movie = new MovieBuilder(logger, stream, scanner, client).build();
        Response response = client.makeRequest("remove_greater", movie, client.getLogin(), client.getPassword());
        stream.printSuccessf(response.getMessage());
        logger.info("Команда выполнена");
    }
}