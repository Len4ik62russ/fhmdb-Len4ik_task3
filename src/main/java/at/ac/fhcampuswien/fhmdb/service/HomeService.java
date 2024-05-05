package at.ac.fhcampuswien.fhmdb.service;

import at.ac.fhcampuswien.fhmdb.database.WatchlistMovieEntity;
import at.ac.fhcampuswien.fhmdb.models.Movie;

import java.util.List;

public interface HomeService {
    void setMoviesInBD(List<Movie> allMovies);

    Movie getMovie(String apiId);

    List<Movie> getMoviesFromBD();

    void setMovieInWatchlistBD(Movie movie);
}
