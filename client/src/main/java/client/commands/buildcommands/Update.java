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

public class Update extends Command {
    private final ScannerManager scanner;
    private final Logger logger = LogManager.getLogger(Update.class);
    private final UDPClient client;

    public Update(UDPClient client, StreamManager stream, ScannerManager scanner) {
        super("update id {element}", "обновить значение элемента коллекции, id которого равен заданному", stream);
        this.scanner = scanner;
        this.client = client;
    }

    @Override
    public void execute(String[] args) throws IOException {
        logger.info("Команда выполняется...");
        if (args.length != 2) {
            stream.printErr("Неверный формат команды\n");
            return;
        }
        int id;
        try {
            id = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            stream.printErr("Введённый id должен быть целым числом\n");
            return;
        }
        Movie newMovie = new MovieBuilder(logger, stream, scanner, client).build();
        newMovie.setId(id);
        Response response = client.makeRequest("update " + id, newMovie, client.getLogin(), client.getPassword());
        stream.print(response.getMessage());
        logger.info("Команда выполнена");
    }
}