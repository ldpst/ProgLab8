package client;

import client.screens.AuthorizationScreen;
import client.utils.Languages;

public class InterfaceMain {
    public static void main(String[] args) {
        Languages.setLanguage("Russian");
        new AuthorizationScreen().run();
    }
}
