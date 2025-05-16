package client.utils.JUtils;

import server.object.Movie;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Vector;

public class JMovieTable extends JTable {
    public JMovieTable() {
        autoResizeColumnWidth();
    }

    public JMovieTable(TableModel dm) {
        super(dm);
        autoResizeColumnWidth();
    }

    public JMovieTable(TableModel dm, TableColumnModel cm) {
        super(dm, cm, null);
        autoResizeColumnWidth();
    }

    public JMovieTable(TableModel dm, TableColumnModel cm, ListSelectionModel sm) {
        super(dm, cm, sm);
        autoResizeColumnWidth();
    }

    public JMovieTable(int numRows, int numColumns) {
        super(numRows, numColumns);
        autoResizeColumnWidth();
    }

    public JMovieTable(Vector<? extends Vector> rowData, Vector<?> columnNames) {
        this(new DefaultTableModel(rowData, columnNames));
        autoResizeColumnWidth();
    }

    public JMovieTable(final Object[][] rowData, final Object[] columnNames) {
        super(rowData, columnNames);
        autoResizeColumnWidth();
    }

    public void autoResizeColumnWidth() {
        JTableHeader header = getTableHeader();
        FontMetrics headerFontMetrics = header.getFontMetrics(header.getFont());

        for (int i = 0; i < getColumnCount(); i++) {
            int preferredWidth = headerFontMetrics.stringWidth(getColumnName(i));
            for (int j = 0; j < getRowCount(); j++) {
                Object value = getValueAt(j, i);
                Component comp = prepareRenderer(getCellRenderer(j, i), j, i);
                preferredWidth = Math.max(preferredWidth, comp.getPreferredSize().width);
            }
            preferredWidth += 10;
            getColumnModel().getColumn(i).setPreferredWidth(preferredWidth);
        }
    }

    public ArrayList<Movie> getData() {
        JMovieTableModel model = (JMovieTableModel) getModel();
        return model.getData();
    }
}
