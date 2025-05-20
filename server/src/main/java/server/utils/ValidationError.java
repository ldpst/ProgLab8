package server.utils;

/**
 * Исключение, вызываемое при необработанном нарушении условий полей.
 * Создано в целях тестирования и отладки
 *
 * @author ldpst
 */
public class ValidationError extends RuntimeException {
    private final String condition;
    private String field;

    public ValidationError(String condition, String field) {
        super();
        this.condition = condition;
        this.field = field;
    }

    public ValidationError(String condition) {
        super();
        this.condition = condition;
    }

    @Override
    public String getMessage() {
        return condition;
    }

    public String getField() {
        return field;
    }
}
