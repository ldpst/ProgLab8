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
//                try {
                    JDialog dialog = buildMovieBuildScreen();
                    dialog.setVisible(true);

//                    Response response = client.makeRequest("add", new Movie("name", new Coordinates((float) 0, 0), Long.parseLong("1"), MovieGenre.DRAMA, MpaaRating.G, new Person("name", new Date(), Long.parseLong("1"), "123"), client.getLogin()), client.getLogin(), client.getPassword());
//                    JMovieTableModel model = (JMovieTableModel) table.getModel();
//                    model.loadData();
//                    table.repaint();
//                } catch (ServerIsUnavailableException | IOException ex) {
//                    JDialog error = buildServerIsUnavailableDialog();
//                    error.setVisible(true);
//                }
            };
        }

        private JDialog buildMovieBuildScreen() {
            JDialog dialog = new JDialog();
            dialog.setModal(true);
            dialog.setTitle(Languages.get("buildMovie"));
            dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            dialog.getContentPane().setLayout(new GridBagLayout());
            dialog.setSize(new Dimension(300, 530));
            dialog.setLocationRelativeTo(frame);
            SwingUtilities.invokeLater(dialog::requestFocusInWindow);

            JPanelDeb buildMoviePanel = new JPanelDeb(new GridBagLayout());
            buildMoviePanel.add(buildMovieLabelPanel(), GBCUtils.buildGBC(0, 0, GridBagConstraints.BOTH, 0, 0, 0, 0, 1, 1));
            buildMoviePanel.add(buildMovieFieldsPanel(), GBCUtils.buildGBC(1, 0, GridBagConstraints.BOTH, 0, 0, 0, 0, 1, 1));

            dialog.add(buildMoviePanel, GBCUtils.buildGBC(0, 0, GridBagConstraints.BOTH, 0, 0, 0, 0, 1, 1));
            dialog.add(buildApplyBtnPanel(), GBCUtils.buildGBC(0, 1, GridBagConstraints.BOTH, 0, 0, 0, 0, 1, 0));

            return dialog;
        }

        private JPanel buildMovieLabelPanel() {
            JPanelDeb panel = new JPanelDeb();
            panel.setLayout(new GridBagLayout());
            panel.setBorder(new LineBorder(Color.RED, 2));

            addMovieBuildLabel(panel, "name", 0);
            addMovieBuildLabel(panel, "coordinateX", 1);
            addMovieBuildLabel(panel, "coordinateY", 2);
            addMovieBuildLabel(panel, "oscarsCount", 3);
            addMovieBuildLabel(panel, "genre", 4);
            addMovieBuildLabel(panel, "rating", 5);
            addMovieBuildLabel(panel, "operator", 6);
            addMovieBuildLabel(panel, "operatorName", 7);
            addMovieBuildLabel(panel, "operatorBirthday", 8);
            addMovieBuildLabel(panel, "operatorWeight", 9);
            addMovieBuildLabel(panel, "operatorPassportID", 10);

//            JPanel spacer = new JPanel();
//            spacer.setOpaque(false);
//            panel.add(spacer, GBCUtils.buildGBC(0, 11, GridBagConstraints.BOTH, 0, 0, 0, 0, 1, 1));

            return panel;
        }

        private JPanelDeb buildApplyBtnPanel() {
            JPanelDeb panel = new JPanelDeb();
            panel.setLayout(new GridBagLayout());
            panel.setBorder(new LineBorder(Color.RED, 2));

            JButton button = new JButton(Languages.get("send"));
            button.setPreferredSize(new Dimension(100, 40));

            panel.add(button, GBCUtils.buildGBC(0, 0, GridBagConstraints.NONE, 0, 0, 0, 0, 0, 0));
            return panel;
        }

        private void addMovieBuildLabel(JPanel panel, String key, int posy) {
            JLabel name = new JLabel(Languages.get(key));
            name.setPreferredSize(new Dimension(0, 40));
            name.setMinimumSize(new Dimension(0, 40));

            GridBagConstraints gbc = GBCUtils.buildGBC(0, posy, GridBagConstraints.HORIZONTAL, 0, 0, 0, 0, 1, 0);
            gbc.anchor = GridBagConstraints.FIRST_LINE_START;
            panel.add(name, gbc);
        }

        private JPanel buildMovieFieldsPanel() {
            JPanelDeb panel = new JPanelDeb();
            panel.setLayout(new GridBagLayout());
            panel.setBorder(new LineBorder(Color.RED, 2));

            JTextFieldPlaceholder name = buildMovieField("Name");
            addMovieField(panel, name, 0);
            JTextFieldPlaceholder coordinateX = buildMovieField("0.0");
            addMovieField(panel, coordinateX, 1);
            JTextFieldPlaceholder coordinateY = buildMovieField("0");
            addMovieField(panel, coordinateY, 2);
            JTextFieldPlaceholder oscarsCount = buildMovieField("1");
            addMovieField(panel, oscarsCount, 3);
            JComboBox<MovieGenre> genreComboBox = new JComboBox<>(MovieGenre.values());
            genreComboBox.setPreferredSize(new Dimension(0, 40));
            genreComboBox.setMinimumSize(new Dimension(0, 40));
            addMovieField(panel, genreComboBox, 4);
            JComboBox<MpaaRating> ratingComboBox = new JComboBox<>(MpaaRating.values());
            ratingComboBox.setPreferredSize(new Dimension(0, 40));
            ratingComboBox.setMinimumSize(new Dimension(0, 40));
            addMovieField(panel, ratingComboBox, 5);

            JPanel checkPanel = new JPanel();
            checkPanel.setLayout(new GridBagLayout());
            checkPanel.setPreferredSize(new Dimension(0, 40));

            JCheckBox operator = new JCheckBox();
            addMovieField(checkPanel, operator, 0);

            addMovieField(panel, checkPanel, 6);


            JTextFieldPlaceholder operatorName = buildMovieField("Name");
            addMovieField(panel, operatorName, 7);
            JTextFieldPlaceholder operatorBirthday = buildMovieField("12/12/2004");
            addMovieField(panel, operatorBirthday, 8);
            JTextFieldPlaceholder operatorWeight = buildMovieField("1");
            addMovieField(panel, operatorWeight, 9);
            JTextFieldPlaceholder operatorPassportID = buildMovieField("123");
            addMovieField(panel, operatorPassportID, 10);



//            JPanel spacer = new JPanel();
//            spacer.setOpaque(false);
//            panel.add(spacer, GBCUtils.buildGBC(0, 11, GridBagConstraints.BOTH, 0, 0, 0, 0, 1, 1));
            return panel;
        }

        private JTextFieldPlaceholder buildMovieField(String placeholder) {
            JTextFieldPlaceholder field = new JTextFieldPlaceholder();
            field.setPlaceholderKey(placeholder);
            field.setPreferredSize(new Dimension(0, 40));
            field.setMinimumSize(new Dimension(0, 40));

            return field;
        }

        private void addMovieField(JPanel panel, JComponent component, int posy) {
            GridBagConstraints gbc = GBCUtils.buildGBC(0, posy, GridBagConstraints.HORIZONTAL, 0, 0, 0, 0, 1, 0);
            gbc.anchor = GridBagConstraints.FIRST_LINE_START;
            panel.add(component, gbc);
        }

        private ActionListener updTableListener() {
            return e -> {
                try {
                    JDialog dialog = buildDefaultJDialog("upd_table", 400, 300);
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
                    JDialog error = buildServerIsUnavailableDialog();
                    error.setVisible(true);
                }
            };
        }

        private ActionListener helpListener() {
            return e -> {
                JDialog dialog = buildDefaultJDialog("help", 630, 260);
                dialog.setLayout(new BorderLayout());

                Response response;
                try {
                    response = client.makeRequest("help", client.getLogin(), client.getPassword());
                } catch (IOException ex) {
                    System.out.println("Ошибка при запросе на сервер");
                    throw new RuntimeException(ex);
                } catch (ServerIsUnavailableException ex) {
                    JDialog error = buildServerIsUnavailableDialog();
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

        private JDialog buildDefaultJDialog(String key, int x, int y) {
            JDialog dialog = new JDialog();
            dialog.setLayout(new GridBagLayout());
            dialog.setTitle(Languages.get(key));
            dialog.setModal(true);
            dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            dialog.setSize(new Dimension(x, y));
            dialog.setLocationRelativeTo(frame);

            return dialog;
        }

        private JDialog buildServerIsUnavailableDialog() {
            JDialog dialog = buildDefaultJDialog(Languages.get("error"), 400, 300);
            dialog.setLayout(new BorderLayout());
            JLabel label = new JLabel("<html><div style='font-size:20pt;'>" + Languages.get("serverIsUnavailable") + "</div></html>");
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setVerticalAlignment(SwingConstants.CENTER);
            dialog.add(label, BorderLayout.CENTER);
            return dialog;
        }
    }
}
