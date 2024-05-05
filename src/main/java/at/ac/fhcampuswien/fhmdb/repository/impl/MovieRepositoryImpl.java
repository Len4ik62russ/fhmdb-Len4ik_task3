package at.ac.fhcampuswien.fhmdb.repository.impl;

import at.ac.fhcampuswien.fhmdb.database.DatabaseManager;
import at.ac.fhcampuswien.fhmdb.database.MovieEntity;
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
    public void save(MovieEntity movieEntity) {
        try {
            Dao<MovieEntity, Long> movieDao = databaseManager.getMovieDao();
            movieDao.create(movieEntity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            databaseManager.closeConnectionSource();
        }
    }

    @Override
    public MovieEntity get(String apiId) {
        List<MovieEntity> result = new ArrayList<>();
        try {
            QueryBuilder<MovieEntity, Long> queryBuilder = databaseManager.getMovieDao().queryBuilder();
            queryBuilder.where().like("APIID", "%" + apiId + "%"); // поиск подстроки

            // Получаем результат запроса
            result = queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public List<MovieEntity> getAll() {
        List<MovieEntity> result = new ArrayList<>();
        try {
            result = databaseManager.getMovieDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

}
