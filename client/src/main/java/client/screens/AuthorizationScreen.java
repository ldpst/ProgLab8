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

        JPanelDeb languagePanel = getLanguagePanel();

        JPanelDeb panelLoginPassword = getLoginPasswordPanel();

        JPanelDeb panelButtons = getButtonPanel();

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

    private JPanelDeb getLanguagePanel() {
        JPanelDeb panel = new JPanelDeb();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBorder(new LineBorder(Color.GREEN));

        JComboBox<String> languageComboBox = getLanguageComboBox();
        panel.add(languageComboBox);

        return panel;
    }

    private JComboBox<String> getLanguageComboBox() {
        String[] languages = Languages.getLangs();
        JComboBox<String> comboBox = new JComboBox<>(languages);

        return comboBox;
    }

    private JPanelDeb getLoginPasswordPanel() {
        JPanelDeb panel = new JPanelDeb();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new LineBorder(Color.RED, 2));

        JTextField loginTextField = getTextField(Languages.get("login"));
        panel.add(loginTextField);
        panel.add(Box.createRigidArea(new Dimension(0, 1)));

        JTextField passwordTextField = getTextField(Languages.get("password"));
        panel.add(passwordTextField);
        panel.add(Box.createRigidArea(new Dimension(0, 1)));

        return panel;
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

    private JPanelDeb getButtonPanel() {
        JPanelDeb panel = new JPanelDeb();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBorder(new LineBorder(Color.BLACK, 2));

        JButton logIn = getButton(Languages.get("logIn"));
        panel.add(logIn);

        JButton signUp = getButton(Languages.get("signUp"));
        panel.add(signUp);

        return panel;
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
