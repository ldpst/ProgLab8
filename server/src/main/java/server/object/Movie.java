package server.object;

import server.utils.Validatable;
import server.utils.ValidationError;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Objects;

/**
 * Класс фильма
 *
 * @author ldpst
 */
public class Movie
        implements Comparable<Movie>, Validatable, Serializable {

    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private final Coordinates coordinates; //Поле не может быть null
    private ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Long oscarsCount; //Значение поля должно быть больше 0, Поле не может быть null
    private MovieGenre genre; //Поле может быть null
    private MpaaRating mpaaRating; //Поле может быть null
    private Person operator; //Поле может быть null
    private String owner;

    public Movie(String name, Coordinates coordinates, Long oscarsCount, MovieGenre genre, MpaaRating mpaaRating, Person operator, String owner) {
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = ZonedDateTime.now();
        this.oscarsCount = oscarsCount;
        this.genre = genre;
        this.mpaaRating = mpaaRating;
        this.operator = operator;
        this.owner = owner;
        if (isValid()) {
            throw new ValidationError("Movie");
        }
    }

    public Movie(Integer id, String name, Coordinates coordinates, Timestamp creationDate, Long oscarsCount, MovieGenre genre, MpaaRating mpaaRating, Person operator, String owner) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate.toInstant().atZone(ZoneId.of("Europe/Moscow"));
        this.oscarsCount = oscarsCount;
        this.genre = genre;
        this.mpaaRating = mpaaRating;
        this.operator = operator;
        this.owner = owner;
        if (isValid()) {
            throw new ValidationError("Movie");
        }
    }

    /**
     * Метод для сравнения с другим объектом Movie
     *
     * @param other объект типа Movie для сравнения
     * @return Результат меньше нуля, если other больше данного объекта. Результат равен нулю, если элементы равны. Результат больше нуля, если данный объект больше other
     */
    @Override
    public int compareTo(Movie other) {
        return Coordinates.compare(getCoordinates(), other.getCoordinates());
    }

    /**
     * Метод, возвращающий поле id
     *
     * @return id
     */
    public long getId() {
        return this.id;
    }

    public void setId(int newId) {
        id = newId;
    }

    /**
     * Метод проверки валидности полей класса
     *
     * @return результат проверки
     */
    @Override
    public boolean isValid() {
        if (this.name == null || this.name.isEmpty()) return true;
        if (this.coordinates == null) return true;
        if (this.creationDate == null) return true;
        return this.oscarsCount <= 0;
    }

    /**
     * Метод, возвращающий поле name
     *
     * @return name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Метод, возвращающий поле coordinates
     *
     * @return coordinates
     */
    public Coordinates getCoordinates() {
        return this.coordinates;
    }

    /**
     * Метод, возвращающий поле creationDate
     *
     * @return creationDate
     */
    public ZonedDateTime getCreationDate() {
        return this.creationDate;
    }

    /**
     * Метод, возвращающий поле oscarsCount
     *
     * @return oscarsCount
     */
    public Long getOscarsCount() {
        return this.oscarsCount;
    }

    /**
     * Метод, возвращающий поле genre
     *
     * @return genre
     */
    public MovieGenre getGenre() {
        return this.genre;
    }

    /**
     * Метод, возвращающий поле mpaaRating
     *
     * @return mpaaRating
     */
    public MpaaRating getMpaaRating() {
        return this.mpaaRating;
    }

    /**
     * Метод, возвращающий поле operator
     *
     * @return operator
     */
    public Person getOperator() {
        return this.operator;
    }

    public String getOwner() {
        return owner;
    }

    public void setName(String name) {
        if (name == null || name.isEmpty()) throw new ValidationError("Movie");
        this.name = name;
    }

    public void setCoordinateX(float x) {
        coordinates.setX(x);
    }

    public void setCoordinateY(int y) {
        coordinates.setY(y);
    }

    public void setCreationDate(ZonedDateTime date) {
        creationDate = date;
    }

    public void setOscarsCount(long count) {
        oscarsCount = count;
    }

    public void setGenre(MovieGenre genre) {
        this.genre = genre;
    }

    public void setMpaaRating(MpaaRating mpaaRating) {
        this.mpaaRating = mpaaRating;
    }

    public void setOperator(Person operator) {
        this.operator = operator;
    }

    public void setOperatorsName(String name) {
        operator.setName(name);
    }

    public void setOperatorsBirthday(Date date) {
        operator.setBirthday(date);
    }

    public void setOperatorsWeight(long weight) {
        operator.setWeight(weight);
    }

    public void setOperatorsPassportID(String passportID) {
        operator.setPassportID(passportID);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return (this.id == movie.getId() &&
                Objects.equals(this.name, movie.getName()) &&
                Objects.equals(this.coordinates, movie.getCoordinates()) &&
                Objects.equals(this.creationDate, movie.getCreationDate()) &&
                Objects.equals(this.oscarsCount, getOscarsCount()) &&
                Objects.equals(this.genre, getGenre()) &&
                Objects.equals(this.mpaaRating, getMpaaRating()) &&
                Objects.equals(this.operator, movie.getOperator()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.coordinates, this.creationDate, this.oscarsCount, this.genre, this.mpaaRating, this.operator);
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id + ", " +
                "name='" + name + "', " +
                "Coordinates=" + coordinates + ", " +
                "creationDate='" + creationDate.toString() + "', " +
                "oscarsCount=" + oscarsCount + ", " +
                "genre=" + ((genre != null) ? ("'" + this.genre + "'") : "null") + ", " +
                "mpaaRating=" + ((mpaaRating != null) ? ("'" + this.mpaaRating + "'") : "null") + ", " +
                "operator=" + ((operator != null) ? (operator) : "null") +
                "}";
    }
}