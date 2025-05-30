package server.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.managers.PSQLManager;
import server.requests.Request;
import server.response.Response;
import server.response.ResponseType;

public class LogIn extends Command {
    private final Logger logger = LogManager.getLogger(LogIn.class);


    public LogIn() {
        super("log_in", "log in command", "log_in");
    }

    @Override
    public Response execute(Request request) {
        String login = request.getLogin();
        String password = request.getPassword();

        boolean res = PSQLManager.logIn(login, password);
        if (res) {
            return new Response("authorizationIsSuccessful", ResponseType.PRINT_MESSAGE, request.getUID());
        } else {
            return new Response("loginOrPasswordIsWrong", ResponseType.ERROR, request.getUID());
        }
    }
}