package at.ac.fhcampuswien.fhmdb.service.impl;

import at.ac.fhcampuswien.fhmdb.database.MovieEntity;
import at.ac.fhcampuswien.fhmdb.database.WatchlistMovieEntity;
import at.ac.fhcampuswien.fhmdb.exception.DatabaseException;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.repository.MovieRepository;
import at.ac.fhcampuswien.fhmdb.repository.impl.MovieRepositoryImpl;
import at.ac.fhcampuswien.fhmdb.service.HomeService;
import at.ac.fhcampuswien.fhmdb.service.WatchListService;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class HomeServiceImpl implements HomeService {
    private MovieRepository movieRepository;
    private WatchListService watchListService;

    public HomeServiceImpl() {
        movieRepository = new MovieRepositoryImpl();
        watchListService = new WatchListServiceImpl(this);
    }

    @Override
    public void setMoviesInBD(List<Movie> allMovies) throws DatabaseException {
        for (Movie movie : allMovies) {
            MovieEntity movieEntity = convertMovieToMovieEntity(movie);
            if (isMoviesEntityNotExist(movie.getId())) {
                movieRepository.save(movieEntity);
            }
        }
    }

    @Override
    public Movie getMovie(String apiId) throws DatabaseException {
        return convertMovieEntityToMovie(movieRepository.get(apiId));
    }

    @Override
    public List<Movie> getMoviesFromBD () throws DatabaseException {
        return movieRepository.getAll().stream()
                .map(this::convertMovieEntityToMovie)
                .collect(Collectors.toList());
    }

    @Override
    public void setMovieInWatchlistBD(Movie movie) throws DatabaseException{
        WatchlistMovieEntity watchlistMovieEntity = convertMovieToWatchlistMovieEntity(movie);
        if (isWatchlistMoviesEntityNotExist(movie.getId())) {
            watchListService.save(watchlistMovieEntity);
        } else {
            throw new DatabaseException("Movie already exists in the watchlist");
        }
    }

    public MovieEntity convertMovieToMovieEntity(Movie movie) {
        MovieEntity movieEntity = new MovieEntity();
        movieEntity.setApiId(movie.getId());
        movieEntity.setTitle(movie.getTitle());
        movieEntity.setDescription(movie.getDescription());
        movieEntity.setGenres(movie.getGenres().stream().map(Enum::name).collect(Collectors.joining(", ")));
        movieEntity.setReleaseYear(movie.getReleaseYear());
        movieEntity.setImgUrl(movie.getImgUrl());
        movieEntity.setLengthInMinutes(movie.getLengthInMinutes());
        movieEntity.setRatingFrom(movie.getRating());

        return movieEntity;
    }

    private Movie convertMovieEntityToMovie(MovieEntity movieEntity) {
        String gen = movieEntity.getGenres();
        List<String> genres;
        genres = Arrays.asList(gen.split(", "));
        Movie movie = new Movie(
                String.valueOf(movieEntity.getApiId()),
                movieEntity.getTitle(),
                movieEntity.getDescription(),
                genres.stream().map(Genre::valueOf).collect(Collectors.toList()),
                movieEntity.getReleaseYear(),
                movieEntity.getImgUrl(),
                movieEntity.getLengthInMinutes(),
                movieEntity.getRatingFrom());

        return movie;
    }

    public boolean isMoviesEntityNotExist(String apiId) throws DatabaseException {
        MovieEntity movieEntity = movieRepository.get(apiId);
        return Objects.isNull(movieEntity);
    }

    public WatchlistMovieEntity convertMovieToWatchlistMovieEntity(Movie movie) {
        WatchlistMovieEntity watchlistMovieEntity = new WatchlistMovieEntity();
        watchlistMovieEntity.setApiId(movie.getId());
        return watchlistMovieEntity;
    }

    public boolean isWatchlistMoviesEntityNotExist(String apiId) {
        WatchlistMovieEntity watchlistMovieEntity = watchListService.get(apiId);
        return Objects.isNull(watchlistMovieEntity);
    }

}
