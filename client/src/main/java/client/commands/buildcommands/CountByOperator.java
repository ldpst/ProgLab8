package client.commands.buildcommands;

import client.builders.PersonBuilder;
import client.client.UDPClient;
import client.commands.Command;
import client.managers.ScannerManager;
import client.managers.StreamManager;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.object.Person;

import java.io.IOException;

public class CountByOperator extends Command {
    private final UDPClient client;
    private final ScannerManager scanner;
    private final Logger logger = LogManager.getLogger(CountByOperator.class);

    public CountByOperator(UDPClient client, StreamManager stream, ScannerManager scanner) {
        super("count_by_operator operator/null", "вывести количество элементов, значение поля operator которых равно заданному", stream);
        this.client = client;
        this.scanner = scanner;
    }

    @Override
    public void execute(String[] args) throws IOException {
        logger.info("Команда выполняется...");
        Person person;
        if (args.length == 2 && args[1].equals("null")) {
            person = null;
        } else {
            person = new PersonBuilder(logger, stream, scanner).build();
        }
        var response = client.makeRequest("count_by_operator", person);
        stream.print(response.getMessage());
        logger.info("Команда выполнена");
    }
}