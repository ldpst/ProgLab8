package server.commands.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.managers.RunManager;
import server.utils.RunMode;

import java.io.PrintStream;

public class Exit extends Command {
    private final RunManager runManager;
    private final PrintStream stream;

    private final Logger logger = LogManager.getLogger(Exit.class);

    public Exit(RunManager runManager, PrintStream stream) {
        super("exit", "завершить программу (без сохранения в файл)");
        this.runManager = runManager;
        this.stream = stream;
    }

    @Override
    public void run(String[] args) {
        logger.info("Команда выполняется");
        runManager.setRunMode(RunMode.EXIT);
        stream.print("Программа завершена\n");
        logger.info("Команда выполнена");
    }
}
