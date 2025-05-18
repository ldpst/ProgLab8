package client.utils.JUtils;

import client.client.UDPClient;
import client.exceptions.ServerIsUnavailableException;
import client.utils.Languages;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.logging.log4j.core.util.JsonUtils;
import server.object.Movie;
import server.response.Response;
import server.response.ResponseType;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class JCoordinatesPanel extends JPanel {

    private BufferedImage image;
    private ArrayList<Movie> images = new ArrayList<>();
    private final LinkedHashSet<String> owners = new LinkedHashSet<>();

    private final int cellSize = 10;
    private final int defaultImageWidth = 32, defaultImageHeight = 32;
    private final int scaleImageByLevel = 4;
    private double zoom = 1.0;
    private double offsetX = 0;
    private double offsetY = 0;

    private boolean dragging = false;
    private int lastX = 0;
    private int lastY = 0;

    private final UDPClient client;
    private final JFrame frame;
    private final JMovieTable table;

    public JCoordinatesPanel(UDPClient client, JMovieTable table, JFrame frame) {
        try {
            image = convertToARGB(ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("Movie.png"))));
        } catch (IOException e) {
            System.out.println("Ошибка при загрузке изображения фильма");
            throw new RuntimeException(e);
        }

        this.client = client;
        this.table = table;
        this.frame = frame;
        getImages();

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    dragging = true;
                    lastX = e.getX();
                    lastY = e.getY();
                }
                if (SwingUtilities.isRightMouseButton(e)) {
                    Point clicked = e.getPoint();
                    handleRightClick(clicked);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                dragging = false;
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragging) {
                    int x = e.getX() - lastX;
                    int y = e.getY() - lastY;
                    offsetX = Math.min(3465, Math.max(-3465, offsetX + x / zoom));
                    offsetY = Math.min(2020, Math.max(-2020, offsetY + y / zoom));
                    lastX = e.getX();
                    lastY = e.getY();
                    repaint();
                }
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int notches = e.getWheelRotation();
                double scaleFactor = 1.1;

                if (notches < 0) {
                    zoom = Math.min(1 * Math.pow(1.1, 30), zoom * scaleFactor);
                } else {
                    zoom = Math.max(1 / Math.pow(1.1, 24), zoom / scaleFactor);
                }

                repaint();
            }
        };

        addMouseListener(mouseAdapter);
        addMouseWheelListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);

        new Thread(() -> {
            while (true) {
                update();

                try {
                    Thread.sleep(3 * 1000L);
                } catch (InterruptedException e) {
                    System.out.println("Поток обновлений прерван");
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        AffineTransform oldTransform = g2d.getTransform();

        g2d.translate(getWidth() / 2.0, getHeight() / 2.0);
        g2d.scale(zoom, zoom);

        drawAxes(g2d);
        g2d.setTransform(oldTransform);

        for (Movie movie : images) {
            double x = movie.getCoordinates().getX() * cellSize;
            double y = -movie.getCoordinates().getY() * cellSize;

            int imageWidth = (int) (defaultImageWidth + scaleImageByLevel * movie.getOscarsCount());
            int imageHeight = (int) (defaultImageHeight + scaleImageByLevel * movie.getOscarsCount());

            Point screen = logicalToScreen(x, y);

            BufferedImage coloredImage = colorImage(movie);
            g2d.drawImage(
                    coloredImage,
                    screen.x - imageWidth / 2,
                    screen.y - imageHeight / 2,
                    imageWidth,
                    imageHeight,
                    null
            );
        }

        g2d.setTransform(oldTransform);
    }

    public BufferedImage convertToARGB(BufferedImage src) {
        if (src.getType() != BufferedImage.TYPE_INT_ARGB) {
            BufferedImage convertedImage = new BufferedImage(
                    src.getWidth(),
                    src.getHeight(),
                    BufferedImage.TYPE_INT_ARGB);

            Graphics2D g2d = convertedImage.createGraphics();
            g2d.drawImage(src, 0, 0, null);
            g2d.dispose();

            return convertedImage;
        }
        return src;
    }

    public BufferedImage colorImage(Movie movie) {
        float[] color = countColor(movie);

        float[] scales = {color[0], color[1], color[2], 1f};
        float[] offsets = {0f, 0f, 0f, 0f};
        RescaleOp op = new RescaleOp(scales, offsets, null);

        BufferedImage coloredImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        op.filter(image, coloredImage);

        return coloredImage;
    }

    private float[] countColor(Movie movie) {
        ArrayList<String> list = new ArrayList<>(owners);
        Random rng = new Random(Long.hashCode(list.indexOf(movie.getOwner())));

        float a = 0.5f + rng.nextFloat() * 0.5f;
        float b = 0.5f + rng.nextFloat() * 0.5f;
        float c = 0.5f + rng.nextFloat() * 0.5f;

        return new float[] {a, b, c};
    }

    private void handleRightClick(Point clicked) {
        for (Movie movie : images) {
            double x = movie.getCoordinates().getX() * cellSize;
            double y = -movie.getCoordinates().getY() * cellSize;

            int imageWidth = (int) (defaultImageWidth + scaleImageByLevel * movie.getOscarsCount());
            int imageHeight = (int) (defaultImageHeight + scaleImageByLevel * movie.getOscarsCount());

            Point screen = logicalToScreen(x, y);

            Rectangle imageBounds = new Rectangle(
                    screen.x - imageWidth / 2,
                    screen.y - imageHeight / 2,
                    imageWidth,
                    imageHeight
            );

            if (imageBounds.contains(clicked)) {
                buildMovieJPopupMenu(clicked, movie);

                return;
            }
        }
    }

    private void buildMovieJPopupMenu(Point point, Movie movie) {
        JPopupMenu popup = new JPopupMenu();
        popup.setLayout(new BoxLayout(popup, BoxLayout.PAGE_AXIS));
        addInfoLabels(popup, movie);
        popup.show(this, point.x, point.y);
    }

    private void addInfoLabels(JPopupMenu popup, Movie movie) {
        JLabel id = new JLabel("id" + ": " + movie.getId());
        JLabel name = new JLabel(Languages.get("name") + ": " + movie.getName());
        JLabel x = new JLabel(Languages.get("coordinateX") + ": " + movie.getCoordinates().getX());
        JLabel y = new JLabel(Languages.get("coordinateY") + ": " + movie.getCoordinates().getY());
        JLabel creationDate = new JLabel(Languages.get("creationDate") + ": " + movie.getCreationDate().withZoneSameInstant(Objects.requireNonNull(Languages.getCurrentLocale()).second).format(DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy HH:mm z", Objects.requireNonNull(Languages.getCurrentLocale()).first)));
        JLabel oscarsCount = new JLabel(Languages.get("oscarsCount") + ": " + movie.getOscarsCount());
        JLabel genre = new JLabel(Languages.get("genre") + ": " + movie.getGenre());
        JLabel rating = new JLabel(Languages.get("rating") + ": " + movie.getMpaaRating());
        popup.add(id);
        popup.add(name);
        popup.add(x);
        popup.add(y);
        popup.add(creationDate);
        popup.add(oscarsCount);
        popup.add(genre);
        popup.add(rating);
        if (movie.getOperator() != null) {
            JLabel operatorName = new JLabel(Languages.get("operatorName") + ": " + movie.getOperator().getName());
            JLabel operatorBirthday = new JLabel(Languages.get("operatorBirthday") + ": " + new SimpleDateFormat("EEEE, d MMMM, yyyy", Objects.requireNonNull(Languages.getCurrentLocale()).first).format(movie.getOperator().getBirthday()));
            JLabel operatorWeight = new JLabel(Languages.get("operatorWeight") + ": " + movie.getOperator().getWeight());
            JLabel operatorPassportID = new JLabel(Languages.get("operatorPassportID") + ": " + movie.getOperator().getPassportID());
            popup.add(operatorName);
            popup.add(operatorBirthday);
            popup.add(operatorWeight);
            popup.add(operatorPassportID);
        }
        JLabel owner = new JLabel(Languages.get("owner") + ": " + movie.getOwner());
        popup.add(owner);
    }

    private Point logicalToScreen(double logicalX, double logicalY) {
        double x = getWidth() / 2.0 + (logicalX + offsetX) * zoom;
        double y = getHeight() / 2.0 + (logicalY + offsetY) * zoom;
        return new Point((int) x, (int) y);
    }

    private Point2D screenToLogical(Point screen) {
        double x = (screen.x - getWidth() / 2.0) / zoom - offsetX;
        double y = (screen.y - getHeight() / 2.0) / zoom - offsetY;
        return new Point2D.Double(x, y);
    }

    private void drawAxes(Graphics2D g) {
        int x = -getWidth() / 2;
        int y = -getHeight() / 2;

        int w = getWidth();
        int h = getHeight();

        int cx = (int) offsetX;
        int cy = (int) offsetY;

        g.setColor(Color.WHITE);
        g.fillRect(x * 15, y * 15, w * 15, h * 15);


        g.setColor(Color.BLACK);
        g.drawLine(cx, -y * 15, cx, y * 15); // ось y
        for (int i = 10; i < -y * 15; i += 10) {
            g.drawLine(cx - 3, cy + i, cx + 3, cy + i);
            g.drawLine(cx - 3, cy - i, cx + 3, cy - i);
        }

        g.drawLine(x * 15, cy, -x * 15, cy); // ось x
        for (int i = 10; i < -x * 15; i += 10) {
            g.drawLine(cx + i, cy - 3, cx + i, cy + 3);
            g.drawLine(cx - i, cy - 3, cx - i, cy + 3);
        }
    }

    public void update() {
        try {
            getImages();
        } catch (ServerIsUnavailableException e) {
            if (isDisplayable()) {
                JDialog dialog = new JDialog();
                dialog.setTitle(Languages.get("error"));
                dialog.setSize(new Dimension(400, 300));
                dialog.setModal(true);
                dialog.setLocationRelativeTo(frame);
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                dialog.setLayout(new BorderLayout());

                JLabel label = new JLabel(Languages.get("serverIsUnavailable"));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setVerticalAlignment(SwingConstants.CENTER);
                dialog.add(label, BorderLayout.CENTER);

                dialog.setVisible(true);
            }
        }
        repaint();
    }

    private void getImages() {
        try {
            Response response = client.makeRequest("show", client.getLogin(), client.getPassword());
            if (response.getType() != ResponseType.ERROR) {
                images = new ArrayList<>(response.getCollection().stream().collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
            }
            getOwners();
        } catch (IOException e) {
            System.out.println("Ошибка при отправке запроса для заполнения коллекции");
        }
    }

    private void getOwners() {
        for (Movie movie : images) {
            owners.add(movie.getOwner());
        }
    }
}
