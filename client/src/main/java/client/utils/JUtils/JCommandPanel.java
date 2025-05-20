package client.utils.JUtils;

import client.client.UDPClient;
import client.exceptions.ServerIsUnavailableException;
import client.screens.JBuildIdScreen;
import client.screens.JBuildMovieScreen;
import client.screens.JBuildOperatorScreen;
import client.utils.GBCUtils;
import client.utils.Languages;
import server.object.Movie;
import server.object.Person;
import server.requests.Request;
import server.response.Response;
import server.response.ResponseType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
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
        addButton("upd_table", listeners.updTableListener(), GBCUtils.buildGBC(0, 1, GridBagConstraints.HORIZONTAL, 0, 1, 0, 10, 1, 0));
        addButton("add", listeners.addListener(), GBCUtils.buildGBC(0, 2, GridBagConstraints.HORIZONTAL, 0, 1, 0, 10, 1, 0));
        addButton("add_if_max", listeners.addIfMaxListener(), GBCUtils.buildGBC(0, 3, GridBagConstraints.HORIZONTAL, 0, 1, 0, 10, 1, 0));
        addButton("update", listeners.updateListener(), GBCUtils.buildGBC(0, 4, GridBagConstraints.HORIZONTAL, 0, 1, 0, 10, 1, 0));
        addButton("clear", listeners.clearListener(), GBCUtils.buildGBC(0, 5, GridBagConstraints.HORIZONTAL, 0, 1, 0, 10, 1, 0));
        addButton("remove_by_id", listeners.removeByIdListener(), GBCUtils.buildGBC(0, 6, GridBagConstraints.HORIZONTAL, 0, 1, 0, 10, 1, 0));
        addButton("remove_greater", listeners.removeGreaterListener(), GBCUtils.buildGBC(0, 7, GridBagConstraints.HORIZONTAL, 0, 1, 0, 10, 1, 0));
        addButton("head", listeners.headListener(), GBCUtils.buildGBC(0, 8, GridBagConstraints.HORIZONTAL, 0, 1, 0, 10, 1, 0));
        addButton("count_by_operator", listeners.countByOperatorListener(), GBCUtils.buildGBC(0, 9, GridBagConstraints.HORIZONTAL, 0, 1, 0, 10, 1, 0));
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
        private ActionListener countByOperatorListener() {
            return e -> {
                JBuildOperatorScreen buildOperatorScreen = new JBuildOperatorScreen(frame);
                buildOperatorScreen.build();
                Person person = buildOperatorScreen.getResult();
                sendObject("count_by_operator", person);
            };
        }

        private ActionListener headListener() {
            return e -> {
                Movie movie = table.getData().getFirst();
                JDialog dialog = new JDialog();
                dialog.setTitle(Languages.get("head"));
                dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                
                JLabel id = new JLabel("id" + ": " + movie.getId());
                JLabel name = new JLabel(Languages.get("name") + ": " + movie.getName());
                JLabel x = new JLabel(Languages.get("coordinateX") + ": " + movie.getCoordinates().getX());
                JLabel y = new JLabel(Languages.get("coordinateY") + ": " + movie.getCoordinates().getY());
                JLabel creationDate = new JLabel(Languages.get("creationDate") + ": " + movie.getCreationDate().withZoneSameInstant(Objects.requireNonNull(Languages.getCurrentLocale()).second).format(DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy HH:mm z", Objects.requireNonNull(Languages.getCurrentLocale()).first)));
                JLabel oscarsCount = new JLabel(Languages.get("oscarsCount") + ": " + movie.getOscarsCount());
                JLabel genre = new JLabel(Languages.get("genre") + ": " + movie.getGenre());
                JLabel rating = new JLabel(Languages.get("rating") + ": " + movie.getMpaaRating());
                panel.add(id);
                panel.add(name);
                panel.add(x);
                panel.add(y);
                panel.add(creationDate);
                panel.add(oscarsCount);
                panel.add(genre);
                panel.add(rating);
                if (movie.getOperator() != null) {
                    JLabel operatorName = new JLabel(Languages.get("operatorName") + ": " + movie.getOperator().getName());
                    JLabel operatorBirthday = new JLabel(Languages.get("operatorBirthday") + ": " + new SimpleDateFormat("EEEE, d MMMM, yyyy", Objects.requireNonNull(Languages.getCurrentLocale()).first).format(movie.getOperator().getBirthday()));
                    JLabel operatorWeight = new JLabel(Languages.get("operatorWeight") + ": " + movie.getOperator().getWeight());
                    JLabel operatorPassportID = new JLabel(Languages.get("operatorPassportID") + ": " + movie.getOperator().getPassportID());
                    panel.add(operatorName);
                    panel.add(operatorBirthday);
                    panel.add(operatorWeight);
                    panel.add(operatorPassportID);
                }

                dialog.add(panel);
                dialog.pack();
                dialog.setLocationRelativeTo(frame);
                dialog.setModal(true);

                dialog.setVisible(true);
            };
        }

        private ActionListener removeGreaterListener() {
            return e -> {
                JBuildMovieScreen buildMovieScreen = new JBuildMovieScreen(frame, client);
                buildMovieScreen.build();
                Movie movie = buildMovieScreen.getResult();
                if (movie == null) return;

                sendObject("remove_greater", movie);
            };
        }

        private ActionListener removeByIdListener() {
            return e -> {
                JBuildIdScreen buildIdScreen = new JBuildIdScreen(frame);
                buildIdScreen.build();
                int id = buildIdScreen.getResult();
                if (id == -1) return;
                sendObject("remove_by_id " + id, null);
            };
        }

        private ActionListener updateListener() {
            return e -> {
                JBuildIdScreen buildIdScreen = new JBuildIdScreen(frame);
                buildIdScreen.build();
                int id = buildIdScreen.getResult();
                if (id == -1) return;

                JBuildMovieScreen buildMovieScreen = new JBuildMovieScreen(frame, client);
                buildMovieScreen.build();
                Movie movie = buildMovieScreen.getResult();
                if (movie == null) return;

                sendObject("update " + id, movie);
            };
        }

        private ActionListener clearListener() {
            return e -> {
                sendObject("clear", null);
            };
        }

        private ActionListener addListener() {
            return e -> {
                JBuildMovieScreen buildMovieScreen = new JBuildMovieScreen(frame, client);
                buildMovieScreen.build();
                Movie movie = buildMovieScreen.getResult();
                if (movie == null) return;

                sendObject("add", movie);
            };
        }

        private ActionListener addIfMaxListener() {
            return e -> {
                JBuildMovieScreen buildMovieScreen = new JBuildMovieScreen(frame, client);
                buildMovieScreen.build();
                Movie movie = buildMovieScreen.getResult();
                if (movie == null) return;

                sendObject("add_if_max", movie);
            };
        }

        private void sendObject(String command, Object object) {
            try {
                Response response = client.makeRequest(command, object, client.getLogin(), client.getPassword());

                updateTable();

                String msg = Languages.getTranslation(response.getTranslate());
                if (response.getType() == ResponseType.ERROR) {
                    DialogBuilder.showErrorDialog(msg, frame);
                } else {
                    DialogBuilder.showSuccessDialog(msg, frame);
                }
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
