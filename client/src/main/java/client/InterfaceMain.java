package client;

import client.client.UDPClient;
import client.screens.AuthorizationScreen;
import client.screens.MainScreen;
import client.utils.Languages;

import java.io.IOException;

public class InterfaceMain {
    public static void main(String[] args) throws IOException {
        Languages.setLanguage("Russian");
        UDPClient client = new UDPClient();
        new AuthorizationScreen(client).run();
    }
}
