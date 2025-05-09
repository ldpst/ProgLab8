package client.utils;

import java.util.HashMap;
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
        authorizationScreen("Zaslon za avtorizacijo"),
        login("Prijava"),
        password("Geslo"),
        logIn("Vstopite"),
        signUp("Registracija"),
        serverIsUnavailable("Strežnik trenutno ni na voljo"),
        error("Napaka"),
        authorizationWasSuccessful("Pooblastilo je bilo uspešno"),
        loginOrPasswordIsWrong("Napačna prijava ali geslo"),
        userWithSuchALoginAlreadyExists("Uporabnik s takšno prijavo že obstaja"),
        registrationWasSuccessful("Registracija je bila uspešna"),
        table("Miza"),
        schedule("Urnik"),
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
        login("Se connecter"),
        password("Mot de passe"),
        logIn("Entrer"),
        signUp("Inscription"),
        serverIsUnavailable("Le serveur n'est actuellement pas disponible"),
        error("Erreur"),
        authorizationWasSuccessful("L'autorisation a réussi"),
        loginOrPasswordIsWrong("Mauvaise connexion ou mot de passe"),
        userWithSuchALoginAlreadyExists("Un utilisateur avec une telle connexion existe déjà"),
        registrationWasSuccessful("L'inscription a réussi"),
        table("Tableau"),
        schedule("Calendrier"),
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
        login("Acceso"),
        password("Contraseña"),
        logIn("Ingresar"),
        signUp("Registro"),
        serverIsUnavailable("El servidor no está actualmente disponible"),
        error("Error"),
        authorizationWasSuccessful("La autorización fue exitosa"),
        loginOrPasswordIsWrong("Iniciar sesión o contraseña incorrectos"),
        userWithSuchALoginAlreadyExists("Un usuario con tal inicio de sesión ya existe"),
        registrationWasSuccessful("El registro fue exitoso"),
        table("Tableau"),
        schedule("Calendrier"),
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
}
