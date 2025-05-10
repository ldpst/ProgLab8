package server.managers;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.object.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class PSQLManager {
    private static final Logger logger = LogManager.getLogger(PSQLManager.class);

    private static final int localPort = 5433;
    private static final String url = "jdbc:postgresql://localhost:" + localPort + "/studs";
    private static final String heliosUrl = "helios.cs.ifmo.ru";
    private static final String user = "s466690";
    private static String pgPassword = "";
    private static String heliosPassword = "";

    private static Session sshSession = null;
    private static Connection connection = null;

    private final CollectionManager collectionManager;

    public PSQLManager(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
        try (BufferedReader reader = new BufferedReader(new FileReader(ConfigManager.getHeliospassPath()))) {
            heliosPassword = reader.readLine();
        } catch (IOException e) {
            logger.error("Не найден файл с паролем от Helios", e);
            throw new RuntimeException(e);
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(ConfigManager.getPgpassPath()))) {
            pgPassword = reader.readLine();
        } catch (IOException e) {
            logger.error("Не найден файл с паролем от PostgreSQL", e);
            throw new RuntimeException(e);
        }

        connect();
    }

    private static void connect() {
        try {
            JSch jsch = new JSch();
            sshSession = jsch.getSession(user, heliosUrl, 2222);
            sshSession.setPassword(heliosPassword);
            sshSession.setConfig("StrictHostKeyChecking", "no");
            sshSession.connect();

            sshSession.setPortForwardingL(localPort, heliosUrl, 5432);
            logger.info("SSH туннель установлен");

            connection = DriverManager.getConnection(url, user, pgPassword);
            logger.info("БД подключена");

        } catch (JSchException e) {
            logger.error("Ошибка при подключении SSH", e);
            throw new RuntimeException(e);
        } catch (SQLException e) {
            logger.error("Ошибка при подключении к БД", e);
            throw new RuntimeException(e);
        }
    }

    public static void disconnect() {
        try {
            sshSession.disconnect();
            connection.close();
            logger.debug("Отключение от БД");
        } catch (SQLException e) {
            logger.error("Ошибка при отключении от БД", e);
            throw new RuntimeException(e);
        }
    }

    public void loadFromDB() {
        logger.debug("Выгрузка данных с БД...");
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT m.id, m.name, m.coordinates_id, c.id as c_id, c.x, c.y, m.creation_date, m.oscars_count, m.movie_genre, m.mpaa_rating, m.operator_id, o.id as o_id, o.name as o_name, o.birthday, o.weight, o.passport_id, m.movie_owner FROM movies m FULL JOIN coordinates c on c.id = m.coordinates_id FULL JOIN operators o on o.id = m.operator_id");
            while (rs.next()) {
                Person person = null;
                if (rs.getInt("operator_id") != -1) {
                    person = new Person(rs.getString("o_name"), rs.getDate("birthday"), rs.getLong("weight"), rs.getString("passport_id"));
                }
                Movie movie = new Movie(rs.getInt("id"), rs.getString("name"), new Coordinates(rs.getFloat("x"), rs.getInt("y")), rs.getTimestamp("creation_date"), rs.getLong("oscars_count"), MovieGenre.valueOf(rs.getString("movie_genre")), MpaaRating.valueOf(rs.getString("mpaa_rating")), person, rs.getString("movie_owner"));
                collectionManager.addFromDB(movie);
            }
            collectionManager.fixNextId();
            logger.debug("Данные с БД выгружены");
            disconnect();
        } catch (SQLException e) {
            logger.error("Ошибка при запросе к БД", e);
            throw new RuntimeException(e);
        }
    }

    public static void insertMovie(Movie movie) {
        logger.debug("Добавление данных в БД: {}...", movie);
        connect();
        if (movie.getOperator() != null) {
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO movies(id, name, creation_date, oscars_count, movie_genre, mpaa_rating, movie_owner) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
                setMovie(statement, movie);
                statement.executeUpdate();
            } catch (SQLException e) {
                logger.error("Ошибка при добавлении данных в БД", e);
                throw new RuntimeException(e);
            }
        } else {
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO movies(id, name, creation_date, oscars_count, movie_genre, mpaa_rating, movie_owner, operator_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
                setMovie(statement, movie);
                statement.setInt(8, -1);
                statement.executeUpdate();
            } catch (SQLException e) {
                logger.error("Ошибка при добавлении данных в БД", e);
                throw new RuntimeException(e);
            }
        }
        insertCoordinates(movie);
        insertPerson(movie);
        logger.debug("Данные в БД добавлены");
        disconnect();
    }

    private static void insertCoordinates(Movie movie) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO coordinates(id, x, y) VALUES (?, ?, ?)")) {
            try (Statement statement1 = connection.createStatement()) {
                ResultSet rs = statement1.executeQuery("SELECT coordinates_id FROM movies WHERE id = " + movie.getId());
                rs.next();
                int id = rs.getInt("coordinates_id");
                statement.setInt(1, id);
                statement.setFloat(2, movie.getCoordinates().getX());
                statement.setInt(3, movie.getCoordinates().getY());
            } catch (SQLException e) {
                logger.error("Ошибка при запросе к БД", e);
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Ошибка при добавлении данных в БД", e);
            throw new RuntimeException(e);
        }
    }

    private static void insertPerson(Movie movie) {
        if (movie.getOperator() != null) {
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO operators(id, name, birthday, weight, passport_id) VALUES (?, ?, ?, ?, ?)")) {
                try (Statement statement1 = connection.createStatement()) {
                    ResultSet rs = statement1.executeQuery("SELECT operator_id FROM movies WHERE id = " + movie.getId());
                    rs.next();
                    int id = rs.getInt("operator_id");
                    statement.setInt(1, id);
                    statement.setString(2, movie.getOperator().getName());
                    statement.setDate(3, new java.sql.Date(movie.getOperator().getBirthday().getTime()));
                    statement.setFloat(4, movie.getOperator().getWeight());
                    statement.setString(5, movie.getOperator().getPassportID());
                } catch (SQLException e) {
                    logger.error("Ошибка при запросе к БД", e);
                }
                statement.executeUpdate();
            } catch (SQLException e) {
                logger.error("Ошибка при добавлении данных в БД", e);
                throw new RuntimeException(e);
            }
        }
    }

    private static void setMovie(PreparedStatement statement, Movie movie) throws SQLException {
        statement.setInt(1, (int) movie.getId());
        statement.setString(2, movie.getName());
        statement.setTimestamp(3, Timestamp.from(movie.getCreationDate().toInstant()));
        statement.setLong(4, movie.getOscarsCount());
        statement.setString(5, movie.getGenre().toString());
        statement.setString(6, movie.getMpaaRating().toString());
        statement.setString(7, movie.getOwner());
    }

    public static boolean logIn(String login, String password) {
        connect();
        try (PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM users WHERE login = ? AND password = ?")) {
            statement.setString(1, login);
            statement.setString(2, password);
            ResultSet rs = statement.executeQuery();
            int count = 0;
            if (rs.next()) {
                count = rs.getInt(1);
            }
            disconnect();
            return count >= 1;
        } catch (SQLException e) {
            logger.error("Ошибка при поиске логина и пароля в БД", e);
            throw new RuntimeException(e);
        }
    }

    public static boolean signIn(String login, String password) {
        connect();
        boolean used = true;
        try (PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM users WHERE login = ?")) {
            statement.setString(1, login);
            ResultSet rs = statement.executeQuery();
            int count = 0;
            if (rs.next()) {
                count = rs.getInt(1);
            }
            if (count == 0) {
                used = false;
            }
        } catch (SQLException e) {
            logger.error("Ошибка при проверке на уникальность логина в БД", e);
            throw new RuntimeException(e);
        }
        if (used) {
            disconnect();
            return false;
        }
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO users(login, password) VALUES (?, ?)")) {
            statement.setString(1, login);
            statement.setString(2, password);
            statement.executeUpdate();
            disconnect();
            return true;
        } catch (SQLException e) {
            logger.error("Ошибка при добавлении логина и пароля в БД", e);
            throw new RuntimeException(e);
        }
    }

    public static void deleteMovie(Movie movie) {
        connect();
        int c_id = -1;
        int o_id = -1;
        try (PreparedStatement statement = connection.prepareStatement("SELECT coordinates_id, operator_id FROM movies WHERE id=?")) {
            statement.setInt(1, (int) movie.getId());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                c_id = rs.getInt("coordinates_id");
                o_id = rs.getInt("operator_id");
            }
        } catch (SQLException e) {
            logger.error("Ошибка при поиске c_id и o_id из БД", e);
        }
        if (o_id != -1) {
            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM operators WHERE id=?")) {
                statement.setInt(1, o_id);
                statement.executeUpdate();
            } catch (SQLException e) {
                logger.error("Ошибка при удалении из operators в БД", e);
            }
        }
        if (c_id != -1) {
            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM coordinates WHERE id=?")) {
                statement.setInt(1, c_id);
                statement.executeUpdate();
            } catch (SQLException e) {
                logger.error("Ошибка при удалении из coordinates в БД", e);
            }
        }

        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM movies WHERE id=?")) {
            statement.setInt(1, (int) movie.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Ошибка при удалении из movies в БД", e);
        }

        disconnect();
    }
}
