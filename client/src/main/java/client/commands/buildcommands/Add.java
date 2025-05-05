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
import server.requests.Request;
import server.response.Response;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class Add extends Command {
    private final UDPClient client;
    private static final Logger logger = LogManager.getLogger(Add.class);
    private final ScannerManager scanner;

    public Add(UDPClient client, StreamManager stream, ScannerManager scanner) {
        super("add", "добавить новый элемент в коллекцию", stream);
        this.client = client;
        this.scanner = scanner;
    }

    @Override
    public void execute(String[] args) throws IOException {
        logger.info("Команда выполняется...");
        Movie movie = new MovieBuilder(logger, stream, scanner, client).build();
        Response response = client.makeRequest("add", movie);
        stream.printSuccess("Элемент успешно добавлен\n");
        logger.info("Команда выполнена");
    }
}