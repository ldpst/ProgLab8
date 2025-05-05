package client.builders;

import client.managers.ScannerManager;
import client.managers.StreamManager;
import org.apache.logging.log4j.Logger;
import server.object.MovieGenre;

import java.util.Arrays;

public class GenreBuilder extends Builder {
    public GenreBuilder(StreamManager stream, ScannerManager scanner) {
        super(stream, scanner);
    }

    public GenreBuilder(Logger logger, StreamManager stream, ScannerManager scanner) {
        super(logger, stream, scanner);
    }

    @Override
    public MovieGenre build() {
        return readGenre();
    }

    /**
     * Метод для чтения жанра
     *
     * @return Найденный жанр
     */
    private MovieGenre readGenre() {
        logger.debug("Заполнение жанра...");
        while (true) {
            stream.print("> Введите жанр " + Arrays.toString(MovieGenre.values()) + ":\n$ ");
            String res = scanner.nextLine().trim();
            if (res.isEmpty() || res.equals("null")) {
                logger.debug("Жанр заполнен");
                return null;
            }
            try {
                MovieGenre newMovieGenre = MovieGenre.checkOf(res);
                logger.debug("Жанр заполнен");
                return newMovieGenre;
            } catch (IllegalArgumentException e) {
                stream.printErr("Введенный жанр не является одним из предложенных\n");
                stream.print("* Повторная попытка ввода\n");
            }
        }
    }
}
