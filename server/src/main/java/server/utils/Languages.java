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
        help("prikaži pomoč za razpoložljive ukaze"),
        show("prikaži vse elemente zbirke v nizu"),
        add("dodaj nov element v zbirko"),
        add_if_max("dodaj nov element, če je največji"),
        update("posodobi element zbirke s podanim ID-jem"),
        remove_by_id("odstrani element iz zbirke po ID-ju"),
        clear("počisti zbirko"),
        save("shrani zbirko v datoteko"),
        exit("končaj program (brez shranjevanja)"),
        head("prikaži prvi element zbirke"),
        remove_greater("odstrani vse elemente, večje od danega"),
        count_less_than_genre("preštej elemente z žanrom manjšim od danega"),
        count_by_operator("preštej elemente z določenim operatorjem"),
        max_by_operator("prikaži element z največjim operatorjem"),

        upd_table("posodobi vrednosti v tabeli"),

        elementAdded("Element je bil uspešno dodan"),
        elementNotAdded("Element ni bil dodan"),
        collectionCleared("Zbirka je bila počiščena"),
        elementRemoved("Element z danim ID-jem je bil uspešno odstranjen"),
        elementDoesNotAppear("Element z danim ID-jem ne obstaja ali nimate dovoljenja za brisanje"),
        elementUpdated("Element je bil uspešno posodobljen"),
        deletedNElements("Izbrisanih je bilo {} elementov"),
        elementWithThisOperator("Število elementov z danim operatorjem"),
        elementLessThanGenre("Število elementov z žanrom manjšim od danega"),
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
        update("mettre à jour un élément de la collection avec l'ID spécifié"),
        remove_by_id("supprimer un élément de la collection par ID"),
        clear("vider la collection"),
        save("enregistrer la collection dans un fichier"),
        exit("quitter le programme (sans sauvegarde)"),
        head("afficher le premier élément de la collection"),
        remove_greater("supprimer tous les éléments supérieurs au donné"),
        count_less_than_genre("compter les éléments dont le genre est inférieur au donné"),
        count_by_operator("compter les éléments avec l'opérateur spécifié"),
        max_by_operator("afficher l'élément avec l'opérateur maximal"),

        upd_table("mettre à jour les valeurs dans le tableau"),

        elementAdded("Élément ajouté avec succès"),
        elementNotAdded("Élément non ajouté"),
        collectionCleared("Collection vidée"),
        elementRemoved("Élément avec cet ID supprimé avec succès"),
        elementDoesNotAppear("Élément avec cet ID inexistant ou sans droits pour le supprimer"),
        elementUpdated("Élément mis à jour avec succès"),
        deletedNElements("{} élément(s) supprimé(s)"),
        elementWithThisOperator("Nombre d'éléments avec cet opérateur"),
        elementLessThanGenre("Nombre d'éléments avec un genre inférieur au donné"),

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
        show("mostrar todos los elementos de la colección en formato de texto"),
        add("agregar un nuevo elemento a la colección"),
        add_if_max("agregar el elemento si es el máximo"),
        update("actualizar el elemento con el ID proporcionado"),
        remove_by_id("eliminar un elemento de la colección por su ID"),
        clear("limpiar la colección"),
        save("guardar la colección en un archivo"),
        exit("cerrar el programa (sin guardar)"),
        head("mostrar el primer elemento de la colección"),
        remove_greater("eliminar todos los elementos mayores al especificado"),
        count_less_than_genre("contar los elementos con género menor al indicado"),
        count_by_operator("contar los elementos con el operador especificado"),
        max_by_operator("mostrar el elemento con el operador más alto"),

        upd_table("actualizar los valores de la tabla"),

        elementAdded("Elemento agregado exitosamente"),
        elementNotAdded("Elemento no agregado"),
        collectionCleared("Colección vaciada"),
        elementRemoved("Elemento con el ID indicado eliminado exitosamente"),
        elementDoesNotAppear("El elemento con ese ID no existe o no tiene permiso para eliminarlo"),
        elementUpdated("Elemento actualizado con éxito"),
        deletedNElements("{} elemento(s) eliminado(s)"),
        elementWithThisOperator("Cantidad de elementos con este operador"),
        elementLessThanGenre("Cantidad de elementos con género menor al indicado"),

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
