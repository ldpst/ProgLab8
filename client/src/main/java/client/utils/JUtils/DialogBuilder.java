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

    public static JDialog buildServerIsUnavailableDialog(JFrame frame) {
        JDialog dialog = DialogBuilder.buildDefaultJDialog(Languages.get("error"), 400, 300, frame);
        dialog.setLayout(new BorderLayout());
        JLabel label = new JLabel("<html><div style='font-size:20pt;'>" + Languages.get("serverIsUnavailable") + "</div></html>");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        dialog.add(label, BorderLayout.CENTER);
        return dialog;
    }

    public static void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(
                null,
                Languages.get("validationError") + " " + message,
                Languages.get("error"),
                JOptionPane.ERROR_MESSAGE
        );
    }
}
