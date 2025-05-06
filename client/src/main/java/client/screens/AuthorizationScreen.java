package client.screens;

import client.utils.JPanelDeb;
import client.utils.Languages;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class AuthorizationScreen {
    private JFrame frame;
    private JTextField loginTextField;
    private FocusListener loginFocusListener;
    private JTextField passwordTextField;
    private FocusListener passwordFocusListener;
    private JButton logInButton;
    private JButton signUpButton;

    public AuthorizationScreen() {

    }

    public void run() {
        initializeFrame();
        frame.add(buildMainPanel());
        frame.setVisible(true);
        SwingUtilities.invokeLater(frame::requestFocusInWindow); // для снятия фокуса с loginTextField
    }

    private void initializeFrame() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle(Languages.get("authorizationScreen"));
        frame.setSize(600, 400);
        frame.setMinimumSize(new Dimension(339, 202));
    }

    private JPanelDeb buildMainPanel() {
        JPanelDeb mainPanel = new JPanelDeb(new BorderLayout());
        mainPanel.setBorder(new LineBorder(Color.PINK, 2));
        mainPanel.add(buildLanguagePanel(), BorderLayout.PAGE_START);
        mainPanel.add(buildCenterPanel(), BorderLayout.CENTER);
        return mainPanel;
    }

    private JPanelDeb buildLanguagePanel() {
        JPanelDeb panel = new JPanelDeb();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBorder(new LineBorder(Color.GREEN));
        panel.add(buildLanguageComboBox());
        return panel;
    }

    private JComboBox<String> buildLanguageComboBox() {
        JComboBox<String> comboBox = new JComboBox<>(Languages.getLangs());
        comboBox.setSelectedItem(Languages.getLanguage());
        comboBox.addActionListener(e -> {
            int selectedLanguage = comboBox.getSelectedIndex();
            Languages.setLanguage(Languages.getLocalNames()[selectedLanguage]);
            updateLocal();
        });
        return comboBox;
    }

    private JPanelDeb buildCenterPanel() {
        JPanelDeb outerPanel = new JPanelDeb(new GridBagLayout());
        outerPanel.setBorder(new LineBorder(Color.YELLOW, 2));

        JPanelDeb joinPanel = new JPanelDeb();
        joinPanel.setLayout(new BoxLayout(joinPanel, BoxLayout.Y_AXIS));
        joinPanel.setBorder(new LineBorder(Color.BLUE, 2));
        joinPanel.add(buildLoginPasswordPanel());
        joinPanel.add(buildButtonPanel());

        outerPanel.add(joinPanel);
        return outerPanel;
    }

    private JPanelDeb buildLoginPasswordPanel() {
        JPanelDeb panel = new JPanelDeb();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new LineBorder(Color.RED, 2));

        loginTextField = buildTextField("login");
        panel.add(loginTextField);
        panel.add(Box.createRigidArea(new Dimension(0, 1)));

        passwordTextField = buildTextField("password");
        panel.add(passwordTextField);
        panel.add(Box.createRigidArea(new Dimension(0, 1)));

        return panel;
    }

    private JTextField buildTextField(String key) {
        JTextField textField = new JTextField(16);
        setPlaceholder(textField, key);

        Dimension size = new Dimension(300, 30);
        textField.setMaximumSize(size);
        textField.setPreferredSize(size);
        textField.setMinimumSize(size);

        FocusListener focusListener = buildFocusListener(textField, key);
        textField.addFocusListener(focusListener);
        if (key.equals("login")) loginFocusListener = focusListener;
        else passwordFocusListener = focusListener;

        return textField;
    }

    private void setPlaceholder(JTextField textField, String key) {
        textField.setText(Languages.get(key));
        textField.setForeground(Color.GRAY);
    }

    private FocusListener buildFocusListener(JTextField textField, String name) {
        return new FocusAdapter() {
            final String text = Languages.get(name);
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(text)) {
                    textField.setForeground(Color.BLACK);
                    textField.setText("");
                }
            }

            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.GRAY);
                    textField.setText(text);
                }
            }
        };
    }

    private JPanelDeb buildButtonPanel() {
        JPanelDeb panel = new JPanelDeb();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBorder(new LineBorder(Color.BLACK, 2));

        logInButton = buildButton("logIn");
        signUpButton = buildButton("signUp");

        panel.add(logInButton);
        panel.add(Box.createRigidArea(new Dimension(1, 0)));
        panel.add(signUpButton);

        return panel;
    }

    private JButton buildButton(String key) {
        JButton button = new JButton(Languages.get(key));
        Dimension size = new Dimension(148, 30);
        button.setPreferredSize(size);
        button.setMaximumSize(size);
        button.setMinimumSize(size);
        return button;
    }

    private void updateLocal() {
        frame.setTitle(Languages.get("authorizationScreen"));

        updateTextField(loginTextField, loginFocusListener, "login");
        loginFocusListener = buildFocusListener(loginTextField, "login");
        loginTextField.addFocusListener(loginFocusListener);

        updateTextField(passwordTextField, passwordFocusListener, "password");
        passwordFocusListener = buildFocusListener(passwordTextField, "password");
        passwordTextField.addFocusListener(passwordFocusListener);

        logInButton.setText(Languages.get("logIn"));
        signUpButton.setText(Languages.get("signUp"));
    }

    private void updateTextField(JTextField textField, FocusListener focusListener, String key) {
        textField.removeFocusListener(focusListener);
        if (textField.getForeground().equals(Color.GRAY)) {
            textField.setText(Languages.get(key));
        }
    }
}
