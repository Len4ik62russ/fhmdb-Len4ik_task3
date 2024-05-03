package at.ac.fhcampuswien.fhmdb.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DatabaseManager {
    private static String DB_Url = "jdbc:h2:file: ./src/main/resources/movies";
    private static String username = "username";
    private static String password = "password";
    private static ConnectionSource connectionSource;
    Dao<MovieEntity, Long> movieDao;
    Dao<WatchlistMovieEntity, Long> watchlistMovieDao;
    private static DatabaseManager instance;

    public static ConnectionSource getConnectionSource() {
        return connectionSource;
    }

    public Dao<MovieEntity, Long> getMovieDao() {
        return movieDao;
    }
    public Dao<WatchlistMovieEntity, Long> getWatchlistMovieDao() {
        return watchlistMovieDao;
    }

    private DatabaseManager() {
        try {
            createConnectionSource();
            movieDao = DaoManager.createDao(connectionSource, MovieEntity.class);
            watchlistMovieDao = DaoManager.createDao(connectionSource, WatchlistMovieEntity.class);
            createTables();

        } catch (SQLException e) {
            System.out.println("Error creating connection source: " + e.getMessage());
        }
    }

    public static DatabaseManager getDatabaseManager() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private static void createTables() throws SQLException {
        TableUtils.createTableIfNotExists(connectionSource, MovieEntity.class);
        TableUtils.createTableIfNotExists(connectionSource, WatchlistMovieEntity.class);
    }

    public void createConnectionSource() throws SQLException {
        connectionSource = new JdbcConnectionSource(DB_Url, username, password);
    }

    public void closeConnectionSource(){
        try {
            connectionSource.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void testDB() throws SQLException {
        MovieEntity movie = new MovieEntity("Title1", "Description1");
        movieDao.create(movie);
    }

}
