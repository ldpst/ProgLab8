package client.utils.JUtils;

import client.client.UDPClient;
import client.utils.GBCUtils;
import client.utils.Languages;
import server.object.*;
import server.utils.ValidationError;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class JBuildMovieScreen extends JDialog {
    private final JFrame frame;
    private final UDPClient client;

    private Movie result;

    private JTextFieldPlaceholder name;
    private JTextFieldPlaceholder coordinateX;
    private JTextFieldPlaceholder coordinateY;
    private JTextFieldPlaceholder oscarsCount;
    private JComboBox<MovieGenre> genreComboBox;
    private JComboBox<MpaaRating> ratingComboBox;
    private JCheckBox operator;
    private JTextFieldPlaceholder operatorName;
    private JTextFieldPlaceholder operatorBirthday;
    private JTextFieldPlaceholder operatorWeight;
    private JTextFieldPlaceholder operatorPassportID;


    public JBuildMovieScreen(JFrame frame, UDPClient client) {
        this.frame = frame;
        this.client = client;
    }

    public void build() {
        setModal(true);
        setTitle(Languages.get("buildMovie"));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new GridBagLayout());
        setSize(new Dimension(300, 530));
        setLocationRelativeTo(frame);
        SwingUtilities.invokeLater(this::requestFocusInWindow);

        JPanelDeb buildMoviePanel = new JPanelDeb(new GridBagLayout());
        buildMoviePanel.add(buildMovieLabelPanel(), GBCUtils.buildGBC(0, 0, GridBagConstraints.BOTH, 0, 0, 0, 0, 1, 1));
        buildMoviePanel.add(buildMovieFieldsPanel(), GBCUtils.buildGBC(1, 0, GridBagConstraints.BOTH, 0, 0, 0, 0, 1, 1));

        add(buildMoviePanel, GBCUtils.buildGBC(0, 0, GridBagConstraints.BOTH, 0, 0, 0, 0, 1, 1));
        add(buildApplyBtnPanel(), GBCUtils.buildGBC(0, 1, GridBagConstraints.BOTH, 0, 0, 0, 0, 1, 0));

        setVisible(true);
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

        return panel;
    }

    private JPanelDeb buildApplyBtnPanel() {
        JPanelDeb panel = new JPanelDeb();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(new LineBorder(Color.RED, 2));

        JButton button = new JButton(Languages.get("send"));
        button.setPreferredSize(new Dimension(100, 40));
        button.addActionListener(buildApplyActionListener());

        panel.add(button, GBCUtils.buildGBC(0, 0, GridBagConstraints.NONE, 0, 0, 0, 0, 0, 0));
        return panel;
    }

    private ActionListener buildApplyActionListener() {
        return e -> {
            Movie movie = buildMovie();
            if (movie != null) {
                result = movie;
                dispose();
            }
        };
    }

    public Movie getResult() {
        return result;
    }

    private Movie buildMovie() {
        clearFieldError(name);
        clearFieldError(coordinateX);
        clearFieldError(coordinateY);
        clearFieldError(oscarsCount);
        clearFieldError(operatorName);
        clearFieldError(operatorBirthday);
        clearFieldError(operatorWeight);
        clearFieldError(operatorPassportID);
        Movie movie = new Movie("Name", new Coordinates((float) 0, 0), Long.parseLong("1"), MovieGenre.DRAMA, MpaaRating.G, new Person("Name", new Date(), Long.parseLong("1"), "123"), client.getLogin());
        try {
            movie.setName(name.getText().trim());
            movie.setCoordinateX(Float.parseFloat(coordinateX.getText().trim()));
            movie.setCoordinateY(Integer.parseInt(coordinateY.getText().trim()));
            movie.setOscarsCount(Long.parseLong(oscarsCount.getText().trim()));
            movie.setGenre(MovieGenre.valueOf(Objects.requireNonNull(genreComboBox.getSelectedItem()).toString().trim()));
            movie.setMpaaRating(MpaaRating.valueOf(Objects.requireNonNull(ratingComboBox.getSelectedItem()).toString().trim()));
            if (operator.isSelected()) {
                movie.setOperatorsName(operatorName.getText().trim());
                movie.setOperatorsBirthday(operatorBirthday.getText() == null || operatorBirthday.getText().isEmpty() ? null : new SimpleDateFormat("dd/MM/yyyy").parse((operatorBirthday.getText()).trim()));
                movie.setOperatorsWeight(Long.parseLong(operatorWeight.getText().trim()));
                movie.setOperatorsPassportID(operatorPassportID.getText().trim());
            }
            else {
                movie.setOperator(null);
            }
        } catch (NumberFormatException ex) {
            if (!isFloat(coordinateX.getText().trim())) highlightFieldError(coordinateX);
            if (!isInteger(coordinateY.getText().trim())) highlightFieldError(coordinateY);
            if (!isLong(oscarsCount.getText().trim())) highlightFieldError(oscarsCount);
            if (operator.isSelected()) {
                if (!isLong(operatorWeight.getText().trim())) highlightFieldError(operatorWeight);
            }

            DialogBuilder.showValidationErrorDialog(Languages.get("wrongNumberFormat"), frame);
            return null;
        } catch (ParseException ex) {
            highlightFieldError(operatorBirthday);

            DialogBuilder.showValidationErrorDialog(Languages.get("wrongDateFormat"), frame);
            return null;
        } catch (ValidationError ex) {
            switch (ex.getField()) {
                case "name" -> highlightFieldError(name);
                case "oscarsCount" -> highlightFieldError(oscarsCount);
                case "operatorName" -> highlightFieldError(operatorName);
                case "passportID" -> highlightFieldError(operatorPassportID);
            }

            DialogBuilder.showValidationErrorDialog(ex.getMessage(), frame);
            return null;
        }
        return movie;
    }

    private void highlightFieldError(JTextFieldPlaceholder field) {
        field.setBackground(Color.PINK);
    }

    private void clearFieldError(JTextFieldPlaceholder field) {
        field.setBackground(Color.WHITE);
    }

    private boolean isFloat(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private boolean isLong(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
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

        name = buildMovieField("Name");
        addComponent(panel, name, 0);
        coordinateX = buildMovieField("0.0");
        addComponent(panel, coordinateX, 1);
        coordinateY = buildMovieField("0");
        addComponent(panel, coordinateY, 2);
        oscarsCount = buildMovieField("1");
        addComponent(panel, oscarsCount, 3);
        genreComboBox = new JComboBox<>(MovieGenre.values());
        genreComboBox.setPreferredSize(new Dimension(0, 40));
        genreComboBox.setMinimumSize(new Dimension(0, 40));
        addComponent(panel, genreComboBox, 4);
        ratingComboBox = new JComboBox<>(MpaaRating.values());
        ratingComboBox.setPreferredSize(new Dimension(0, 40));
        ratingComboBox.setMinimumSize(new Dimension(0, 40));
        addComponent(panel, ratingComboBox, 5);

        JPanel checkPanel = new JPanel();
        checkPanel.setLayout(new GridBagLayout());
        checkPanel.setPreferredSize(new Dimension(0, 40));

        operator = new JCheckBox();
        addComponent(checkPanel, operator, 0);

        addComponent(panel, checkPanel, 6);


        operatorName = buildMovieField("Name");
        addComponent(panel, operatorName, 7);
        operatorBirthday = buildMovieField("12/12/2004");
        addComponent(panel, operatorBirthday, 8);
        operatorWeight = buildMovieField("1");
        addComponent(panel, operatorWeight, 9);
        operatorPassportID = buildMovieField("123");
        addComponent(panel, operatorPassportID, 10);

        return panel;
    }

    private JTextFieldPlaceholder buildMovieField(String placeholder) {
        JTextFieldPlaceholder field = new JTextFieldPlaceholder();
        field.setPlaceholderKey(placeholder);
        field.setPreferredSize(new Dimension(0, 40));
        field.setMinimumSize(new Dimension(0, 40));

        return field;
    }

    private void addComponent(JPanel panel, JComponent component, int posy) {
        GridBagConstraints gbc = GBCUtils.buildGBC(0, posy, GridBagConstraints.HORIZONTAL, 0, 0, 0, 0, 1, 0);
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        panel.add(component, gbc);
    }

}
