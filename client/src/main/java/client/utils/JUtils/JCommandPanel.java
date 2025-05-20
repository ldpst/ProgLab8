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
import java.util.*;

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
        addButton("add_if_max", listeners.addIfMaxListener(), GBCUtils.buildGBC(0, 3, GridBagConstraints.HORIZONTAL, 0, 1, 0, 10, 1, 0));
        addButton("clear", listeners.clearListener(), GBCUtils.buildGBC(0, 4, GridBagConstraints.HORIZONTAL, 0, 1, 0, 10, 1, 0));
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
        private ActionListener clearListener() {
            return e -> {
                sendObject("clear", null);
            };
        }

        private ActionListener addListener() {
            return e -> {
                JBuildMovieScreen buildMovieScreen = new JBuildMovieScreen(frame, client);
                buildMovieScreen.buildMovieBuildScreen();
                Movie movie = buildMovieScreen.getResult();

                sendObject("add", movie);
            };
        }

        private ActionListener addIfMaxListener() {
            return e -> {
                JBuildMovieScreen buildMovieScreen = new JBuildMovieScreen(frame, client);
                buildMovieScreen.buildMovieBuildScreen();
                Movie movie = buildMovieScreen.getResult();

                sendObject("add_if_max", movie);
            };
        }

        private void sendObject(String command, Object object) {
            try {
                Response response = client.makeRequest(command, object, client.getLogin(), client.getPassword());

                updateTable();

                DialogBuilder.showSuccessDialog(Languages.getTranslation(response.getTranslate()), frame);
            } catch (ServerIsUnavailableException | IOException ex) {
                DialogBuilder.showServerIsUnavailableDialog(frame);
            }
        }

        private ActionListener updTableListener() {
            return e -> {
                try {
                    updateTable();
                    DialogBuilder.showSuccessDialog(Languages.get("tableUpdated"), frame);
                } catch (ServerIsUnavailableException ex) {
                    DialogBuilder.showServerIsUnavailableDialog(frame);
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
                    DialogBuilder.showServerIsUnavailableDialog(frame);
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

        private void updateTable() {
            JMovieTableModel model = (JMovieTableModel) table.getModel();
            model.loadData();
            table.repaint();
        }
    }
}
