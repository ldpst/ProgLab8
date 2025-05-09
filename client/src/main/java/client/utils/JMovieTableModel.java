package client.utils;

import server.object.MovieGenre;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Date;

public class JMovieTableModel extends AbstractTableModel {
    private final String[] columns = {"id", "name", "coordinates.x", "coordinates.y", "creation date", "oscars count", "genre", "is operator exits?", "operator.name", "operator.birthday", "operator.weight", "operator.passportID", "owner"};
    private Object[][] data = new Object[3][columns.length];

    private int sortedBy = 0;
    private boolean reversed = false;

    public JMovieTableModel() {
        data[1] = new Object[]{1, "1", 1.1, 2, new Date(), 2, MovieGenre.HORROR, false, null, null, null, null, null};
        data[0] = new Object[]{2, "2", 2.2, 1, new Date(), 1, MovieGenre.HORROR, false, null, null, null, null, null};
        setSortedByColumn(0);
        sortDataByColumn(sortedBy);
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= data.length || columnIndex < 0 || columnIndex >= columns.length) {
            return null;
        }
        return data[rowIndex][columnIndex];
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        data[rowIndex][columnIndex] = aValue;
    }

    public void sortDataByColumn(int column) {
        data = Arrays.stream(data)
                .sorted((o1, o2) -> {
                    if (o1[column] instanceof Comparable<?> && o2[column] instanceof Comparable<?>) {
                        Comparable<Object> c1 = (Comparable<Object>) o1[column];
                        Comparable<Object> c2 = (Comparable<Object>) o2[column];
                        if (reversed) {
                            return c2.compareTo(c1);
                        }
                        return c1.compareTo(c2);
                    }
                    return 0;
                })
                .toArray(Object[][]::new);
    }

    private void setSortedByColumn(int column) {
        if (sortedBy != -1 && (columns[sortedBy].endsWith("▼") || columns[sortedBy].endsWith("▲"))) {
            columns[sortedBy] = columns[sortedBy].substring(0, columns[sortedBy].length() - 2);
        }
        sortedBy = column;
        if (reversed) {
            columns[sortedBy] += " ▲";
        }
        else {
            columns[sortedBy] += " ▼";
        }
        fireTableStructureChanged();
    }

    private int getSortedByColumn() {
        return sortedBy;
    }

    private boolean getReversed() {
        return reversed;
    }

    private void setReversed(boolean reversed) {
        this.reversed = reversed;
    }

    private String[] getColumns() {
        return columns;
    }

    static public class JMovieTableHeaderMouseReader extends MouseAdapter {
        private final JTable table;
        private final JMovieTableModel model;

        public JMovieTableHeaderMouseReader(JTable table) {
            this.table = table;
            this.model = (JMovieTableModel) table.getModel();
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            int column = table.columnAtPoint(e.getPoint());
            if (column != -1) {
                if (model.getSortedByColumn() == column) {
                    model.setReversed(!model.getReversed());
                } else {
                    model.setReversed(false);
                }
                model.setSortedByColumn(column);
                sortTable(column);
            }
        }

        private void sortTable(int column) {
            JMovieTableModel model = (JMovieTableModel) table.getModel();
            model.sortDataByColumn(column);
        }
    }
}
