package at.ac.fhcampuswien.fhmdb.repository;

import at.ac.fhcampuswien.fhmdb.database.MovieEntity;
import at.ac.fhcampuswien.fhmdb.database.WatchlistMovieEntity;
import at.ac.fhcampuswien.fhmdb.exception.DatabaseException;

import java.util.List;

public interface WatchListMovieRepository {
    void save(WatchlistMovieEntity watchlistMovieEntity);

    WatchlistMovieEntity get(String apiId);

    void delete(String apiId) throws DatabaseException;

    List<WatchlistMovieEntity> getAll();

}
