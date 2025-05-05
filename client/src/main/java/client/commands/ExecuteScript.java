package client.commands;

import client.client.UDPClient;
import client.managers.RunManager;
import client.managers.ScannerManager;
import client.managers.StreamManager;
import client.utils.InputFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    private final UDPClient client;
    private final Logger logger = LogManager.getLogger(this.getClass());

    public ExecuteScript(UDPClient client, StreamManager stream) {
        super("execute_script file_name", "считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме", stream);
        this.client = client;
    }

    @Override
    public void execute(String[] args) {
        logger.info("Команда выполняется...");
        logger.debug("Поиск пути");
        if (args.length != 2) {
            stream.printErr("Неверный формат команды\n");
            logger.warn("Путь не указан");
            return;
        }
        logger.debug("Поиск файла по пути {}", args[1]);
        if (RunManager.usedScripts.contains(args[1])) {
            stream.printErr("Запуск скрипта " + args[1] + " вызывает рекурсию\n");
            logger.error("Запуск скрипта {} вызывает рекурсию\n", args[1]);
            System.exit(1);
        }
        RunManager.usedScripts.add(args[1]);
        try (FileInputStream fis = new FileInputStream(args[1])) {
            stream.printSuccess("Выполнение скрипта\n");
            logger.info("Выполнение скрипта {}...", args[1]);
            new RunManager(client, new ScannerManager(new InputStreamReader(fis)), InputFormat.SCRIPT).run();
            RunManager.usedScripts.remove(args[1]);
            stream.printSuccess("Скрипт успешно выполнен\n");
            logger.info("Выполнение скрипта {} завершено", args[1]);
        } catch (FileNotFoundException e) {
            stream.printErr("Файл со скриптом не найден\n");
        } catch (NullPointerException e) {
            stream.printErr("\nВ скрипте отсутствует команда exit. Программа завершена\n");
            System.exit(0);
        } catch (ClassCastException e) {
            logger.warn(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        logger.info("Команда выполнена");
    }
}