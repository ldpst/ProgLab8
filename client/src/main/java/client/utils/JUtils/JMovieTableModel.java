package client.utils.JUtils;

import client.client.UDPClient;
import client.exceptions.ServerIsUnavailableException;
import client.utils.GBCUtils;
import client.utils.Languages;
import server.object.*;
import server.response.Response;
import server.response.ResponseType;
import server.utils.ValidationError;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Timer;
import java.util.stream.Collectors;

public class JMovieTableModel extends AbstractTableModel {
    private final String[] columns = {"id ▼", "name", "coordinates.x", "coordinates.y", "creation date", "oscars count", "genre", "mpaa rating", "is operator exits?", "operator.name", "operator.birthday", "operator.weight", "operator.passportID", "owner"};
    private ArrayList<Movie> data = new ArrayList<>();
    private static int sortedBy = 0;
    private boolean reversed = false;

    private final UDPClient client;
    private final JFrame frame;

    private final Debouncer debouncer = new Debouncer();

    public JMovieTableModel(UDPClient client, JFrame frame) {
        this.client = client;
        this.frame = frame;
        loadData();
    }

    public void loadData() {
        try {
            Response response = client.makeRequest("show", client.getLogin(), client.getPassword());
            if (response.getType() != ResponseType.ERROR) {
                data = new ArrayList<>(response.getCollection().stream().collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
            }
        } catch (IOException e) {
            System.out.println("Ошибка при отправке запроса для заполнения коллекции");
        }
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
        if (!((String) getValueAt(rowIndex, 13)).equals(client.getLogin())) {
            return false;
        }
        return switch (columnIndex) {
            case 9, 10, 11, 12 -> (boolean) getValueAt(rowIndex, 8);
            case 0, 4, 13 -> false;
            default -> true;
        };
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Movie movie = data.get(rowIndex);
        try {
            switch (columnIndex) {
                case 1 -> movie.setName(((String) aValue).trim());
                case 2 -> movie.setCoordinateX(Float.parseFloat(((String) aValue).trim()));
                case 3 -> movie.setCoordinateY(Integer.parseInt(((String) aValue).trim()));
                case 5 -> movie.setOscarsCount(Long.parseLong(((String) aValue).trim()));
                case 6 -> movie.setGenre((MovieGenre) aValue);
                case 7 -> movie.setMpaaRating((MpaaRating) aValue);
                case 8 -> {
                    movie.setOperator((boolean) aValue ? new Person("Name", new Date(), Long.parseLong("1"), "123") : null);
                    fireTableDataChanged();
                }
                case 9 -> movie.setOperatorsName((String) aValue);
                case 10 -> {
                    if (aValue == null || ((String) aValue).isEmpty()) movie.setOperatorsBirthday(null);
                    else movie.setOperatorsBirthday(new SimpleDateFormat("dd/MM/yyyy").parse(((String) aValue).trim()));
                }
                case 11 -> movie.setOperatorsWeight((Long.parseLong(((String) aValue).trim())));
                case 12 -> movie.setOperatorsPassportID((String) aValue);
            }
        } catch (ParseException e) {
            throw new ValidationError(Languages.get("wrongDateFormat")); // так как метод parse плохой и не позволяет забить на ексцептшен и обработать его лишь на уровне ValidationCellEditor, используем подобный костыль)
        } catch (NumberFormatException e) {
            throw new ValidationError(Languages.get("wrongNumberFormat"));
            // ну раз уж ParseException добавили, то почему бы и тут не согрешить?) Раньше оно обрабатывалось на уровне ValidationCellEditor
        }
        fireTableCellUpdated(rowIndex, columnIndex);
        updateValueOnServer(movie);
    }

    private Comparable<?> getValueByColumn(int column, Movie o1) {
        return switch (column) {
            case 0 -> o1.getId();
            case 1 -> o1.getName();
            case 2 -> o1.getCoordinates().getX();
            case 3 -> o1.getCoordinates().getY();
            case 4 -> o1.getCreationDate().withZoneSameInstant(Objects.requireNonNull(Languages.getCurrentLocale()).second).format(DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy HH:mm z", Objects.requireNonNull(Languages.getCurrentLocale()).first));
            case 5 -> o1.getOscarsCount();
            case 6 -> o1.getGenre();
            case 7 -> o1.getMpaaRating();
            case 8 -> o1.getOperator() != null;
            case 9 -> o1.getOperator() == null ? "" : o1.getOperator().getName();
            case 10 -> o1.getOperator() == null ? null : new SimpleDateFormat("EEEE, d MMMM, yyyy", Objects.requireNonNull(Languages.getCurrentLocale()).first).format(o1.getOperator().getBirthday());
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
            case 1, 9, 10, 12, 13 -> String.class;
            case 2 -> Float.class;
            case 4 -> ZonedDateTime.class;
            case 5, 11 -> Long.class;
            case 6 -> MovieGenre.class;
            case 7 -> MpaaRating.class;
            case 8 -> Boolean.class;
            default -> null;
        };
    }
    
    private void updateValueOnServer(Movie movie) {
        debouncer.update(movie);
    }

    private class Debouncer {
        private final Map<Long, Timer> timers = new HashMap<>();
        private static final long DELAY = 500;

        public synchronized void update(Movie movie) {
            long id = movie.getId();

            Timer oldTimer = timers.get(id);
            if (oldTimer != null) {
                oldTimer.cancel();
            }

            Timer timer = new Timer();
            timers.put(id, timer);

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        client.makeRequest("update " + id, movie, client.getLogin(), client.getPassword());
                    } catch (IOException | ServerIsUnavailableException e) {
                        showErrorDialog();
                    } finally {
                        synchronized (Debouncer.this) {
                            timers.remove(id);
                        }
                    }
                }
            }, DELAY);
        }

        private void showErrorDialog() {
            SwingUtilities.invokeLater(() -> {
                JDialog dialog = new JDialog();
                dialog.setLayout(new BorderLayout());
                dialog.setTitle(Languages.get("error"));
                dialog.setModal(true);
                dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                dialog.setSize(new Dimension(630, 260));
                dialog.setLocationRelativeTo(frame);

                JLabel label = new JLabel(Languages.get("serverIsUnavailable"));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setVerticalAlignment(SwingConstants.CENTER);
                dialog.add(label, BorderLayout.CENTER);

                dialog.setVisible(true);
            });
        }
    }

    public ArrayList<Movie> getData() {
        return data;
    }
    static public class JMovieTableHeaderMouseReader extends MouseAdapter {
        private final JMovieTable table;
        private final JMovieTableModel model;
        private String[] columns;

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
                setSortedByColumn(column);
                sortTable(column);
                table.autoResizeColumnWidth();
                table.repaint();
            }
        }

        private void sortTable(int column) {
            JMovieTableModel model = (JMovieTableModel) table.getModel();
            model.sortDataByColumn(column);
        }


        private void setSortedByColumn(int column) {
            columns = model.getColumns();
            if (sortedBy != -1 && (columns[sortedBy].endsWith("▼") || columns[sortedBy].endsWith("▲"))) {
                columns[sortedBy] = columns[sortedBy].substring(0, columns[sortedBy].length() - 2);
            }
            sortedBy = column;
            if (model.getReversed()) {
                columns[sortedBy] += " ▲";
            } else {
                columns[sortedBy] += " ▼";
            }
            updateHeaders();
        }

        private void updateHeaders() {
            TableColumnModel columnModel = table.getColumnModel();
            for (int i = 0; i < columnModel.getColumnCount(); i++) {
                columnModel.getColumn(i).setHeaderValue(columns[i]);
            }
            table.getTableHeader().repaint();
        }
    }
}
