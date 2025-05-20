package client.screens;

import client.utils.GBCUtils;
import client.utils.JUtils.DialogBuilder;
import client.utils.JUtils.JPanelDeb;
import client.utils.JUtils.JTextFieldPlaceholder;
import client.utils.Languages;
import server.object.*;
import server.utils.ValidationError;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JBuildOperatorScreen extends JDialog {
    private final JFrame frame;

    private Person result;

    private JCheckBox operatorCheckBox;
    private JTextFieldPlaceholder operatorName;
    private JTextFieldPlaceholder operatorBirthday;
    private JTextFieldPlaceholder operatorWeight;
    private JTextFieldPlaceholder operatorPassportID;

    public JBuildOperatorScreen(JFrame frame) {
        this.frame = frame;
    }

    public void build() {
        setModal(true);
        setTitle(Languages.get("buildOperator"));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new GridBagLayout());
        setSize(new Dimension(370, 315));

        setLocationRelativeTo(frame);
        SwingUtilities.invokeLater(this::requestFocusInWindow);

        JPanelDeb buildIdPanel = new JPanelDeb(new GridBagLayout());
        buildIdPanel.add(buildLabelPanel(), GBCUtils.buildGBC(0, 0, GridBagConstraints.BOTH, 0, 0, 0, 0, 1, 1));
        buildIdPanel.add(buildFieldsPanel(), GBCUtils.buildGBC(1, 0, GridBagConstraints.BOTH, 0, 0, 0, 0, 1, 1));

        add(buildIdPanel, GBCUtils.buildGBC(0, 0, GridBagConstraints.BOTH, 0, 0, 0, 0, 1, 1));
        add(buildApplyBtnPanel(), GBCUtils.buildGBC(0, 1, GridBagConstraints.BOTH, 10, 0, 10, 0, 1, 1));

        setVisible(true);
    }

    private JPanel buildLabelPanel() {
        JPanel panel = new JPanel(new GridBagLayout());

        addLabel(panel, "operator", 0);
        addLabel(panel, "operatorName", 1);
        addLabel(panel, "operatorBirthday", 2);
        addLabel(panel, "operatorWeight", 3);
        addLabel(panel, "operatorPassportID", 4);

        return panel;
    }

    private void addLabel(JPanel panel, String key, int posy) {
        JLabel name = new JLabel(Languages.get(key));
        name.setPreferredSize(new Dimension(0, 40));
        name.setMinimumSize(new Dimension(0, 40));

        GridBagConstraints gbc = GBCUtils.buildGBC(0, posy, GridBagConstraints.HORIZONTAL, 0, 0, 0, 0, 1, 0);
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        panel.add(name, gbc);
    }

    private JPanel buildFieldsPanel() {
        JPanel panel = new JPanelDeb(new GridBagLayout());

        JPanel checkPanel = new JPanel(new GridBagLayout());
        checkPanel.setPreferredSize(new Dimension(0, 30));
        checkPanel.setMinimumSize(new Dimension(0, 30));
        operatorCheckBox = new JCheckBox();
        operatorCheckBox.doClick();
        checkPanel.add(operatorCheckBox);

        panel.add(checkPanel, GBCUtils.buildGBC(0, 0, GridBagConstraints.HORIZONTAL, 10, 0, 0, 0, 1, 0));

        operatorName = buildField("Name");
        panel.add(operatorName, GBCUtils.buildGBC(0, 1, GridBagConstraints.HORIZONTAL, 10, 0, 0, 10, 1, 0));
        operatorBirthday = buildField("12/12/2004");
        panel.add(operatorBirthday, GBCUtils.buildGBC(0, 2, GridBagConstraints.HORIZONTAL, 0, 0, 0, 10, 1, 0));
        operatorWeight = buildField("1");
        panel.add(operatorWeight, GBCUtils.buildGBC(0, 3, GridBagConstraints.HORIZONTAL, 0, 0, 0, 10, 1, 0));
        operatorPassportID = buildField("123");
        panel.add(operatorPassportID, GBCUtils.buildGBC(0, 4, GridBagConstraints.HORIZONTAL, 0, 0, 0, 10, 1, 0));

        return panel;
    }

    public JPanel buildApplyBtnPanel() {
        JPanel panel = new JPanelDeb(new GridBagLayout());

        JButton button = new JButton(Languages.get("send"));
        button.setPreferredSize(new Dimension(100, 40));
        button.setMinimumSize(new Dimension(100, 40));

        button.addActionListener(e -> {
            Person person = buildPerson();
            if (person != null || !operatorCheckBox.isSelected()) {
                result = person;
                dispose();
            }
        });

        panel.add(button, GBCUtils.buildGBC(0, 0, GridBagConstraints.HORIZONTAL, 0, 0, 0, 0, 0, 0));

        return panel;
    }

    private JTextFieldPlaceholder buildField(String placeholder) {
        JTextFieldPlaceholder field = new JTextFieldPlaceholder();
        field.setPlaceholderKey(placeholder);
        field.setPreferredSize(new Dimension(0, 40));
        field.setPreferredSize(new Dimension(0, 40));

        return field;
    }

    private void clearFieldError(JTextFieldPlaceholder field) {
        field.setBackground(Color.WHITE);
    }

    private void highlightFieldError(JTextFieldPlaceholder field) {
        field.setBackground(Color.PINK);
    }

    private Person buildPerson() {
        clearFieldError(operatorName);
        clearFieldError(operatorBirthday);
        clearFieldError(operatorWeight);
        clearFieldError(operatorPassportID);
        if (!operatorCheckBox.isSelected()) {
            return null;
        }
        Person operator = new Person("Name", new Date(), Long.parseLong("1"), "123");
        try {
            operator.setName(operatorName.getText().trim());
            operator.setBirthday(operatorBirthday.getText() == null || operatorBirthday.getText().isEmpty() ? null : new SimpleDateFormat("dd/MM/yyyy").parse((operatorBirthday.getText()).trim()));
            operator.setWeight(Long.parseLong(operatorWeight.getText().trim()));
            operator.setPassportID(operatorPassportID.getText().trim());
        } catch (NumberFormatException ex) {
            highlightFieldError(operatorWeight);
            DialogBuilder.showValidationErrorDialog(Languages.get("wrongNumberFormat"), frame);
            return null;
        } catch (ParseException ex) {
            highlightFieldError(operatorBirthday);
            DialogBuilder.showValidationErrorDialog(Languages.get("wrongDateFormat"), frame);
            return null;
        } catch (ValidationError ex) {
            switch (ex.getField()) {
                case "name" -> highlightFieldError(operatorName);
                case "operatorName" -> highlightFieldError(operatorName);
                case "passportID" -> highlightFieldError(operatorPassportID);
            }
            DialogBuilder.showValidationErrorDialog(ex.getMessage(), frame);
            return null;
        }
        return operator;
    }

    public Person getResult() {
        return result;
    }
}
