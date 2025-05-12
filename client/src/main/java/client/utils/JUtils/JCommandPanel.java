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
import java.util.Objects;

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
            dialog.setLayout(new GridBagLayout());
            dialog.setTitle(Languages.get("help"));
            dialog.setModal(true);
            dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            dialog.setSize(new Dimension(630, 260));
            dialog.setLocationRelativeTo(frame);

            Response response;
            try {
                response = client.makeRequest("help", client.getLogin(), client.getPassword());
            } catch (IOException ex) {
                System.out.println("Ошибка при запросе на сервер");
                throw new RuntimeException(ex);
            }

            String[] msg = Objects.requireNonNull(Languages.getTranslation(response.getTranslate())).split("\n");
            StringBuilder builder = new StringBuilder();
            for (String s : msg) {
                String[] split = s.split(" : ");
                builder.append(Languages.get(split[0])).append(" : ").append(split[1]).append("<br>");
            }
            JLabel label = new JLabel("<html>" + "<div style='font-size:20pt;'>" + Languages.get("helpMessage") + "</div>" + builder + "</html>");
            dialog.add(label, buildGBC(0, 0, GridBagConstraints.BOTH, 0, 10, 0, 10, 1, 1));

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
