package server.managers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.utils.TextColors;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {
    private final static Logger logger = LogManager.getLogger(ConfigManager.class);

    private static final Properties properties = new Properties();

    static {
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            properties.load(fis);
        } catch (IOException e) {
            logger.error(e);
        }
    }

    /**
     * Возвращает ip адрес сервера
     *
     * @return адрес
     */
    public static String getAddress() {

        String address = properties.getProperty("server.ip");

        if (address == null || address.isBlank()) {
            logger.warn("Не удалось получить IP-адрес сервера. Значение пустое или отсутствует.");
        }

        return address;
    }

    /**
     * Возвращает порт
     *
     * @return порт
     */
    public static Integer getPort() {
        Integer port = null;
        try {
            port = Integer.parseInt(properties.getProperty("server.port"));
        } catch (NumberFormatException e) {
            logger.warn("Не удалось получить порт сервера. Поданное значение не является числом.");
        }
        if (port == null) {
            logger.warn("Не удалось получить порт сервера. Значение пустое или отсутствует.");
        }
        return port;
    }

    /**
     * Возвращает размер пакета
     *
     * @return размер пакета
     */
    public static Integer getPacketSize() {
        Integer size = null;
        try {
            size = Integer.parseInt(properties.getProperty("packet.size"));
        } catch (NumberFormatException e) {
            logger.warn("Не удалось получить размер пакета. Поданное значение не является числом.");
        }
        if (size == null) {
            logger.warn("Не удалось получить размер пакета. Значение пустое или отсутствует.");
        }
        return size;
    }

    /**
     * Возвращает максимум повторных попыток подключения
     *
     * @return максимум попыток
     */
    public static Integer getAttemptMax() {
        Integer attemptMax = null;
        try {
            attemptMax = Integer.parseInt(properties.getProperty("attempt.max"));
        } catch (NumberFormatException e) {
            logger.warn("Не удалось получить максимум попыток. Поданное значение не является числом.");
        }
        if (attemptMax == null) {
            logger.warn("Не удалось получить максимум попыток. Значение пустое или отсутствует.");
        }
        return attemptMax;
    }

    /**
     * Возвращает имя переменной окружения
     *
     * @return имя переменной
     */
    public static String getEnvName() {
        String envName = properties.getProperty("env.name");
        if (envName == null || envName.isBlank()) {
            logger.warn("Не удалось получить имя переменной окружения. Значение пустое или отсутствует.");
        }
        return envName;
    }

    public static String getColor(TextColors textColor) {
        String colorName = textColor.getColor();
        String color = properties.getProperty("color." + colorName);
        if (color == null || color.isBlank()) {
            logger.warn("Не удалось получить цвет {}. Значение пустое или отсутствует.", colorName);
        }
        return color;
    }

    public static String getPgpassPath() {
        String pgpassPath = properties.getProperty("pgpass.path");
        if (pgpassPath == null || pgpassPath.isBlank()) {
            logger.warn("Не удалось получить путь до pgpass. Значение пустое или отсутствует.");
        }
        return pgpassPath;
    }

    public static String getHeliospassPath() {
        String heliospassPath = properties.getProperty("heliospass.path");
        if (heliospassPath == null || heliospassPath.isBlank()) {
            logger.warn("Не удалось получить путь до heliospass. Значение пустое или отсутствует.");
        }
        return heliospassPath;
    }
}
