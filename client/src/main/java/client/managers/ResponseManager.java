package client.managers;

import client.client.UDPClient;
import server.object.Movie;
import server.managers.ConfigManager;
import server.utils.TextColors;

import java.util.concurrent.LinkedBlockingDeque;


public class ResponseManager {
    protected static final String RED = ConfigManager.getColor(TextColors.RED);
    protected static final String RESET = ConfigManager.getColor(TextColors.RESET);
    protected static final String GREEN = ConfigManager.getColor(TextColors.GREEN);

    private final UDPClient client;

    public ResponseManager(UDPClient client) {
        this.client = client;
    }

    public static String collectionToString(LinkedBlockingDeque<Movie> movies) {
        StringBuilder message;
        if (movies.isEmpty()) {
            message = new StringBuilder(GREEN + "Коллекция пуста\n" + RESET);
        } else {
            message = new StringBuilder(GREEN + "Коллекция:\n" + RESET);
            for (Movie movie : movies) {
                message.append(movie).append("\n");
            }
        }
        return message.toString();
    }
}
