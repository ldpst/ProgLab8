package server.commands;

import server.object.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.managers.CollectionManager;
import server.requests.Request;
import server.response.Response;
import server.response.ResponseType;
import server.server.UDPDatagramChannel;

import java.io.IOException;

public class CountByOperator extends Command {
    private final Logger logger = LogManager.getLogger(CountByOperator.class);
    private final CollectionManager collectionManager;
    private final UDPDatagramChannel channel;

    public CountByOperator(CollectionManager collectionManager, UDPDatagramChannel channel) {
        super("count_by_operator operator", "вывести количество элементов, значение поля operator которых равно заданному", "count_by_operator");
        this.collectionManager = collectionManager;
        this.channel = channel;
    }

    @Override
    public Response execute(Request request) throws IOException {
        logger.info("Команда выполняется...");
        Person operator = (Person) request.getData();
        int count = collectionManager.countByOperator(operator);
        logger.info("Команда выполнена");
        return new Response(GREEN + "Элементов с данным operator: " + count + "\n" + RESET, ResponseType.PRINT_MESSAGE, request.getUID());
    }
}
