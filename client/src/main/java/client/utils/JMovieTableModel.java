package client.utils;

import server.object.*;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

public class JMovieTableModel extends AbstractTableModel {
    private final String[] columns = {"id", "name", "coordinates.x", "coordinates.y", "creation date", "oscars count", "genre", "mpaa rating", "is operator exits?", "operator.name", "operator.birthday", "operator.weight", "operator.passportID", "owner"};
    private ArrayList<Movie> data = new ArrayList<>();

    private int sortedBy = 0;
    private boolean reversed = false;

    public JMovieTableModel() {
        data.add(new Movie("1", new Coordinates((float) 1.1, 2), (long) 2, MovieGenre.HORROR, MpaaRating.G, null, ""));
        data.add(new Movie("2", new Coordinates((float) 2.2, 1), (long) 1, MovieGenre.HORROR, MpaaRating.G, null, ""));
        setSortedByColumn(0);
        sortDataByColumn(sortedBy);
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= data.size()) {
            return null;
        }
        Movie movie = data.get(rowIndex);
        return getValueByColumn(columnIndex, movie);
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return switch (columnIndex) {
            case 9, 10, 11, 12 -> (boolean) getValueAt(rowIndex, 8);
            case 13 -> false;
            default -> true;
        };
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Movie movie = data.get(rowIndex);
        switch (columnIndex) {
            case 0 -> movie.setId((int) aValue);
            case 1 -> movie.setName((String) aValue);
            case 2 -> movie.setCoordinateX((Float) aValue);
            case 3 -> movie.setCoordinateY((Integer) aValue);
            case 4 -> movie.setCreationDate((ZonedDateTime) aValue);
            case 5 -> movie.setOscarsCount((Long) aValue);
            case 6 -> movie.setGenre((MovieGenre) aValue);
            case 7 -> movie.setMpaaRating((MpaaRating) aValue);
            case 8 -> {
                    movie.setOperator((boolean) aValue ? new Person("Name", new Date(), Long.parseLong("1"), "123") : null);
                    fireTableDataChanged();
                }
            case 9 -> movie.setOperatorsName((String) aValue);
            case 10 -> movie.setOperatorsBirthday((Date) aValue);
            case 11 -> movie.setOperatorsWeight((Long) aValue);
            case 12 -> movie.setOperatorsPassportID((String) aValue);
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    private Comparable<?> getValueByColumn(int column, Movie o1) {
        return switch (column) {
            case 0 -> o1.getId();
            case 1 -> o1.getName();
            case 2 -> o1.getCoordinates().getX();
            case 3 -> o1.getCoordinates().getY();
            case 4 -> o1.getCreationDate();
            case 5 -> o1.getOscarsCount();
            case 6 -> o1.getGenre();
            case 7 -> o1.getMpaaRating();
            case 8 -> o1.getOperator() != null;
            case 9 -> o1.getOperator() == null ? "" : o1.getOperator().getName();
            case 10 -> o1.getOperator() == null ? null : o1.getOperator().getBirthday();
            case 11 -> o1.getOperator() == null ? null : o1.getOperator().getWeight();
            case 12 -> o1.getOperator() == null ? "" : o1.getOperator().getPassportID();
            case 13 -> o1.getOwner();
            default -> null;
        };
    }

    public void sortDataByColumn(int column) {
        data = data.stream()
                .sorted((o1, o2) -> {
                    if (reversed) return GenericComparator.compare(getValueByColumn(column, o2), getValueByColumn(column, o1));
                    return GenericComparator.compare(getValueByColumn(column, o1), getValueByColumn(column, o2));
                }).collect(Collectors.toCollection(ArrayList::new));
    }

    public static class GenericComparator {
        @SuppressWarnings("unchecked")
        public static int compare(Comparable a, Comparable b) {
            if (a == null) {
                return 1;
            }
            if (b == null) {
                return -1;
            }
            return a.compareTo(b);
        }
    }

    private void setSortedByColumn(int column) {
        if (sortedBy != -1 && (columns[sortedBy].endsWith("▼") || columns[sortedBy].endsWith("▲"))) {
            columns[sortedBy] = columns[sortedBy].substring(0, columns[sortedBy].length() - 2);
        }
        sortedBy = column;
        if (reversed) {
            columns[sortedBy] += " ▲";
        } else {
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

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
            case 0, 3 -> Integer.class;
            case 1, 9, 12, 13 -> String.class;
            case 2 -> Float.class;
            case 4 -> ZonedDateTime.class;
            case 5, 11 -> Long.class;
            case 6 -> MovieGenre.class;
            case 7 -> MpaaRating.class;
            case 8 -> Boolean.class;
            case 10 -> Date.class;
            default -> null;
        };
    }

    static public class JMovieTableHeaderMouseReader extends MouseAdapter {
        private final JMovieTable table;
        private final JMovieTableModel model;

        public JMovieTableHeaderMouseReader(JMovieTable table) {
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
                table.autoResizeColumnWidth();
            }
        }

        private void sortTable(int column) {
            JMovieTableModel model = (JMovieTableModel) table.getModel();
            model.sortDataByColumn(column);
        }
    }
}
