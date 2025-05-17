package server.commands;

import server.managers.CollectionManager;
import server.requests.Request;
import server.response.Response;
import server.response.ResponseType;

public class Show extends Command {
    private final CollectionManager collectionManager;

    public Show(CollectionManager collectionManager) {
        super("show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении", null);
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        return new Response("", ResponseType.COLLECTION, collectionManager.getMovies(), request.getUID());
    }
}
