package client.exceptions;

/**
 * Исключение, вызываемое при необработанном нарушении условий полей.
 * Создано в целях тестирования и отладки
 *
 * @author ldpst
 */
public class ValidationError extends RuntimeException {
    private final String place;

    public ValidationError(String place) {
        super();
        this.place = place;
    }

    @Override
    public String getMessage() {
        return "Ошибка при загрузке данных из файла. Объект класса " + place + " введен неверно.";
    }
}
