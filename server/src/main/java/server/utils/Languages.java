package server.utils;

import server.utils.Pair;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Languages {
    private static final Map<String, Class<? extends Enum<?>>> languages = new HashMap<>();
    private static final String[] langs = {"Русский", "Slovenski", "Français", "Español (nicaragua)"};
    private static final String[] localNames = {"Russian", "Slovenian", "French", "Spanish"};

    static {
        languages.put("Russian", Russian.class);
        languages.put("Slovenian", Slovenian.class);
        languages.put("French", French.class);
        languages.put("Spanish", Spanish.class);
    }

    public Languages() {

    }

    private enum Russian {
        help("вывести справку по доступным командам"),
        show("вывести в стандартный поток вывода все элементы коллекции в строковом представлении"),
        add("добавить новый элемент в коллекцию"),
        add_if_max("добавить новый элемент, если он максимален"),
        update("обновить значение элемента коллекции, id которого равен заданному"),
        remove_by_id("удалить элемент из коллекции по его id"),
        clear("очистить коллекцию"),
        save("сохранить коллекцию в файл"),
        exit("завершить программу (без сохранения в файл)"),
        head("вывести первый элемент коллекции"),
        remove_greater("удалить из коллекции все элементы, превышающие заданный"),
        count_less_than_genre("посчитать количество элементов, значение genre которых меньше заданного"),
        count_by_operator("посчитать количество элементов, значение operator которых равно заданному"),
        max_by_operator("вывести элемент, значение operator которого максимально"),
        
        upd_table("обновить значения в таблице"),

        elementAdded("Элемент успешно добавлен"),
        elementNotAdded("Элемент не добавлен"),
        collectionCleared("Коллекция очищена"),
        elementRemoved("Элемент с данным id успешно удален"),
        elementDoesNotAppear("Элемент с данным id не существует, или у Вас нет прав для его удаления"),
        elementUpdated("Элемент успешно обновлен"),
        deletedNElements("Удалено {} элемент(ов)"),
        elementWithThisOperator("Элементов с данным operator"),
        elementLessThanGenre("Элементов меньше данного genre"),
        ;

        final String string;

        Russian(String string) {
            this.string = string;
        }

        public String toString() {
            return string;
        }
    }

    private enum Slovenian {
        help("prikaži pomoč o razpoložljivih ukazih"),
        show("izpiši vse elemente zbirke v obliki niza"),
        add("dodaj nov element v zbirko"),
        add_if_max("dodaj nov element, če je največji"),
        update("posodobi vrednost elementa zbirke z določenim id-jem"),
        remove_by_id("odstrani element iz zbirke po id-ju"),
        clear("počisti zbirko"),
        save("shrani zbirko v datoteko"),
        exit("končaj program (brez shranjevanja v datoteko)"),
        head("prikaži prvi element zbirke"),
        remove_greater("odstrani iz zbirke vse elemente, večje od podanega"),
        count_less_than_genre("preštej elemente, katerih genre je manjši od podanega"),
        count_by_operator("preštej elemente, katerih operator je enak podanemu"),
        max_by_operator("prikaži element z največjo vrednostjo operatorja"),
        deletedNElements("Удалено {} элемент(ов)"),
        ;

        final String string;

        Slovenian(String string) {
            this.string = string;
        }

        public String toString() {
            return string;
        }
    }

    private enum French {
        help("afficher l'aide sur les commandes disponibles"),
        show("afficher tous les éléments de la collection sous forme de chaîne"),
        add("ajouter un nouvel élément à la collection"),
        add_if_max("ajouter un nouvel élément s'il est le plus grand"),
        update("mettre à jour la valeur de l'élément dont l'identifiant est donné"),
        remove_by_id("supprimer un élément de la collection par son id"),
        clear("vider la collection"),
        save("sauvegarder la collection dans un fichier"),
        exit("quitter le programme (sans sauvegarde)"),
        head("afficher le premier élément de la collection"),
        remove_greater("supprimer de la collection tous les éléments supérieurs à celui donné"),
        count_less_than_genre("compter les éléments dont la valeur de genre est inférieure à celle donnée"),
        count_by_operator("compter les éléments dont l'opérateur est égal à celui donné"),
        max_by_operator("afficher l'élément dont la valeur d'opérateur est maximale"),
        deletedNElements("Удалено {} элемент(ов)"),
        ;

        final String string;

        French(String string) {
            this.string = string;
        }

        public String toString() {
            return string;
        }
    }

    private enum Spanish {
        help("mostrar ayuda sobre los comandos disponibles"),
        show("mostrar todos los elementos de la colección en forma de cadena"),
        add("agregar un nuevo elemento a la colección"),
        add_if_max("agregar un nuevo elemento si es el máximo"),
        update("actualizar el valor del elemento cuyo id sea el especificado"),
        remove_by_id("eliminar un elemento de la colección por su id"),
        clear("limpiar la colección"),
        save("guardar la colección en un archivo"),
        exit("salir del programa (sin guardar en archivo)"),
        head("mostrar el primer elemento de la colección"),
        remove_greater("eliminar de la colección todos los elementos mayores al especificado"),
        count_less_than_genre("contar los elementos cuyo género sea menor al especificado"),
        count_by_operator("contar los elementos cuyo operador sea igual al especificado"),
        max_by_operator("mostrar el elemento cuyo valor de operador sea el máximo"),
        deletedNElements("Удалено {} элемент(ов)"),
        ;

        final String string;

        Spanish(String string) {
            this.string = string;
        }

        public String toString() {
            return string;
        }
    }

    public static String get(String string, String language) {
        Enum<?>[] constants = languages.get(language).getEnumConstants();
        for (var constant : constants) {
            if (constant.name().equals(string)) {
                return constant.toString();
            }
        }
        return null;
    }

    public static String[] getLangs() {
        return langs;
    }

    public static String[] getLocalNames() {
        return localNames;
    }

    public static Pair<Locale, ZoneId> getCurrentLocale(String language) {
        return switch (language) {
            case "Russian" -> new Pair<>(Locale.forLanguageTag("ru"), ZoneId.of("Europe/Moscow"));
            case "Slovenian" -> new Pair<>(Locale.forLanguageTag("sl"), ZoneId.of("Europe/Ljubljana"));
            case "French" -> new Pair<>(Locale.forLanguageTag("fr-FR"), ZoneId.of("Europe/Paris"));
            case "Spanish" -> new Pair<>(Locale.forLanguageTag("es-NI"), ZoneId.of("America/Managua"));
            default -> null;
        };
    }
}
