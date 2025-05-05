package server.commands;

import client.managers.RunManager;
import client.managers.StreamManager;
import client.utils.RunMode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.requests.Request;
import server.response.Response;
import server.response.ResponseType;

import java.io.IOException;

public class Exit extends Command {
    private final Logger logger = LogManager.getLogger(Exit.class);

    public Exit() {
        super("exit", "завершить программу (без сохранения в файл)");
    }

    @Override
    public Response execute(Request request) throws IOException {
        logger.info("Команда выполнена");
        return new Response(GREEN + "Программа завершена\n" + RESET, ResponseType.EXIT);
    }
}