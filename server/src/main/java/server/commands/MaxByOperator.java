package server.commands;

import server.object.Movie;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.managers.CollectionManager;
import server.requests.Request;
import server.response.Response;
import server.response.ResponseType;

import java.io.IOException;

public class MaxByOperator extends Command {
    private final Logger logger = LogManager.getLogger(MaxByOperator.class);

    private final CollectionManager collectionManager;

    public MaxByOperator(CollectionManager collectionManager) {
        super("max_by_operator", "вывести любой объект из коллекции, значение поля operator которого является максимальным", "max_by_operator");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) throws IOException {
        logger.info("Команда выполняется...");
        Movie max = collectionManager.getMaxByOperator();
        logger.info("Команда выполнена");
        if (max == null) {
            return new Response(GREEN + "Коллекция пуста\n" + RESET, ResponseType.PRINT_MESSAGE);
        }
        return new Response(GREEN + "Максимальный элемент по оператору:\n" + RESET + max + "\n", ResponseType.PRINT_MESSAGE);
    }
}