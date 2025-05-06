package client.utils;

import java.util.HashMap;
import java.util.Map;

public class Languages {
    private static final Map<String, Class<? extends Enum<?>>> languages = new HashMap<>();

    static {
        languages.put("Russian", Russian.class);
        languages.put("English", English.class);
    }

    private static String language = "Russian";

    public Languages() {

    }

    private enum Russian {
        authorizationScreen("Экран авторизации");

        final String string;

        Russian(String string) {
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

    public void setLanguage(String language) {
        Languages.language = language;
    }
}
