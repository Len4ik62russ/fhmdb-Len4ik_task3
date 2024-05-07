package at.ac.fhcampuswien.fhmdb.repository.impl;

import at.ac.fhcampuswien.fhmdb.database.DatabaseManager;
import at.ac.fhcampuswien.fhmdb.database.MovieEntity;
import at.ac.fhcampuswien.fhmdb.exception.DatabaseException;
import at.ac.fhcampuswien.fhmdb.repository.MovieRepository;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MovieRepositoryImpl implements MovieRepository {
    private DatabaseManager databaseManager;

    public MovieRepositoryImpl() {
        databaseManager = DatabaseManager.getDatabaseManager();
    }

    @Override
    public void save(MovieEntity movieEntity) throws DatabaseException {
        try {
            Dao<MovieEntity, Long> movieDao = databaseManager.getMovieDao();
            movieDao.create(movieEntity);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        } finally {
            databaseManager.closeConnectionSource();
        }
    }

    @Override
    public MovieEntity get(String apiId) throws DatabaseException {
        List<MovieEntity> result;
        try {
            QueryBuilder<MovieEntity, Long> queryBuilder = databaseManager.getMovieDao().queryBuilder();
            queryBuilder.where().like("APIID", "%" + apiId + "%"); // поиск подстроки

            // Получаем результат запроса
            result = queryBuilder.query();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
        if (result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public List<MovieEntity> getAll() throws DatabaseException {
        List<MovieEntity> result;
        try {
            result = databaseManager.getMovieDao().queryForAll();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
        return result;
    }

}
