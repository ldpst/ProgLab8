package client.utils;

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

    private static String language = "Russian";

    public Languages() {

    }

    private enum Russian {
        authorizationScreen("Экран авторизации"),
        login("Логин"),
        password("Пароль"),
        logIn("Войти"),
        send("Отправить"),
        signUp("Регистрация"),
        serverIsUnavailable("Сервер в данный момент не доступен"),
        error("Ошибка"),
        authorizationWasSuccessful("Авторизация прошла успешно"),
        loginOrPasswordIsWrong("Неверный логин или пароль"),
        userWithSuchALoginAlreadyExists("Пользователь с таким логином уже существует"),
        registrationWasSuccessful("Регистрация прошла успешно"),
        table("Таблица"),
        schedule("График"),
        mainScreen("Главный экран"),

        help("Помощь"),
        upd_table("Обновить таблицу"),
        add("Добавить"),
        add_if_max("Добавить, если макс"),
        clear("Очистить"),
        count_by_operator("Кол-во по operator"),
        head("Голова"),
        max_by_operator("Макс по operator"),
        remove_by_id("Удалить по id"),
        remove_greater("Удалить больше"),
        update("Обновить по id"),
        count_less_than_genre("Кол-во меньше genre"),
        buildMovie("Заполнение Movie"),

        profile("Профиль"),
        user("Пользователь"),
        name("Название"),
        coordinateX("Координата X"),
        coordinateY("Координата Y"),
        creationDate("Дата создания"),
        oscarsCount("Количество оскаров"),
        genre("Жанр"),
        rating("Рейтинг МПАА"),
        operator("Оператор"),
        operatorName("Имя оператора"),
        operatorBirthday("День рождение оператора"),
        operatorWeight("Вес оператора"),
        operatorPassportID("Паспорт ID оператора"),
        owner("Владелец"),

        validationError("Ошибка валидации:"),
        wrongDateFormat("Введенная дата должна быть формата dd/MM/yyyy"),
        wrongNumberFormat("Введенное значение равно null или не является числом"),
        wrongMovieName("Поле не может быть null, Строка не может быть пустой"),
        wrongOscarsCount("Значение поля должно быть больше 0, Поле не может быть null"),
        wrongPersonName("Поле не может быть null, Строка не может быть пустой"),
        wrongPersonWeight("Значение поля должно быть больше 0"),
        wrongPassportID("Длина строки не должна быть больше 25, Строка не может быть пустой, Поле не может быть null"),

        helpMessage("Справка по доступным командам"),
        tableUpdated("Таблица обновлена"),
        elementAdded("Элемент добавлен"),
        success("Успех"),
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
        authorizationScreen("Avtentikacijski zaslon"),
        login("Prijava"),
        password("Geslo"),
        logIn("Prijavi se"),
        signUp("Registracija"),
        serverIsUnavailable("Strežnik trenutno ni na voljo"),
        error("Napaka"),
        authorizationWasSuccessful("Avtentikacija je bila uspešna"),
        loginOrPasswordIsWrong("Napačno uporabniško ime ali geslo"),
        userWithSuchALoginAlreadyExists("Uporabnik s takšnim uporabniškim imenom že obstaja"),
        registrationWasSuccessful("Registracija je bila uspešna"),
        table("Tabela"),
        schedule("Urnik"),
        mainScreen("Glavni zaslon"),

        help("Pomoč"),
        add("Dodaj"),
        add_if_max("Dodaj, če je največji"),
        clear("Počisti"),
        count_by_operator("Število po operaterju"),
        head("Prvi element"),
        max_by_operator("Največji po operaterju"),
        remove_by_id("Odstrani po ID-ju"),
        remove_greater("Odstrani večje"),
        update("Posodobi po ID-ju"),
        count_less_than_genre("Število je manj kot žanr"),

        profile("Profil"),
        user("Uporabnik"),
        name("Ime"),
        coordinateX("Koordinata X"),
        coordinateY("Koordinata Y"),
        creationDate("Datum ustvarjanja"),
        oscarsCount("Število oskarjev"),
        genre("Žanr"),
        rating("MPAA ocena"),
        operator("Operater"),
        operatorName("Ime operaterja"),
        operatorBirthday("Rojstni dan operaterja"),
        operatorWeight("Teža operaterja"),
        operatorPassportID("ID potnega lista operaterja"),
        owner("Lastnik"),

        validationError("Napaka pri preverjanju:"),
        wrongDateFormat("Vneseni datum mora biti v formatu dd/MM/yyyy"),
        wrongNumberFormat("Vnesena vrednost je null ali ni število"),
        wrongMovieName("Polje ne sme biti null, niz ne sme biti prazen"),
        wrongOscarsCount("Vrednost mora biti večja od 0, polje ne sme biti null"),
        wrongPersonName("Polje ne sme biti null, niz ne sme biti prazen"),
        wrongPersonWeight("Vrednost mora biti večja od 0"),
        wrongPassportID("Dolžina niza ne sme presegati 25, niz ne sme biti prazen, polje ne sme biti null"),

        helpMessage("Pomoč za razpoložljive ukaze"),
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
        authorizationScreen("Écran d'autorisation"),
        login("Identifiant"),
        password("Mot de passe"),
        logIn("Se connecter"),
        signUp("Inscription"),
        serverIsUnavailable("Le serveur est actuellement indisponible"),
        error("Erreur"),
        authorizationWasSuccessful("L'autorisation a réussi"),
        loginOrPasswordIsWrong("Identifiant ou mot de passe incorrect"),
        userWithSuchALoginAlreadyExists("Un utilisateur avec cet identifiant existe déjà"),
        registrationWasSuccessful("L'inscription a réussi"),
        table("Tableau"),
        schedule("Emploi du temps"),
        mainScreen("Écran principal"),

        help("Aide"),
        add("Ajouter"),
        add_if_max("Ajouter si c'est le plus grand"),
        clear("Vider"),
        count_by_operator("Nombre par opérateur"),
        head("Premier élément"),
        max_by_operator("Max par opérateur"),
        remove_by_id("Supprimer par ID"),
        remove_greater("Supprimer les plus grands"),
        update("Mettre à jour par ID"),
        count_less_than_genre("Le nombre est inférieur au genre"),

        profile("Profil"),
        user("Utilisateur"),
        name("Nom"),
        coordinateX("Coordonnée X"),
        coordinateY("Coordonnée Y"),
        creationDate("Date de création"),
        oscarsCount("Nombre d'Oscars"),
        genre("Genre"),
        rating("Classement MPAA"),
        operator("Opérateur"),
        operatorName("Nom de l'opérateur"),
        operatorBirthday("Date de naissance de l'opérateur"),
        operatorWeight("Poids de l'opérateur"),
        operatorPassportID("ID de passeport de l'opérateur"),
        owner("Propriétaire"),

        validationError("Erreur de validation :"),
        wrongDateFormat("La date doit être au format jj/MM/aaaa"),
        wrongNumberFormat("La valeur saisie est nulle ou n’est pas un nombre"),
        wrongMovieName("Le champ ne peut pas être nul, la chaîne ne peut pas être vide"),
        wrongOscarsCount("La valeur doit être supérieure à 0, le champ ne peut pas être nul"),
        wrongPersonName("Le champ ne peut pas être nul, la chaîne ne peut pas être vide"),
        wrongPersonWeight("La valeur doit être supérieure à 0"),
        wrongPassportID("La longueur de la chaîne ne doit pas dépasser 25, ne peut pas être vide ou nulle"),

        helpMessage("Aide sur les commandes disponibles"),
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
        authorizationScreen("Pantalla de autorización"),
        login("Usuario"),
        password("Contraseña"),
        logIn("Iniciar sesión"),
        signUp("Registrarse"),
        serverIsUnavailable("El servidor no está disponible en este momento"),
        error("Error"),
        authorizationWasSuccessful("Autorización exitosa"),
        loginOrPasswordIsWrong("Usuario o contraseña incorrectos"),
        userWithSuchALoginAlreadyExists("Ya existe un usuario con ese nombre"),
        registrationWasSuccessful("Registro exitoso"),
        table("Tabla"),
        schedule("Horario"),
        mainScreen("Pantalla principal"),

        help("Ayuda"),
        add("Agregar"),
        add_if_max("Agregar si es el máximo"),
        clear("Limpiar"),
        count_by_operator("Cantidad por operador"),
        head("Primer elemento"),
        max_by_operator("Máximo por operador"),
        remove_by_id("Eliminar por ID"),
        remove_greater("Eliminar mayores"),
        update("Actualizar por ID"),
        count_less_than_genre("El número es menor que el género"),

        profile("Perfil"),
        user("Usuario"),
        name("Nombre"),
        coordinateX("Coordenada X"),
        coordinateY("Coordenada Y"),
        creationDate("Fecha de creación"),
        oscarsCount("Cantidad de Óscars"),
        genre("Género"),
        rating("Clasificación MPAA"),
        operator("Operador"),
        operatorName("Nombre del operador"),
        operatorBirthday("Fecha de nacimiento del operador"),
        operatorWeight("Peso del operador"),
        operatorPassportID("ID del pasaporte del operador"),
        owner("Propietario"),

        validationError("Error de validación:"),
        wrongDateFormat("La fecha debe tener el formato dd/MM/yyyy"),
        wrongNumberFormat("El valor ingresado es null o no es un número"),
        wrongMovieName("El campo no puede ser null y la cadena no puede estar vacía"),
        wrongOscarsCount("El valor debe ser mayor que 0, el campo no puede ser null"),
        wrongPersonName("El campo no puede ser null y la cadena no puede estar vacía"),
        wrongPersonWeight("El valor debe ser mayor que 0"),
        wrongPassportID("La longitud de la cadena no debe superar los 25 caracteres, no puede estar vacía ni ser null"),

        helpMessage("Ayuda sobre los comandos disponibles"),
        ;

        final String string;

        Spanish(String string) {
            this.string = string;
        }

        public String toString() {
            return string;
        }
    }

    public static String get(String string) {
        Enum<?>[] constants = languages.get(language).getEnumConstants();
        for (var constant : constants) {
            if (constant.name().equals(string)) {
                return constant.toString();
            }
        }
        return null;
    }

    public static void setLanguage(String language) {
        Languages.language = language;
    }

    public static String getLanguage() {
        return Languages.language;
    }

    public static String[] getLangs() {
        return langs;
    }

    public static String[] getLocalNames() {
        return localNames;
    }

    public static Pair<Locale, ZoneId> getCurrentLocale() {
        return switch (language) {
            case "Russian" -> new Pair<>(Locale.forLanguageTag("ru"), ZoneId.of("Europe/Moscow"));
            case "Slovenian" -> new Pair<>(Locale.forLanguageTag("sl"), ZoneId.of("Europe/Ljubljana"));
            case "French" -> new Pair<>(Locale.forLanguageTag("fr-FR"), ZoneId.of("Europe/Paris"));
            case "Spanish" -> new Pair<>(Locale.forLanguageTag("es-NI"), ZoneId.of("America/Managua"));
            default -> null;
        };
    }

    public static String getTranslation(String[] message) {
        return switch(language) {
            case "Russian" -> message[0];
            case "Slovenian" -> message[1];
            case "French" -> message[2];
            case "Spanish" -> message[3];
            default -> null;
        };
    }
}
