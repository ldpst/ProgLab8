package server.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.managers.PSQLManager;
import server.requests.Request;
import server.response.Response;
import server.response.ResponseType;

import java.io.IOException;

public class SignIn extends Command {
    private final Logger logger = LogManager.getLogger(SignIn.class);


    public SignIn() {
        super("sign_in", "sign in command");
    }

    @Override
    public Response execute(Request request) {
        String login = request.getLogin();
        String password = request.getPassword();

        boolean res = PSQLManager.signIn(login, password);
        if (res) {
            return new Response(GREEN + "Регистрация прошла успешно\n" + RESET, ResponseType.PRINT_MESSAGE);
        } else {
            return new Response(RED + "Пользователь с таким логином уже существует\n" + RESET, ResponseType.ERROR);
        }
    }
}