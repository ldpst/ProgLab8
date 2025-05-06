package client.screens;

import client.utils.JPanelDeb;
import client.utils.Languages;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class AuthorizationScreen {
    public AuthorizationScreen() {

    }

    public void run() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle(Languages.get("authorizationScreen"));
        frame.setSize(600, 400);

        JPanelDeb languagePanel = new JPanelDeb();
        languagePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        languagePanel.setBorder(new LineBorder(Color.GREEN));

        String[] languages = Languages.getLangs();
        JComboBox<String> languageComboBox = new JComboBox<>(languages);
        languagePanel.add(languageComboBox);

        JPanelDeb panelLoginPassword = new JPanelDeb();
        panelLoginPassword.setLayout(new BoxLayout(panelLoginPassword, BoxLayout.Y_AXIS));
        JTextField loginTextField = getTextField(Languages.get("login"));
        panelLoginPassword.add(loginTextField);
        panelLoginPassword.add(Box.createRigidArea(new Dimension(0, 1)));

        JTextField passwordTextField = getTextField(Languages.get("password"));
        panelLoginPassword.add(passwordTextField);
        panelLoginPassword.add(Box.createRigidArea(new Dimension(0, 1)));

        panelLoginPassword.setBorder(new LineBorder(Color.RED, 2));

        JPanelDeb panelButtons = new JPanelDeb();
        panelButtons.setLayout(new BoxLayout(panelButtons, BoxLayout.X_AXIS));
        panelButtons.setBorder(new LineBorder(Color.BLACK, 2));

        JButton logIn = getButton(Languages.get("logIn"));
        panelButtons.add(logIn);

        JButton signUp = getButton(Languages.get("signUp"));
        panelButtons.add(signUp);

        JPanelDeb joinPanel = new JPanelDeb();
        joinPanel.setLayout(new BoxLayout(joinPanel, BoxLayout.Y_AXIS));
        joinPanel.setBorder(new LineBorder(Color.BLUE, 2));
        joinPanel.add(panelLoginPassword);
        joinPanel.add(panelButtons);

        JPanelDeb outerPanel = new JPanelDeb();
        outerPanel.setLayout(new GridBagLayout());
        outerPanel.setBorder(new LineBorder(Color.YELLOW, 2));
        outerPanel.add(joinPanel);

        JPanelDeb mainPanel = new JPanelDeb();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(new LineBorder(Color.PINK, 2));
        mainPanel.add(languagePanel, BorderLayout.PAGE_START);
        mainPanel.add(outerPanel, BorderLayout.CENTER);

        frame.add(mainPanel);

        frame.setVisible(true);

        SwingUtilities.invokeLater(frame::requestFocusInWindow); // для снятия фокуса с loginTextField
    }

    private JTextField getTextField(String name) {
        JTextField textField = new JTextField(16);
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
