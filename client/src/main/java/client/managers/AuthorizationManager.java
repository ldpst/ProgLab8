package client.managers;

import client.client.UDPClient;
import client.exceptions.ServerIsUnavailableException;
import client.screens.MainScreen;
import client.utils.JUtils.JPanelDeb;
import client.utils.Languages;
import server.response.Response;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.IOException;

public class AuthorizationManager {
    private final UDPClient client;
    private final JFrame frame;

    public AuthorizationManager(UDPClient client, JFrame frame) {
        this.client = client;
        this.frame = frame;
    }

    public void logIn(String login, String password) {
        authorize("log_in", login, password);
    }

    public void signUp(String login, String password) {
        authorize("sign_up", login, password);
    }

    private void authorize(String key, String login, String password) {
        String hashPassword = MD2Manager.getHash(password);
        try {
            Response response = client.makeRequest(key, login, hashPassword);
            parseResponse(response, login, password);
        } catch (ServerIsUnavailableException e) {
            buildErrorJDialog(Languages.get("serverIsUnavailable"));
        } catch (IOException e) {
            System.out.println("Вызвана ошибка ввода/вывода");
            throw new RuntimeException(e);
        }
    }

    private void parseResponse(Response response, String login, String password) {
        switch (response.getType()) {
            case PRINT_MESSAGE -> {
                client.setLoginAndPassword(login, password);
                new MainScreen(client, frame).run();
            }
            case ERROR -> {
                buildErrorJDialog(Languages.get(response.getMessage()));
            }
        }
    }

    private void buildErrorJDialog(String errorMessage) {
        JDialog dialog = new JDialog(frame, Languages.get("error"), true);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        Dimension size = new Dimension(300, 200);
        dialog.setPreferredSize(size);
        dialog.setMinimumSize(size);
        dialog.setMaximumSize(size);
        dialog.setLocationRelativeTo(frame);

        dialog.add(buildMainPanel(errorMessage));
        dialog.setVisible(true);
    }

    private JPanel buildMainPanel(String errorMessage) {
        JPanelDeb panel = new JPanelDeb(new GridBagLayout());
        panel.setBorder(new LineBorder(Color.RED, 2));

        panel.add(buildErrorLabel(errorMessage));
        return panel;
    }

    private JLabel buildErrorLabel(String errorMessage) {
        JLabel error = new JLabel(errorMessage);
        error.setForeground(Color.BLACK);
        return error;
    }
}
