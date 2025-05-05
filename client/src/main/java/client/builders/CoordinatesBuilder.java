package client.builders;

import client.managers.ScannerManager;
import client.managers.StreamManager;
import org.apache.logging.log4j.Logger;
import server.object.Coordinates;

public class CoordinatesBuilder extends Builder {
    public CoordinatesBuilder(StreamManager stream, ScannerManager scanner) {
        super(stream, scanner);
    }
    public CoordinatesBuilder(Logger logger, StreamManager stream, ScannerManager scanner) {
        super(logger, stream, scanner);
    }

    @Override
    public Coordinates build() {
        logger.debug("Заполнение координат...");
        Coordinates newCoordinates = new Coordinates(
                readX(),
                readY());
        logger.debug("Координаты заполнены");
        return newCoordinates;
    }

    /**
     * Метод для чтения координаты X
     *
     * @return Найденное число
     */
    private Float readX() {
        logger.debug("Заполнение х...");
        while (true) {
            stream.print("> Введите координату x:\n$ ");
            String res = scanner.nextLine().trim();
            try {
                Float x = Float.parseFloat(res);
                logger.debug("х заполнен");
                return x;
            } catch (NumberFormatException e) {
                stream.printErr("Координата x должна быть целым или вещественным числом\n");
                stream.print("* Повторная попытка ввода\n");
            }
        }

    }

    /**
     * Метод для чтения координаты Y
     *
     * @return Найденное число
     */
    private int readY() {
        logger.debug("Заполнение y");
        while (true) {
            stream.print("> Введите координату y:\n$ ");
            String res = scanner.nextLine().trim();
            try {
                int y = Integer.parseInt(res);
                logger.debug("y заполнен");
                return y;
            } catch (NumberFormatException e) {
                stream.printErr("Координата y должна быть целым числом\n");
                stream.print("* Повторная попытка ввода\n");
            }
        }
    }
}
