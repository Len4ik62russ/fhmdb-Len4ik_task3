package at.ac.fhcampuswien.fhmdb.repository.impl;

import at.ac.fhcampuswien.fhmdb.database.DatabaseManager;
import at.ac.fhcampuswien.fhmdb.database.WatchlistMovieEntity;
import at.ac.fhcampuswien.fhmdb.exception.DatabaseException;
import at.ac.fhcampuswien.fhmdb.repository.WatchListMovieRepository;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WatchListMovieRepositoryImpl implements WatchListMovieRepository {
    private DatabaseManager databaseManager;

    public WatchListMovieRepositoryImpl() {
        databaseManager = DatabaseManager.getDatabaseManager();
    }

    @Override
    public void save(WatchlistMovieEntity watchlistMovieEntity) throws DatabaseException{
        try {
            Dao<WatchlistMovieEntity, Long> watchlistMovieDao = databaseManager.getWatchlistMovieDao();
            watchlistMovieDao.create(watchlistMovieEntity);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to save movie to watchlist: " + e.getMessage());
        } finally {
            databaseManager.closeConnectionSource();
        }
    }

    @Override
    public WatchlistMovieEntity get(String apiId) throws DatabaseException {
        List<WatchlistMovieEntity> result;
        try {
            QueryBuilder<WatchlistMovieEntity, Long> queryBuilder = databaseManager.getWatchlistMovieDao().queryBuilder();
            queryBuilder.where().like("APIID", "%" + apiId + "%"); // поиск подстроки

            // Получаем результат запроса
            result = queryBuilder.query();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to get movie from watchlist: " + e.getMessage());
        }
        if (result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public void delete(String apiId) throws DatabaseException {
        try {
            QueryBuilder<WatchlistMovieEntity, Long> queryBuilder = databaseManager.getWatchlistMovieDao().queryBuilder();

            // Добавляем условие поиска по полю
            queryBuilder.where().like("APIID", "%" + apiId + "%"); // поиск подстроки

            // Получаем результат запроса
            List<WatchlistMovieEntity> result = queryBuilder.query();
            if (result.isEmpty()) {
                throw new DatabaseException("No such movie in watchlist");
            }
            databaseManager.getWatchlistMovieDao().delete(result);

        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete movie from watchlist: " + e.getMessage());
        }
    }

    @Override
    public List<WatchlistMovieEntity> getAll() throws DatabaseException {
        List<WatchlistMovieEntity> result;
        try {
            QueryBuilder<WatchlistMovieEntity, Long> queryBuilder = databaseManager.getWatchlistMovieDao().queryBuilder();
            // Получаем результат запроса
            result = queryBuilder.query();

        } catch (SQLException e) {
            throw new DatabaseException("Failed to get all movies from watchlist: " + e.getMessage());
        }
        return result;
    }
}
