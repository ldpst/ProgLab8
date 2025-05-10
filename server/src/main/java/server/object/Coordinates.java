package server.object;

import server.utils.Validatable;

import java.io.Serializable;
import java.util.Objects;

/**
 * Класс координат
 *
 * @author ldpst
 */
public class Coordinates implements Validatable, Comparable<Coordinates>, Serializable {
    private Float x;
    private int y;

    public Coordinates(Float x, int y) {
        this.x = x;
        this.y = y;
//        if (isValid()) {
//            throw new ValidationError("Coordinates");
//        }
    }

    /**
     * Метод, возвращающий значение поля x
     *
     * @return x
     */
    public Float getX() {
        return x;
    }

    /**
     * Метод, возвращающий значение поля y
     *
     * @return y
     */
    public int getY() {
        return y;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    private double countVectorLength() {
        return Math.sqrt(Math.pow((double) x, 2.0) + Math.pow(y, 2.0));
    }

    @Override
    public boolean isValid() {
        if (x == null) return true;
        return y <= -486;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || obj.getClass() != getClass()) return false;
        Coordinates coordinates = (Coordinates) obj;
        return (Objects.equals(x, coordinates.getX()) && Objects.equals(y, coordinates.getY()));
    }

    @Override
    public String toString() {
        return "{x=" + x + ", y=" + y + "}";
    }

    /**
     * Метод для сравнения с другим объектом Coordinates
     *
     * @param o объект типа Coordinates для сравнения
     * @return Результат меньше нуля, если other больше данного объекта. Результат равен нулю, если элементы равны. Результат больше нуля, если данный объект больше other
     */
    @Override
    public int compareTo(Coordinates o) {
        return Double.compare(countVectorLength(), o.countVectorLength());
    }

    /**
     * Метод для сравнения двух Coordinates
     *
     * @param x объект 1
     * @param y объект 2
     * @return Результат меньше нуля, если y больше данного x. Результат равен нулю, если элементы равны. Результат больше нуля, если данный x больше y
     */
    public static int compare(Coordinates x, Coordinates y) {
        return Double.compare(x.countVectorLength(), y.countVectorLength());
    }
}