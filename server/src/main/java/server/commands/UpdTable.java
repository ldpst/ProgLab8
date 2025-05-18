package server.commands;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.managers.CommandManager;
import server.requests.Request;
import server.response.Response;

import java.io.IOException;


public class UpdTable extends Command {
    private final Logger logger = LogManager.getLogger(Help.class);

    public UpdTable() {
        super("upd_table", "обновить значения в таблице", "upd_table");
    }

    @Override
    public Response execute(Request request) throws IOException {
        return null;
    }
}
