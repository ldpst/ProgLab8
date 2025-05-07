package server.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.managers.PSQLManager;
import server.requests.Request;
import server.response.Response;
import server.response.ResponseType;

public class SignUp extends Command {
    private final Logger logger = LogManager.getLogger(SignUp.class);


    public SignUp() {
        super("sign_up", "sign up command");
    }

    @Override
    public Response execute(Request request) {
        String login = request.getLogin();
        String password = request.getPassword();

        boolean res = PSQLManager.signIn(login, password);
        if (res) {
            return new Response("registrationWasSuccessful", ResponseType.PRINT_MESSAGE);
        } else {
            return new Response("userWithSuchALoginAlreadyExists", ResponseType.ERROR);
        }
    }
}