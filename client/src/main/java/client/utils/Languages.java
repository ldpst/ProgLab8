package client.utils;

import java.util.HashMap;
import java.util.Map;

public class Languages {
    private static final Map<String, Class<? extends Enum<?>>> languages = new HashMap<>();
    private static final String[] langs = {"Русский", "Slovenski", "Français", "Español (nicaragua)"};

    static {
        languages.put("Russian", Russian.class);
        languages.put("Slovenian", Slovenian.class);
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
        authorizationScreen("Zaslon za avtorizacijo");

        final String string;

        Slovenian(String string) {
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

    public static String[] getLangs() {
        return langs;
    }
}
