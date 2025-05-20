package client.utils.JUtils;

import client.utils.Languages;

import javax.swing.*;
import java.awt.*;

public class DialogBuilder {
    public static JDialog buildDefaultJDialog(String key, int x, int y, JFrame frame) {
        JDialog dialog = new JDialog();
        dialog.setLayout(new GridBagLayout());
        dialog.setTitle(Languages.get(key));
        dialog.setModal(true);
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dialog.setSize(new Dimension(x, y));
        dialog.setLocationRelativeTo(frame);

        return dialog;
    }

    public static void showServerIsUnavailableDialog(JFrame frame) {
        JOptionPane.showMessageDialog(
                frame,
                Languages.get("serverIsUnavailable"),
                Languages.get("error"),
                JOptionPane.ERROR_MESSAGE
        );
    }

    public static void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(
                null,
                Languages.get("validationError") + " " + message,
                Languages.get("error"),
                JOptionPane.ERROR_MESSAGE
        );
    }

    public static void showSuccessDialog(String message, Window frame) {
        JOptionPane.showMessageDialog(
                frame,
                message,
                Languages.get("success"),
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
