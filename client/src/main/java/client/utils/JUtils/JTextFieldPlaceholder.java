package client.utils.JUtils;

import client.utils.Languages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class JTextFieldPlaceholder extends JTextField {
    private String placeholderKey = "";
    private FocusListener focusListener = buildFocusListener(this);

    public JTextFieldPlaceholder() {
        super();
    }

    public JTextFieldPlaceholder(int columns) {
        super(columns);
    }

    public JTextFieldPlaceholder(String text) {
        super(text);
    }
    public JTextFieldPlaceholder(String text, int columns) {
        super(text, columns);
    }

    public void setPlaceholderKey(String placeholderKey) {
        this.placeholderKey = placeholderKey;
        removeFocusListener(focusListener);
        if (getForeground().equals(Color.GRAY) || getText() == null || getText().isEmpty()) {
            setForeground(Color.GRAY);
            setText(placeholderKey);
        }
        focusListener = buildFocusListener(this);
        addFocusListener(focusListener);
    }

    public FocusListener buildFocusListener(JTextField textField) {
        return new FocusAdapter() {
            final String text = placeholderKey;
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
}
