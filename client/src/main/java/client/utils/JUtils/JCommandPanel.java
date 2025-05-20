package client.utils.JUtils;

import client.client.UDPClient;
import client.exceptions.ServerIsUnavailableException;
import client.utils.GBCUtils;
import client.utils.Languages;
import server.object.*;
import server.response.Response;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class JCommandPanel extends JPanel {
    private final UDPClient client;

    private final JFrame frame;
    private final JMovieTable table;

    Map<String, JButton> buttons = new HashMap<>();

    private final Listeners listeners = new Listeners();

    public JCommandPanel(UDPClient client, JFrame frame, JMovieTable table) {
        this.client = client;
        this.frame = frame;
        this.table = table;

        setLayout(new GridBagLayout());

        buildButtons();

        add(Box.createGlue(), GBCUtils.buildGBC(0, 15, GridBagConstraints.BOTH, 0, 0, 0, 0, 1, 1));
    }

    private void buildButtons() {
        addButton("help", listeners.helpListener(), GBCUtils.buildGBC(0, 0, GridBagConstraints.HORIZONTAL, 0, 1, 0, 10, 1, 0));
        addButton("upd_table", listeners.updTableListener(), GBCUtils.buildGBC(0, 1, GridBagConstraints.HORIZONTAL, 0, 1, 0, 10, 1 ,0));
        addButton("add", listeners.addListener(), GBCUtils.buildGBC(0, 2, GridBagConstraints.HORIZONTAL, 0, 1, 0, 10, 1, 0));
//        addButton("add_if_max", listeners.addIfMaxListener(), GBCUtils.buildGBC(0, 3, GridBagConstraints.HORIZONTAL, 0, 1, 0, 10, 1, 0))
    }

    private void addButton(String key, ActionListener actionListener, GridBagConstraints gbc) {
        buttons.put(key, buildButton(key));
        buttons.get(key).addActionListener(actionListener);
        add(buttons.get(key), gbc);
    }

    private JButton buildButton(String key) {
        JButton button = new JButton(Languages.get(key));
        button.setPreferredSize(new Dimension(0, 40));
        return button;
    }

    private class Listeners {
        private ActionListener addListener() {
            return e -> {
                JDialog dialog = new JBuildMovieScreen(frame, client, table).buildMovieBuildScreen();
                dialog.setVisible(true);
            };
        }


        private ActionListener updTableListener() {
            return e -> {
                try {
                    JDialog dialog = DialogBuilder.buildDefaultJDialog("upd_table", 400, 300, frame);
                    dialog.setLayout(new BorderLayout());
                    JMovieTableModel model = (JMovieTableModel) table.getModel();
                    model.loadData();
                    table.repaint();
                    JLabel label = new JLabel("<html>" + "<div style='font-size:20pt;'>" + Languages.get("tableUpdated")+ "</div>" + "</html>");
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    label.setVerticalAlignment(SwingConstants.CENTER);
                    dialog.add(label, BorderLayout.CENTER);
                    dialog.setVisible(true);
                } catch (ServerIsUnavailableException ex) {
                    JDialog error = DialogBuilder.buildServerIsUnavailableDialog(frame);
                    error.setVisible(true);
                }
            };
        }

        private ActionListener helpListener() {
            return e -> {
                JDialog dialog = DialogBuilder.buildDefaultJDialog("help", 630, 260, frame);
                dialog.setLayout(new BorderLayout());

                Response response;
                try {
                    response = client.makeRequest("help", client.getLogin(), client.getPassword());
                } catch (IOException ex) {
                    System.out.println("Ошибка при запросе на сервер");
                    throw new RuntimeException(ex);
                } catch (ServerIsUnavailableException ex) {
                    JDialog error = DialogBuilder.buildServerIsUnavailableDialog(frame);
                    error.setVisible(true);
                    return;
                }

                String[] msg = Objects.requireNonNull(Languages.getTranslation(response.getTranslate())).split("\n");
                StringBuilder builder = new StringBuilder();
                for (String s : msg) {
                    String[] split = s.split(" : ");
                    builder.append(Languages.get(split[0])).append(" : ").append(split[1]).append("<br>");
                }
                JLabel label = new JLabel("<html>" + "<div style='font-size:20pt;'>" + Languages.get("helpMessage") + "</div>" + builder + "</html>");
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setVerticalAlignment(SwingConstants.CENTER);
                dialog.add(label, BorderLayout.CENTER);

                dialog.setVisible(true);
            };
        }
    }
}
