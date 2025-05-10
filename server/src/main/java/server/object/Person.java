package server.object;

import server.utils.Validatable;
import server.utils.ValidationError;

import java.io.Serializable;
import java.util.Objects;

/**
 * Класс человек
 *
 * @author ldpst
 */
public class Person implements Validatable, Comparable<Person>, Serializable {
    private String name; //Поле не может быть null, Строка не может быть пустой
    private java.util.Date birthday; //Поле может быть null
    private Long weight; //Значение поля должно быть больше 0
    private String passportID; //Длина строки не должна быть больше 25, Строка не может быть пустой, Поле не может быть null

    public Person(String name, java.util.Date birthday, Long weight, String passportID) {
        this.name = name;
        this.birthday = birthday;
        this.weight = weight;
        this.passportID = passportID;
        if (isValid()) {
            throw new ValidationError("Person");
        }
    }

    @Override
    public boolean isValid() {
        if (name == null) return true;
        if (weight <= 0) return true;
        return passportID == null || passportID.isEmpty() || passportID.length() > 25;
    }

    /**
     * Метод, возвращающий поле name
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Метод, возвращающий поле birthday
     *
     * @return birthday
     */
    public java.util.Date getBirthday() {
        return birthday;
    }

    /**
     * Метод, возвращающий поле weight
     *
     * @return weight
     */
    public long getWeight() {
        return weight;
    }

    /**
     * Метод, возвращающий поле passportID
     *
     * @return passportID
     */
    public String getPassportID() {
        return passportID;
    }

    public void setName(String newName) {
        if (newName == null || newName.isEmpty()) throw new ValidationError("Person");
        name = newName;
    }

    public void setBirthday(java.util.Date newBirthday) {
        if (newBirthday == null) throw new ValidationError("Person");
        birthday = newBirthday;
    }

    public void setWeight(long newWeight) {
        if (newWeight <= 0) throw new ValidationError("Person");
        weight = newWeight;
    }

    public void setPassportID(String newPassportID) {
        if (newPassportID == null || newPassportID.isEmpty() || newPassportID.length() > 25) throw new ValidationError("Person");
        passportID = newPassportID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return (Objects.equals(name, person.getName()) &&
                Objects.equals(birthday, person.getBirthday()) &&
                Objects.equals(weight, person.getWeight()) &&
                Objects.equals(passportID, person.getPassportID()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, birthday, weight, passportID);
    }

    @Override
    public String toString() {
        return "{" +
                "name=" + toNotNullString(name) + ", " +
                "birthday=" + toNotNullString(birthday) + ", " +
                "weight=" + toNotNullLong(weight) + ", " +
                "passportID=" + toNotNullString(passportID) +
                "}";

    }

    /**
     * Метод возвращающий поле в виде строки
     *
     * @param object конвертирующийся объект
     * @return строка
     */
    private String toNotNullString(Object object) {
        return ((object == null) ? "null" : "'" + object + "'");
    }

    private String toNotNullLong(Long object) {
        return ((object == null) ? "null" : object.toString());
    }

    /**
     * Метод для сравнения с другим объектом Person
     *
     * @param o объект типа Person для сравнения
     * @return Результат меньше нуля, если other больше данного объекта. Результат равен нулю, если элементы равны. Результат больше нуля, если данный объект больше other
     */
    @Override
    public int compareTo(Person o) {
        return birthday.compareTo(o.getBirthday());
    }
}