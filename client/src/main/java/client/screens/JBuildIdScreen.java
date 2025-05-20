package client.screens;

import client.utils.GBCUtils;
import client.utils.JUtils.DialogBuilder;
import client.utils.JUtils.JPanelDeb;
import client.utils.JUtils.JTextFieldPlaceholder;
import client.utils.Languages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class JBuildIdScreen extends JDialog {
    private final JFrame frame;

    private JTextFieldPlaceholder idField;

    private int result = -1;

    public JBuildIdScreen(JFrame frame) {
        this.frame = frame;
    }

    public void build() {
        setModal(true);
        setTitle(Languages.get("buildId"));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new GridBagLayout());
        setSize(new Dimension(300, 130));
        setLocationRelativeTo(frame);
        SwingUtilities.invokeLater(this::requestFocusInWindow);

        JPanelDeb buildIdPanel = new JPanelDeb(new GridBagLayout());
        buildIdPanel.add(buildLabelPanel(), GBCUtils.buildGBC(0, 0, GridBagConstraints.BOTH, 0, 0, 0, 0, 1, 1));
        buildIdPanel.add(buildFieldsPanel(), GBCUtils.buildGBC(1, 0, GridBagConstraints.BOTH, 0, 0, 0, 0, 1, 1));

        add(buildIdPanel, GBCUtils.buildGBC(0, 0, GridBagConstraints.BOTH, 0, 0, 0, 0, 1, 1));
        add(buildApplyBtnPanel(), GBCUtils.buildGBC(0, 1, GridBagConstraints.BOTH, 10, 0, 0, 0, 1, 1));


        setVisible(true);
    }

    private JPanel buildLabelPanel() {
        JPanel panel = new JPanel(new GridBagLayout());

        JLabel id = new JLabel("id");
        id.setMinimumSize(new Dimension(0, 40));
        id.setPreferredSize(new Dimension(0, 40));
        panel.add(id, GBCUtils.buildGBC(0, 0, GridBagConstraints.HORIZONTAL, 10, 10, 5, 0, 1, 0));

        return panel;
    }

    private JPanel buildFieldsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());

        idField = new JTextFieldPlaceholder();
        idField.setMinimumSize(new Dimension(0, 40));
        idField.setPreferredSize(new Dimension(0, 40));
        panel.add(idField, GBCUtils.buildGBC(0, 0, GridBagConstraints.HORIZONTAL, 10, 0, 0, 10, 1, 0));

        return panel;
    }

    private JPanel buildApplyBtnPanel() {
        JPanel panel = new JPanel(new GridBagLayout());

        JButton applyBtn = new JButton(Languages.get("send"));
        applyBtn.setMinimumSize(new Dimension(100, 40));
        applyBtn.setPreferredSize(new Dimension(100, 40));
        applyBtn.addActionListener(applyBtnActionListener());
        panel.add(applyBtn, GBCUtils.buildGBC(0, 0, GridBagConstraints.NONE, 0, 0, 10, 0, 0, 0));

        return panel;
    }

    private ActionListener applyBtnActionListener() {
        return e -> {
            int id;
            try {
                id = Integer.parseInt(idField.getText());
            } catch (NumberFormatException ex) {
                DialogBuilder.showValidationErrorDialog(Languages.get("wrongNumberFormat"), frame);
                return;
            }
            result = id;
            dispose();
        };
    }

    public int getResult() {
        return result;
    }
}
