package client.utils.JUtils;

import client.client.UDPClient;
import client.utils.Languages;
import server.managers.CommandManager;
import server.response.Response;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JCommandPanel extends JPanel {
    private final UDPClient client;

    private final JFrame frame;

    Map<String, JButton> buttons = new HashMap<>();

    public JCommandPanel(UDPClient client, JFrame frame) {
        this.client = client;
        this.frame = frame;

        setLayout(new GridBagLayout());

        buildButtons();

        add(Box.createGlue(), buildGBC(0, 15, GridBagConstraints.BOTH, 0, 0, 0, 0, 1, 1));
    }

    private void buildButtons() {
        buttons.put("help", buildButton("help"));
        buttons.get("help").addActionListener(helpListener());
        add(buttons.get("help"), buildGBC(0, 0, GridBagConstraints.HORIZONTAL, 0, 1, 0, 10, 1, 0));
    }

    private JButton buildButton(String key) {
        JButton button = new JButton(Languages.get(key));
        button.setPreferredSize(new Dimension(0, 40));
        return button;
    }

    private ActionListener helpListener() {
        return e -> {
            JDialog dialog = new JDialog();
            dialog.setTitle(Languages.get("help"));
            dialog.setModal(true);
            dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            dialog.setSize(new Dimension(400, 300));
            dialog.setLocationRelativeTo(frame);

            Response response;
            try {
                response = client.makeRequest("help", client.getLogin(), client.getPassword());
            } catch (IOException ex) {
                System.out.println("Ошибка при запросе на сервер");
                throw new RuntimeException(ex);
            }

            JLabel label = new JLabel(response.getMessage());
            dialog.add(label);

            dialog.setVisible(true);
        };
    }

    private GridBagConstraints buildGBC(int gridx, int gridy, int fill, int top, int left, int bottom, int right, double weightx, double weighty) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.fill = fill;
        gbc.insets = new Insets(top, left, bottom, right);
        gbc.weightx = weightx;
        gbc.weighty = weighty;
        return gbc;
    }
}
