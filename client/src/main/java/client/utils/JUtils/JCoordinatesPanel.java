package client.utils.JUtils;

import client.client.UDPClient;
import server.object.Movie;
import server.response.Response;
import server.response.ResponseType;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class JCoordinatesPanel extends JPanel {

    private BufferedImage image;
    private ArrayList<Movie> images = new ArrayList<>();

    private int cellSize = 10;
    private int imageWidth = 32, imageHeight = 32;
    private double zoom = 1.0;
    private double offsetX = 0;
    private double offsetY = 0;

    private boolean dragging = false;
    private int lastX = 0;
    private int lastY = 0;

    private final UDPClient client;

    public JCoordinatesPanel(UDPClient client) {
        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("Movie.png")));
        } catch (IOException e) {
            System.out.println("Ошибка при загрузке изображения фильма");
            throw new RuntimeException(e);
        }

        this.client = client;
        getImages();

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    dragging = true;
                    lastX = e.getX();
                    lastY = e.getY();
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

        for (Movie pair : images) {
            double x = pair.getCoordinates().getX() * cellSize;
            double y = -pair.getCoordinates().getY() * cellSize;

            Point screen = logicalToScreen(x, y);


            g2d.drawImage(
                    image,
                    screen.x - imageWidth / 2,
                    screen.y - imageHeight / 2,
                    imageWidth,
                    imageHeight,
                    null
            );
        }

        g2d.setTransform(oldTransform);
    }

    private Point logicalToScreen(double logicalX, double logicalY) {
        double x = getWidth() / 2.0 + (logicalX + offsetX) * zoom;
        double y = getHeight() / 2.0 + (logicalY + offsetY) * zoom;
        return new Point((int) x, (int) y);
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
        getImages();
        repaint();
    }

    private void getImages() {
        try {
            Response response = client.makeRequest("show", client.getLogin(), client.getPassword());
            if (response.getType() != ResponseType.ERROR) {
                images = new ArrayList<>(response.getCollection().stream().collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
            }
        } catch (IOException e) {
            System.out.println("Ошибка при отправке запроса для заполнения коллекции");
        }
    }
}
