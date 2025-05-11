package server.object;

import java.io.Serializable;

/**
 * Класс жанра фильма
 *
 * @author ldpst
 */
public enum MovieGenre implements Serializable, Comparable<MovieGenre> {
    DRAMA,
    MUSICAL,
    HORROR;

    /**
     * Метод, переводящий строку в верхний регистр и проверяющий на принадлежность к данному enum'у
     *
     * @param s строка
     * @return результат проверки
     */
    public static MovieGenre checkOf(String s) {
        MovieGenre genre;
        try {
            genre = MovieGenre.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            genre = null;
        }
        if (genre == null) {
            try {
                genre = values()[Integer.parseInt(s) - 1];
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                //
            }
        }
        if (genre == null) {
            throw new IllegalArgumentException();
        }
        return genre;
    }
}