package client.screens;

import client.utils.Languages;

import javax.swing.*;

public class AuthorizationScreen {
    public AuthorizationScreen() {

    }

    public void run() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle(Languages.get("authorizationScreen"));

        frame.setVisible(true);
    }
}
