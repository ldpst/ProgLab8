package client.commands.buildcommands;

import client.builders.GenreBuilder;
import client.client.UDPClient;
import client.commands.Command;
import client.managers.ScannerManager;
import client.managers.StreamManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.object.MovieGenre;
import server.response.Response;

import java.io.IOException;

public class CountLessThanGenre extends Command {
    private final UDPClient client;
    private final ScannerManager scanner;
    private final Logger logger = LogManager.getLogger(CountLessThanGenre.class);

    public CountLessThanGenre(UDPClient client, StreamManager stream, ScannerManager scanner) {
        super("count_less_than_genre genre", "вывести количество элементов, значение поля genre которых меньше заданного", stream);
        this.client = client;
        this.scanner = scanner;
    }

    @Override
    public void execute(String[] args) throws IOException {
        logger.info("Команда выполняется...");
        MovieGenre genre;
        if (args.length == 2 && args[1].equals("null")) {
            genre = null;
        } else {
            genre = new GenreBuilder(logger, stream, scanner).build();
        }
        Response response = client.makeRequest("count_less_than_genre", genre);
        stream.print(response.getMessage());
        logger.info("Команда выполнена");
    }
}