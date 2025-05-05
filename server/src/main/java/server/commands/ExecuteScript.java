package server.commands;

import client.managers.RunManager;
import client.managers.ScannerManager;
import client.utils.InputFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.managers.CollectionManager;
import server.requests.Request;
import server.response.Response;
import server.response.ResponseType;
import server.server.UDPDatagramChannel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Класс команды execute_script
 *
 * @author ldpst
 */
public class ExecuteScript extends Command {
    private final Logger logger = LogManager.getLogger(this.getClass());

    public ExecuteScript() {
        super("execute_script file_name", "считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме");
    }

    @Override
    public Response execute(Request request) throws IOException {
        return null;
    }
}