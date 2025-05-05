package client.managers;

import client.client.UDPClient;
import client.commands.Command;
import client.commands.ExecuteScript;
import client.commands.buildcommands.*;
import client.exceptions.ServerIsUnavailableException;
import client.utils.InputFormat;
import client.utils.RunMode;
import server.response.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RunManager {
    private RunMode runMode = RunMode.RUN;
    private final StreamManager stream;
    private final ScannerManager scanner;

    private final UDPClient client;

    public static final List<String> usedScripts = new ArrayList<>();

    Map<String, Command> buildCommands = new HashMap<>();

    public RunManager(UDPClient client, ScannerManager scanner, InputFormat inputFormat) throws IOException {
        this.client = client;
        this.scanner = scanner;
        this.stream = new StreamManager(System.out, inputFormat);
        buildCommands.put("add", new Add(client, stream, scanner));
        buildCommands.put("add_if_max", new AddIfMax(client, stream, scanner));
        buildCommands.put("update", new Update(client, stream, scanner));
        buildCommands.put("count_by_operator", new CountByOperator(client, stream, scanner));
        buildCommands.put("count_less_than_genre", new CountLessThanGenre(client, stream, scanner));
        buildCommands.put("remove_greater", new RemoveGreater(client, stream, scanner));
    }

    public void run() {
        authorize();

        while (runMode == RunMode.RUN) {
            stream.print("$ ");
            String nextCommand = scanner.nextLine().trim();
            if (nextCommand.startsWith("execute_script")) {
                new ExecuteScript(client, stream).execute(nextCommand.split(" "));
                continue;
            }
            try {
                String[] split = nextCommand.split(" ");
                buildCommands.get(split[0]).execute(split);
                continue;
            } catch (Exception e) {
                //
            }
            try {
                Response response = client.makeRequest(nextCommand, client.getLogin(), client.getPassword());
                switch (response.getType()) {
                    case PRINT_MESSAGE, ERROR -> stream.print(response.getMessage());
                    case COLLECTION -> stream.print(ResponseManager.collectionToString(response.getCollection()));
                    case NEXT_STEP -> {
                        stream.print(response.getMessage());
                    }
                    case EXIT -> {
                        stream.print(response.getMessage());
                        runMode = RunMode.EXIT;
                    }
                }
            } catch (ServerIsUnavailableException e) {
                stream.printErr("Сервер в данный момент недоступен. Программа завершена\n");
                System.exit(1);
            } catch (IOException e) {
                stream.printErr("Вызвана ошибка ввода/вывода");
                throw new RuntimeException(e);
            }
        }
        UDPClient.logger.info("Клиент завершил работу");
    }

    public void setRunMode(RunMode runMode) {
        this.runMode = runMode;
    }

    private void authorize() {
        while (true) {
            stream.print("> Выберите [log in/sign in]\n");
            String res = scanner.nextLine().trim();
            if (res.equals("log in") || res.equals("1")) {
                logIn();
                break;
            } else if (res.equals("sign in") || res.equals("2")) {
                signIn();
                break;
            }
        }
    }

    private void logIn() {
        String login, password;
        stream.print("> Введите логин\n");
        login = scanner.nextLine().trim();
        stream.print("> Введите пароль\n");
        password = scanner.nextLine().trim();
        String hashPassword = MD2Manager.getHash(password);
        try {
            Response response = client.makeRequest("log_in", login, hashPassword);
            parseLogInSignInResponse(response, login, hashPassword);
        } catch (ServerIsUnavailableException e) {
            stream.printErr("Сервер в данный момент недоступен. Программа завершена\n");
            System.exit(1);
        } catch (IOException e) {
            stream.printErr("Вызвана ошибка ввода/вывода");
            throw new RuntimeException(e);
        }
    }

    private void signIn() {
        String login, password;
        while (true) {
            stream.print("> Введите логин\n");
            login = scanner.nextLine().trim();
            if (login.length() > 16) {
                stream.print("> Логин не может быть длиннее 16ти символов\n* Повторная попытка ввода");
            } else break;
        }
        stream.print("> Введите пароль\n");
        password = scanner.nextLine().trim();
        String hashPassword = MD2Manager.getHash(password);
        try {
            Response response = client.makeRequest("sign_in", login, hashPassword);
            parseLogInSignInResponse(response, login, hashPassword);
        } catch (ServerIsUnavailableException e) {
            stream.printErr("Сервер в данный момент недоступен. Программа завершена\n");
            System.exit(1);
        } catch (IOException e) {
            stream.printErr("Вызвана ошибка ввода/вывода");
            throw new RuntimeException(e);
        }
    }

    private void parseLogInSignInResponse(Response response, String login, String hashPassword) {
        switch (response.getType()) {
            case PRINT_MESSAGE -> {
                stream.print(response.getMessage());
                client.setLoginAndPassword(login, hashPassword);
            }
            case ERROR -> {
                stream.print(response.getMessage());
                stream.print("* Повторная попытка авторизации\n");
                authorize();
            }
        }
    }
}
