package at.ac.fhcampuswien.fhmdb.service;

import at.ac.fhcampuswien.fhmdb.database.WatchlistMovieEntity;
import at.ac.fhcampuswien.fhmdb.models.Movie;

import java.util.List;

public interface WatchListService {
    void save(WatchlistMovieEntity watchlistMovieEntity);

    WatchlistMovieEntity get(String apiId);

    List<WatchlistMovieEntity> getAll();

    void delete(String apiId);

    List<Movie> getMoviesFromDB(List<WatchlistMovieEntity> watchlistMovieEntities);
}
