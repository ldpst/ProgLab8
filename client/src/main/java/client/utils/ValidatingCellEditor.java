package client.utils;

import server.utils.ValidationError;

import javax.swing.*;
import java.awt.*;

public class ValidatingCellEditor extends DefaultCellEditor {
    private final JMovieTableModel model;
    private final JMovieTable table;

    public ValidatingCellEditor(JTextField jTextField, JMovieTable table) {
        super(jTextField);
        this.table = table;
        this.model = (JMovieTableModel) table.getModel();
    }

    @Override
    public boolean stopCellEditing() {
        JTextField textField = (JTextField) getComponent();

        int row = table.getEditingRow();
        int column = table.getEditingColumn();

        try {
            model.setValueAt(textField.getText(), row, column);

            return super.stopCellEditing();
        } catch (ValidationError e) {
            editorComponent.setBackground(Color.PINK);
            table.repaint();

            JOptionPane.showMessageDialog(
                    null,
                    Languages.get("validationError") + " " + e.getMessage(),
                    Languages.get("error"),
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
    }

    @Override
    public Object getCellEditorValue() {
        JTextField editorComponent = (JTextField) getComponent();
        editorComponent.setBackground(Color.WHITE);
        return editorComponent.getText();
    }
}
