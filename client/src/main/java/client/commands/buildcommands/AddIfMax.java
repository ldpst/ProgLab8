package client.commands.buildcommands;

import client.builders.MovieBuilder;
import client.client.UDPClient;
import client.commands.Command;
import client.managers.ScannerManager;
import client.managers.StreamManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.object.Movie;
import server.response.Response;

import java.io.IOException;

public class AddIfMax extends Command {
    private final UDPClient client;
    private final Logger logger = LogManager.getLogger(AddIfMax.class);
    private final ScannerManager scanner;

    public AddIfMax(UDPClient client, StreamManager stream, ScannerManager scanner) {
        super("add_if_max {element}", "добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции", stream);
        this.client = client;
        this.scanner = scanner;
    }

    @Override
    public void execute(String[] args) throws IOException {
        logger.info("Команда выполняется...");
        Movie movie = new MovieBuilder(logger, stream, scanner, client).build();
        Response response = client.makeRequest("add_if_max", movie);
        stream.print(response.getMessage());
        logger.info("Команда выполнена");
    }
}
