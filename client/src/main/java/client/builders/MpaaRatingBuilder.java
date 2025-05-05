package client.builders;

import client.managers.ScannerManager;
import client.managers.StreamManager;
import org.apache.logging.log4j.Logger;
import server.object.MpaaRating;

import java.util.Arrays;

public class MpaaRatingBuilder extends Builder {
    public MpaaRatingBuilder(StreamManager stream, ScannerManager scanner) {
        super(stream, scanner);
    }

    public MpaaRatingBuilder(Logger logger, StreamManager stream, ScannerManager scanner) {
        super(logger, stream, scanner);
    }

    @Override
    public MpaaRating build() {
        return readMpaaRating();
    }

    /**
     * Метод для чтения Мпаа Рейтинга
     *
     * @return Найденный MpaaRating
     */
    private MpaaRating readMpaaRating() {
        logger.debug("Заполнение мпаа рейтинга...");
        while (true) {
            stream.print("> Введите Мпаа Рейтинг " + Arrays.toString(MpaaRating.values()) + ":\n$ ");
            String res = scanner.nextLine().trim();
            if (res.isEmpty()) {
                logger.debug("Мпаа рейтинг заполнен");
                return null;
            }
            try {
                MpaaRating newMpa = MpaaRating.checkOf(res);
                logger.debug("Мпаа рейтинг заполнен");
                return newMpa;
            } catch (IllegalArgumentException e) {
                stream.printErr("Введенный Мпаа рейтинг не является одним из предложенных\n");
                stream.print("* Повторная попытка ввода\n");
            }
        }
    }
}
