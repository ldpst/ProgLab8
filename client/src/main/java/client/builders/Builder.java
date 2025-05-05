package client.builders;

import client.managers.ScannerManager;
import client.managers.StreamManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class Builder {
    protected StreamManager stream;
    protected ScannerManager scanner;

    protected Logger logger;

    public Builder(StreamManager stream, ScannerManager scanner) {
        this(LogManager.getLogger(Builder.class), stream, scanner);
    }

    public Builder(Logger logger, StreamManager stream, ScannerManager scanner) {
        this.stream = stream;
        this.scanner = scanner;
        this.logger = logger;
    }

    /**
     * Метод для заполнения объекта данными, используя стандартный ввод
     *
     * @return Заполненный объект
     */
    abstract public Object build();
}
