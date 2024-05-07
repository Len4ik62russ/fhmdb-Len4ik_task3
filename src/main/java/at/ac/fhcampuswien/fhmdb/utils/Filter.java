package at.ac.fhcampuswien.fhmdb.utils;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;

import java.util.ArrayList;
import java.util.List;

public class Filter {
    public static List<Movie> applyAllFilters(List<Movie> filteredMovies, String searchQuery, Object genre, String releaseYear, String ratingFrom) {
        List<Movie> movies = new ArrayList<>();
        if (!searchQuery.isEmpty()) {
            filteredMovies = filterByQuery(filteredMovies, searchQuery);
        }
        if (genre != null && !genre.toString().equals("No filter")) {
            filteredMovies = filterByGenre(filteredMovies, Genre.valueOf(genre.toString()));
        }
        if (releaseYear != null && !releaseYear.isEmpty()) {
            int year = Integer.parseInt(releaseYear);
            filteredMovies = filterByYear(filteredMovies, year);
        }
        if (ratingFrom != null && !ratingFrom.isEmpty()) {
            double rating = Double.parseDouble(ratingFrom);
            filteredMovies = filterByRatingFrom(filteredMovies, rating);
        }
        movies.addAll(filteredMovies);
        return movies;
    }

    private static List<Movie> filterByQuery(List<Movie> movies, String query) {
        if (query == null || query.isEmpty()) {
            return movies;
        }

        if (movies == null) {
            throw new IllegalArgumentException("movies must not be null");
        }
        return movies.stream()
                .filter(movie -> isTitleOrDescriptionContainsQuery(movie, query))
                .toList();
    }

    private static boolean isTitleOrDescriptionContainsQuery(Movie movie, String query) {
        return movie.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                movie.getDescription().toLowerCase().contains(query.toLowerCase());
    }

    private static List<Movie> filterByGenre(List<Movie> movies, Genre genre) {
        if (genre == null) return movies;

        if (movies == null) {
            throw new IllegalArgumentException("movies must not be null");
        }

        return movies.stream().filter(movie -> movie.getGenres().contains(genre)).toList();
    }

    private static List<Movie> filterByYear(List<Movie> movies, int releaseYear) {
        return movies.stream()
                .filter(movie -> movie.getReleaseYear() == releaseYear)
                .toList();
    }

    private static List<Movie> filterByRatingFrom(List<Movie> movies, double ratingFrom) {
        return movies.stream()
                .filter(movie -> movie.getRating() == ratingFrom)
                .toList();
    }


}
