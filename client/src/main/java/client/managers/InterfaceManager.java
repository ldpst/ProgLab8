package client.managers;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class InterfaceManager {
    public InterfaceManager() {

    }

    public void run() {
        JFrame frame = new JFrame("Авторизация");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        JPanel panelLoginPassword = new JPanel();
        panelLoginPassword.setLayout(new BoxLayout(panelLoginPassword, BoxLayout.Y_AXIS));
        JTextField loginTextField = getTextField("Логин", 16);
        panelLoginPassword.add(loginTextField);
        panelLoginPassword.add(Box.createRigidArea(new Dimension(0, 1)));

        JTextField passwordTextField = getTextField("Пароль", 16);
        panelLoginPassword.add(passwordTextField);
        panelLoginPassword.add(Box.createRigidArea(new Dimension(0, 1)));

        panelLoginPassword.setBorder(new LineBorder(Color.RED, 2));

        JPanel panelButtons = new JPanel();
        panelButtons.setLayout(new BoxLayout(panelButtons, BoxLayout.X_AXIS));
        panelButtons.setBorder(new LineBorder(Color.BLACK, 2));

        JButton signIn = getButton("Войти");
        panelButtons.add(signIn);

        JButton signUp = getButton("Регистрация");
        panelButtons.add(signUp);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new LineBorder(Color.BLUE, 2));
        mainPanel.add(panelLoginPassword);
        mainPanel.add(panelButtons);


        frame.add(mainPanel);

        frame.setVisible(true);
    }

    private JTextField getTextField(String name, int columns) {
        JTextField textField = new JTextField(columns);
        textField.setText(name);
        textField.setForeground(Color.GRAY);

        Dimension size = new Dimension(300, 30);
        textField.setMaximumSize(size);
        textField.setPreferredSize(size);
        textField.setMinimumSize(size);

        textField.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.GRAY);
                    textField.setText(name);
                }
            }

            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(name)) {
                    textField.setForeground(Color.BLACK);
                    textField.setText("");
                }
            }
        });

        return textField;
    }

    private JButton getButton(String label) {
        JButton button = new JButton(label);
        Dimension size = new Dimension(150, 30);
        button.setPreferredSize(size);
        button.setMaximumSize(size);
        button.setMinimumSize(size);
        return button;
    }
}
