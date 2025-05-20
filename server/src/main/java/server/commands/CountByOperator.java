package server.commands;

import server.object.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.managers.CollectionManager;
import server.requests.Request;
import server.response.Response;
import server.response.ResponseType;
import server.server.UDPDatagramChannel;
import server.utils.Languages;

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
        logger.debug("Команда выполняется...");
        Person operator = (Person) request.getData();
        int count = collectionManager.countByOperator(operator);

        StringBuilder ru = new StringBuilder();
        StringBuilder sl = new StringBuilder();
        StringBuilder fr = new StringBuilder();
        StringBuilder es = new StringBuilder();

        ru.append(Languages.get("elementWithThisOperator", "Russian")).append(": ").append(count);
        sl.append(Languages.get("elementWithThisOperator", "Slovenian")).append(": ").append(count);
        fr.append(Languages.get("elementWithThisOperator", "French")).append(": ").append(count);
        es.append(Languages.get("elementWithThisOperator", "Spanish")).append(": ").append(count);

        Response response = new Response(ru.toString(), ResponseType.PRINT_MESSAGE, request.getUID());
        response.setTranslate(new String[]{ru.toString(), sl.toString(), fr.toString(), es.toString()});
        logger.debug("Команда выполнена");
        return response;
    }
}
