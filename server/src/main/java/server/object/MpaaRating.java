package server.object;

import java.io.Serializable;

/**
 * Класс Мпа рейтинга
 *
 * @author ldpst
 */
public enum MpaaRating implements Serializable {
    G,
    PG,
    PG_13,
    R,
    NC_17;

    /**
     * Метод, переводящий строку в верхний регистр и проверяющий на принадлежность к данному enum'у
     *
     * @param s строка
     * @return результат проверки
     */
    public static MpaaRating checkOf(String s) {
        MpaaRating genre;
        try {
            genre = MpaaRating.valueOf(s.toUpperCase());
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