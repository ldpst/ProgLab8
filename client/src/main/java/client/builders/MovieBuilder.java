package client.builders;

import client.client.UDPClient;
import client.managers.ScannerManager;
import client.managers.StreamManager;
import org.apache.logging.log4j.Logger;
import server.object.*;

/**
 * Класс для создания объекта Movie
 *
 * @author ldpst
 */
public class MovieBuilder extends Builder {
    private final UDPClient client;

    public MovieBuilder(StreamManager stream, ScannerManager scanner, UDPClient client) {
        super(stream, scanner);
        this.client = client;
    }

    public MovieBuilder(Logger logger, StreamManager stream, ScannerManager scanner, UDPClient client) {
        super(logger, stream, scanner);
        this.client = client;
    }

    @Override
    public Movie build() {
        logger.debug("Заполнение Movie...");
        Movie newMovie = new Movie(
                readName(),
                readCoordinates(),
                readOscarCount(),
                readGenre(),
                readMpaaRating(),
                readPerson(),
                client.getLogin());
        logger.debug("Movie заполнен");
        return newMovie;
    }

    /**
     * Метод для чтения имени
     *
     * @return Найденная строка
     */
    private String readName() {
        logger.debug("Заполнение имени...");
        while (true) {
            stream.print("> Введите название фильма:\n$ ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                stream.printErr("Название не должно быть пустым\n");
                stream.print("* Повторная попытка ввода\n");
            } else {
                logger.debug("Имя заполнено");
                return name;
            }
        }
    }

    /**
     * Метод для чтения координат
     *
     * @return Найденные координаты
     */
    private Coordinates readCoordinates() {
        stream.print("* Ввод координат\n");
        return new CoordinatesBuilder(logger, stream, scanner).build();
    }

    /**
     * Метод для чтения количества оскаров
     *
     * @return Найденное количество
     */
    private Long readOscarCount() {
        logger.debug("Заполнение кол-ва оскаров...");
        while (true) {
            stream.print("> Введите количество оскаров:\n$ ");
            String res = scanner.nextLine().trim();
            long count;
            try {
                count = Long.parseLong(res);
                if (count <= 0) {
                    stream.printErr("Количество оскаров должно быть больше нуля\n");
                    stream.print("* Повторная попытка ввода\n");
                } else {
                    logger.debug("Кол-во оскаров заполнено");
                    return count;
                }
            } catch (NumberFormatException e) {
                stream.printErr("Количество оскаров должно быть целым числом\n");
                stream.print("* Повторная попытка ввода\n");
            }
        }
    }

    /**
     * Метод для чтения жанра
     *
     * @return Найденный жанр
     */
    private MovieGenre readGenre() {
        return new GenreBuilder(logger, stream, scanner).build();
    }

    /**
     * Метод для чтения Мпаа Рейтинга
     *
     * @return Найденный MpaaRating
     */
    private MpaaRating readMpaaRating() {
        return new MpaaRatingBuilder(logger, stream, scanner).build();
    }

    /**
     * Метод для чтения человека
     *
     * @return Найденный Person
     */
    private Person readPerson() {
        while (true) {
            stream.print("> Оператор == null? y/n ");
            String res = scanner.nextLine().trim().toLowerCase();
            if (res.equals("n") || res.isEmpty() || res.equals("no")) {
                stream.print("* Ввод оператора\n");
                return new PersonBuilder(logger, stream, scanner).build();
            } else if (res.equals("y") || res.equals("yes")) {
                return null;
            } else {
                stream.printErr("Введённая строка не соответствует y или n\n");
                stream.print("* Повторная попытка ввода\n");
            }
        }
    }
}
