package server.commands.server;

import server.managers.CollectionManager;
import server.object.Movie;

import java.io.PrintStream;

public class Show extends Command {
    private final CollectionManager collectionManager;
    private final PrintStream stream;

    public Show(CollectionManager collectionManager, PrintStream stream) {
        super("show", "показать содержимое коллекции");
        this.collectionManager = collectionManager;
        this.stream = stream;
    }

    @Override
    public void run(String[] args) {
        stream.print("Содержимое коллекции:\n");
        for (Movie movie : collectionManager.getMovies()) {
            stream.print(movie + "\n");
        }
    }
}
