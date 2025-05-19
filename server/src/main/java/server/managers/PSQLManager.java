package server.managers;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
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
        loadPasswords();
        connect();
    }

    private void loadPasswords() {
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
    }

    private static void connect() {
        try {
            connection = DBConnectionPool.getConnection();
            logger.debug("БД подключена");
        } catch (SQLException e) {
            logger.error("Ошибка при подключении к БД", e);
            throw new RuntimeException(e);
        }
    }

    public static void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                logger.debug("Отключение от БД");
            }
        } catch (SQLException e) {
            logger.error("Ошибка при отключении от БД", e);
        }
    }

    public void loadFromDB() {
        logger.debug("Выгрузка данных с БД...");
        String query = "SELECT m.id, m.name, m.coordinates_id, c.id as c_id, c.x, c.y, " +
                "m.creation_date, m.oscars_count, m.movie_genre, m.mpaa_rating, m.operator_id, " +
                "o.id as o_id, o.name as o_name, o.birthday, o.weight, o.passport_id, m.movie_owner " +
                "FROM movies m " +
                "LEFT JOIN coordinates c on c.id = m.coordinates_id " +
                "LEFT JOIN operators o on o.id = m.operator_id";

        try (Statement statement = connection.createStatement(); ResultSet rs = statement.executeQuery(query)) {
            while (rs.next()) {
                Person person = null;
                if (rs.getInt("operator_id") != -1) {
                    String operatorName = rs.getString("o_name");
                    Date birthday = rs.getDate("birthday");
                    long weight = rs.getLong("weight");
                    String passportID = rs.getString("passport_id");
                    if (operatorName != null && passportID != null && weight > 0) {
                        person = new Person(operatorName, birthday, weight, passportID);
                    }
                }
                Movie movie = new Movie(
                        rs.getInt("id"),
                        rs.getString("name"),
                        new Coordinates(rs.getFloat("x"), rs.getInt("y")),
                        rs.getTimestamp("creation_date"),
                        rs.getLong("oscars_count"),
                        MovieGenre.valueOf(rs.getString("movie_genre")),
                        MpaaRating.valueOf(rs.getString("mpaa_rating")),
                        person,
                        rs.getString("movie_owner")
                );
                collectionManager.addFromDB(movie);
            }
            collectionManager.fixNextId();
            logger.debug("Данные с БД выгружены");
        } catch (SQLException e) {
            logger.error("Ошибка при запросе к БД", e);
            throw new RuntimeException(e);
        } finally {
            disconnect();
        }
    }

    public static void insertMovie(Movie movie) {
        logger.debug("Добавление фильма в БД: {}", movie);
        connect();

        try {
            connection.setAutoCommit(false);

            int coordinatesId = insertCoordinates(movie.getCoordinates());
            int operatorId = insertPerson(movie.getOperator());

            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO movies (id, name, creation_date, oscars_count, movie_genre, mpaa_rating, movie_owner, coordinates_id, operator_id) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
                setMovie(statement, movie);
                statement.setInt(8, coordinatesId);
                if (operatorId == -1) {
                    statement.setNull(9, Types.INTEGER);
                } else {
                    statement.setInt(9, operatorId);
                }
                statement.executeUpdate();
            }

            connection.commit(); // Подтверждаем транзакцию
            logger.debug("Фильм успешно добавлен в БД");

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                logger.error("Ошибка при откате транзакции", rollbackEx);
            }
            logger.error("Ошибка при добавлении фильма в БД", e);
            throw new RuntimeException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                logger.warn("Ошибка при возврате autoCommit в true", e);
            }
            disconnect();
        }
    }

    private static int insertCoordinates(Coordinates coordinates) throws SQLException {
        String sql = "INSERT INTO coordinates (x, y) VALUES (?, ?) RETURNING id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setFloat(1, coordinates.getX());
            statement.setInt(2, coordinates.getY());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) return rs.getInt("id");
            throw new SQLException("Не удалось вставить координаты");
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

    private static int insertPerson(Person person) throws SQLException {
        if (person == null) return -1;

        String sql = "INSERT INTO operators (name, birthday, weight, passport_id) VALUES (?, ?, ?, ?) RETURNING id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, person.getName());
            statement.setDate(2, new java.sql.Date(person.getBirthday().getTime()));
            statement.setFloat(3, person.getWeight());
            statement.setString(4, person.getPassportID());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) return rs.getInt("id");
            throw new SQLException("Не удалось вставить оператора");
        }
    }




    public static boolean logIn(String login, String password) {
        connect();
        String query = "SELECT COUNT(*) FROM users WHERE login = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, login);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) >= 1;
        } catch (SQLException e) {
            logger.error("Ошибка при входе пользователя", e);
            throw new RuntimeException(e);
        } finally {
            disconnect();
        }
    }

    public static boolean signIn(String login, String password) {
        connect();
        String checkQuery = "SELECT COUNT(*) FROM users WHERE login = ?";
        String insertQuery = "INSERT INTO users(login, password) VALUES (?, ?)";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
            checkStmt.setString(1, login);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return false;
            }

            try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                insertStmt.setString(1, login);
                insertStmt.setString(2, password);
                insertStmt.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            logger.error("Ошибка при регистрации пользователя", e);
            throw new RuntimeException(e);
        } finally {
            disconnect();
        }
    }

    public static void deleteMovie(Movie movie) {
        connect();
        int c_id = -1;
        int o_id = -1;
        try (PreparedStatement stmt = connection.prepareStatement("SELECT coordinates_id, operator_id FROM movies WHERE id=?")) {
            stmt.setInt(1, (int) movie.getId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                c_id = rs.getInt("coordinates_id");
                o_id = rs.getInt("operator_id");
            }
        } catch (SQLException e) {
            logger.error("Ошибка при получении id для удаления", e);
        }

        if (o_id != -1) {
            try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM operators WHERE id=?")) {
                stmt.setInt(1, o_id);
                stmt.executeUpdate();
            } catch (SQLException e) {
                logger.error("Ошибка при удалении оператора", e);
            }
        }

        if (c_id != -1) {
            try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM coordinates WHERE id=?")) {
                stmt.setInt(1, c_id);
                stmt.executeUpdate();
            } catch (SQLException e) {
                logger.error("Ошибка при удалении координат", e);
            }
        }

        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM movies WHERE id=?")) {
            stmt.setInt(1, (int) movie.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Ошибка при удалении фильма", e);
        } finally {
            disconnect();
        }
    }

    public static class DBConnectionPool {
        private static final HikariDataSource dataSource;

        static {
            JSch jsch = new JSch();
            try {
                sshSession = jsch.getSession(user, heliosUrl, 2222);
                sshSession.setPassword(heliosPassword);
                sshSession.setConfig("StrictHostKeyChecking", "no");
                sshSession.setPortForwardingL(localPort, heliosUrl, 5432);
                sshSession.connect();
                logger.info("SSH туннель установлен");
            } catch (JSchException e) {
                logger.error("Ошибка при подключении SSH", e);
                throw new RuntimeException(e);
            }

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(url);
            config.setUsername(user);
            config.setPassword(pgPassword);
            config.setMaximumPoolSize(60);
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            dataSource = new HikariDataSource(config);
        }

        public static Connection getConnection() throws SQLException {
            return dataSource.getConnection();
        }
    }
}
