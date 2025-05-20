package client.utils.JUtils;

import client.utils.GBCUtils;
import client.utils.Languages;
import server.object.MovieGenre;
import server.object.MpaaRating;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class JBuildMovieScreen extends JDialog {
    private final JFrame frame;

    public JBuildMovieScreen(JFrame frame) {
        this.frame = frame;
    }

    public JDialog buildMovieBuildScreen() {
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

}
