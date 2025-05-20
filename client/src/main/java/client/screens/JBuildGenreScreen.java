package client.screens;

import client.utils.GBCUtils;
import client.utils.JUtils.JPanelDeb;
import client.utils.JUtils.JTextFieldPlaceholder;
import client.utils.Languages;
import server.object.MovieGenre;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class JBuildGenreScreen extends JDialog {
    private final JFrame frame;

    private MovieGenre result;

    private final JComboBox<MovieGenre> genreCombo = new JComboBox<>(MovieGenre.values());

    public JBuildGenreScreen(JFrame frame) {
        this.frame = frame;
    }

    public void build() {
        setModal(true);
        setTitle(Languages.get("buildGenre"));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new GridBagLayout());
        setSize(new Dimension(300, 140));
        setLocationRelativeTo(frame);
        SwingUtilities.invokeLater(this::requestFocusInWindow);

        JPanelDeb buildIdPanel = new JPanelDeb(new GridBagLayout());
        buildIdPanel.add(buildLabel(), GBCUtils.buildGBC(0, 0, GridBagConstraints.BOTH, 0, 10, 0, 0, 1, 1));
        buildIdPanel.add(buildComboBox(), GBCUtils.buildGBC(1, 0, GridBagConstraints.BOTH, 0, 0, 0, 10, 1, 1));

        add(buildIdPanel, GBCUtils.buildGBC(0, 0, GridBagConstraints.BOTH, 10, 0, 0, 0, 1, 1));
        add(buildApplyBtnPanel(), GBCUtils.buildGBC(0, 1, GridBagConstraints.BOTH, 10, 0, 10, 0, 1, 1));


        setVisible(true);
    }

    private JComboBox<MovieGenre> buildComboBox() {
        genreCombo.setPreferredSize(new Dimension(0, 40));
        genreCombo.setMinimumSize(new Dimension(0, 40));

        return genreCombo;
    }

    private JLabel buildLabel() {
        JLabel label = new JLabel(Languages.get("genre"));
        label.setPreferredSize(new Dimension(0, 40));
        label.setMinimumSize(new Dimension(0, 40));

        return label;
    }

    private JPanel buildApplyBtnPanel() {
        JPanel panel = new JPanel(new GridBagLayout());

        JButton button = new JButton(Languages.get("send"));
        button.setPreferredSize(new Dimension(100, 40));
        button.setMinimumSize(new Dimension(100, 40));
        button.addActionListener(e -> {
            result = buildGenre();
            dispose();
        });

        panel.add(button, GBCUtils.buildGBC(0, 0, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0));

        return panel;
    }

    private MovieGenre buildGenre() {
        return MovieGenre.valueOf(Objects.requireNonNull(genreCombo.getSelectedItem()).toString().trim());
    }

    public MovieGenre getResult() {
        return result;
    }
}
