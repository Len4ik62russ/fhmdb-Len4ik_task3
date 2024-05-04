package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.api.MovieAPI;
import at.ac.fhcampuswien.fhmdb.database.DatabaseManager;
import at.ac.fhcampuswien.fhmdb.database.MovieEntity;
import at.ac.fhcampuswien.fhmdb.database.WatchlistMovieEntity;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.SortedState;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;

import java.io.IOException;
import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HomeController implements Initializable {
    @FXML
    public JFXButton searchBtn;

    @FXML
    public TextField searchField;

    @FXML
    public JFXListView movieListView;

    @FXML
    public JFXComboBox genreComboBox;

    @FXML
    public JFXComboBox releaseYearComboBox;

    @FXML
    public JFXComboBox ratingFromComboBox;

    @FXML
    public JFXButton sortBtn;

    private DatabaseManager databaseManager;

    public List<Movie> allMovies;

    protected ObservableList<Movie> observableMovies = FXCollections.observableArrayList();

    protected SortedState sortedState;
    private final MovieCell.ClickEventHandler<Movie> onAddToWatchlistClicked = (clickedItem) -> {
        Movie movie = clickedItem;
        databaseManager = DatabaseManager.getDatabaseManager();
        setMovieInWatchlistBD(movie);
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        databaseManager = DatabaseManager.getDatabaseManager();
        initializeState();
        initializeLayout();
    }

    public void initializeState() {
        List<Movie> result = MovieAPI.getAllMovies();
        if (result.isEmpty()) {
            try {
                result = databaseManager.getMovieDao().queryForAll().stream()
                        .map(this:: convertMovieEntityToMovie)
                        .collect(Collectors.toList());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        setMovies(result);
        setMovieList(result);
        setMoviesInBD(result);
        sortedState = SortedState.NONE;

        // test stream methods
        System.out.println("getMostPopularActor");
        System.out.println(getMostPopularActor(allMovies));

        System.out.println("getLongestMovieTitle");
        System.out.println(getLongestMovieTitle(allMovies));

        System.out.println("count movies from Zemeckis");
        System.out.println(countMoviesFrom(allMovies, "Robert Zemeckis"));

        System.out.println("count movies from Steven Spielberg");
        System.out.println(countMoviesFrom(allMovies, "Steven Spielberg"));

        System.out.println("getMoviewsBetweenYears");
        List<Movie> between = getMoviesBetweenYears(allMovies, 1994, 2000);
        System.out.println(between.size());
        System.out.println(between.stream().map(Objects::toString).collect(Collectors.joining(", ")));
    }



    public void initializeLayout() {
        movieListView.setItems(observableMovies);   // set the items of the listview to the observable list
        movieListView.setCellFactory(movieListView -> new MovieCell(onAddToWatchlistClicked)); // apply custom cells to the listview

        // genre combobox
        Object[] genres = Genre.values();   // get all genres
        genreComboBox.getItems().add("No filter");  // add "no filter" to the combobox
        genreComboBox.getItems().addAll(genres);    // add all genres to the combobox
        genreComboBox.setPromptText("Filter by Genre");

        // year combobox
        releaseYearComboBox.getItems().add("No filter");  // add "no filter" to the combobox
        // fill array with numbers from 1900 to 2023
        Integer[] years = new Integer[124];
        for (int i = 0; i < years.length; i++) {
            years[i] = 1900 + i;
        }
        releaseYearComboBox.getItems().addAll(years);    // add all years to the combobox
        releaseYearComboBox.setPromptText("Filter by Release Year");

        // rating combobox
        ratingFromComboBox.getItems().add("No filter");  // add "no filter" to the combobox
        // fill array with numbers from 0 to 10
        Integer[] ratings = new Integer[11];
        for (int i = 0; i < ratings.length; i++) {
            ratings[i] = i;
        }
        ratingFromComboBox.getItems().addAll(ratings);    // add all ratings to the combobox
        ratingFromComboBox.setPromptText("Filter by Rating");
    }
    private Movie convertMovieEntityToMovie(MovieEntity movieEntity) {
        String gen = movieEntity.getGenres();
        List<String> genres;
        genres = Arrays.asList(gen.split(", "));
        Movie movie = new Movie(
                String.valueOf(movieEntity.getId()),
                movieEntity.getTitle(),
                movieEntity.getDescription(),
                genres.stream().map(Genre::valueOf).collect(Collectors.toList()),
                movieEntity.getReleaseYear(),
                movieEntity.getImgUrl(),
                movieEntity.getLengthInMinutes(),
                movieEntity.getRatingFrom());

        return movie;
    }
    public void setMovies(List<Movie> movies) {
        allMovies = movies;
    }

    public void setMovieList(List<Movie> movies) {
        observableMovies.clear();
        observableMovies.addAll(movies);
    }

    public void setMoviesInBD(List<Movie> allMovies) {
        Dao<MovieEntity, Long> movieDao = databaseManager.getMovieDao();
        try {
            for (Movie movie : allMovies) {
                MovieEntity movieEntity = convertMovieToMovieEntity(movie);
                if (isMoviesEntityNotExist(movie.getId())) {
                    movieDao.create(movieEntity);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            databaseManager.closeConnectionSource();
        }
    }


    public MovieEntity convertMovieToMovieEntity(Movie movie) {
        MovieEntity movieEntity = new MovieEntity();
        movieEntity.setApiId(movie.getId());
        // дописать методы сохранения значений полей из Movie в MovieEntity
        movieEntity.setTitle(movie.getTitle());
        movieEntity.setDescription(movie.getDescription());
        movieEntity.setGenres(movie.getGenres().stream().map(Enum::name).collect(Collectors.joining(", ")));
        movieEntity.setReleaseYear(movie.getReleaseYear());
        movieEntity.setImgUrl(movie.getImgUrl());
        movieEntity.setLengthInMinutes(movie.getLengthInMinutes());
        movieEntity.setRatingFrom(movie.getRating());


        return movieEntity;
    }


    public boolean isMoviesEntityNotExist(String apiId) {
        List<MovieEntity> result = new ArrayList<>();
        try {
            QueryBuilder<MovieEntity, Long> queryBuilder = databaseManager.getMovieDao().queryBuilder();

            // Добавляем условие поиска по полю title
            queryBuilder.where().like("APIID", "%" + apiId + "%"); // поиск подстроки

            // Получаем результат запроса
            result = queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result.isEmpty();
    }


    public void setMovieInWatchlistBD(Movie movie) {
        Dao<WatchlistMovieEntity, Long> watchlistMovieDao = databaseManager.getWatchlistMovieDao();
        try {

            WatchlistMovieEntity watchlistMovieEntity = convertMovieToWatchlistMovieEntity(movie);
            if (isWatchlistMoviesEntityNotExist(movie.getId())) {
                watchlistMovieDao.create(watchlistMovieEntity);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            databaseManager.closeConnectionSource();
        }
    }

    public WatchlistMovieEntity convertMovieToWatchlistMovieEntity(Movie movie) {
        WatchlistMovieEntity watchlistMovieEntity = new WatchlistMovieEntity();
        watchlistMovieEntity.setApiId(movie.getId());
        return watchlistMovieEntity;
    }

    public boolean isWatchlistMoviesEntityNotExist(String apiId) {
        List<WatchlistMovieEntity> result = new ArrayList<>();
        try {
            QueryBuilder<WatchlistMovieEntity, Long> queryBuilder = databaseManager.getWatchlistMovieDao().queryBuilder();

            // Добавляем условие поиска по полю title
            queryBuilder.where().like("APIID", "%" + apiId + "%"); // поиск подстроки

            // Получаем результат запроса
            result = queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result.isEmpty();
    }


    public void sortMovies() {
        if (sortedState == SortedState.NONE || sortedState == SortedState.DESCENDING) {
            sortMovies(SortedState.ASCENDING);
        } else if (sortedState == SortedState.ASCENDING) {
            sortMovies(SortedState.DESCENDING);
        }
    }

    // sort movies based on sortedState
// by default sorted state is NONE
// afterwards it switches between ascending and descending
    public void sortMovies(SortedState sortDirection) {
        if (sortDirection == SortedState.ASCENDING) {
            observableMovies.sort(Comparator.comparing(Movie::getTitle));
            sortedState = SortedState.ASCENDING;
        } else {
            observableMovies.sort(Comparator.comparing(Movie::getTitle).reversed());
            sortedState = SortedState.DESCENDING;
        }
    }

    public List<Movie> filterByQuery(List<Movie> movies, String query) {
        if (query == null || query.isEmpty()) return movies;

        if (movies == null) {
            throw new IllegalArgumentException("movies must not be null");
        }

        return movies.stream().filter(movie ->
                        movie.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                                movie.getDescription().toLowerCase().contains(query.toLowerCase()))
                .toList();
    }

    public List<Movie> filterByGenre(List<Movie> movies, Genre genre) {
        if (genre == null) return movies;

        if (movies == null) {
            throw new IllegalArgumentException("movies must not be null");
        }

        return movies.stream().filter(movie -> movie.getGenres().contains(genre)).toList();
    }

    public void applyAllFilters(String searchQuery, Object genre) {
        List<Movie> filteredMovies = allMovies;

        if (!searchQuery.isEmpty()) {
            filteredMovies = filterByQuery(filteredMovies, searchQuery);
        }

        if (genre != null && !genre.toString().equals("No filter")) {
            filteredMovies = filterByGenre(filteredMovies, Genre.valueOf(genre.toString()));
        }

        observableMovies.clear();
        observableMovies.addAll(filteredMovies);
    }

    public void searchBtnClicked(ActionEvent actionEvent) {
        String searchQuery = searchField.getText().trim().toLowerCase();
        String releaseYear = validateComboboxValue(releaseYearComboBox.getSelectionModel().getSelectedItem());
        String ratingFrom = validateComboboxValue(ratingFromComboBox.getSelectionModel().getSelectedItem());
        String genreValue = validateComboboxValue(genreComboBox.getSelectionModel().getSelectedItem());

        Genre genre = null;
        if (genreValue != null) {
            genre = Genre.valueOf(genreValue);
        }

        List<Movie> movies = getMovies(searchQuery, genre, releaseYear, ratingFrom);
        setMovies(movies);
        setMovieList(movies);
        // applyAllFilters(searchQuery, genre);

        sortMovies(sortedState);
    }

    public String validateComboboxValue(Object value) {
        if (value != null && !value.toString().equals("No filter")) {
            return value.toString();
        }
        return null;
    }

    public List<Movie> getMovies(String searchQuery, Genre genre, String releaseYear, String ratingFrom) {
        return MovieAPI.getAllMovies(searchQuery, genre, releaseYear, ratingFrom);
    }

    public void sortBtnClicked(ActionEvent actionEvent) {
        sortMovies();
    }

    // count which actor is in the most movies
    public String getMostPopularActor(List<Movie> movies) {
        String actor = movies.stream()
                .flatMap(movie -> movie.getMainCast().stream())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("");

        return actor;
    }

    public int getLongestMovieTitle(List<Movie> movies) {
        return movies.stream()
                .mapToInt(movie -> movie.getTitle().length())
                .max()
                .orElse(0);
    }

    public long countMoviesFrom(List<Movie> movies, String director) {
        return movies.stream()
                .filter(movie -> movie.getDirectors().contains(director))
                .count();
    }

    public List<Movie> getMoviesBetweenYears(List<Movie> movies, int startYear, int endYear) {
        return movies.stream()
                .filter(movie -> movie.getReleaseYear() >= startYear && movie.getReleaseYear() <= endYear)
                .collect(Collectors.toList());
    }

    @FXML
    public void showWatchlistScreen(ActionEvent event) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/at/ac/fhcampuswien/fhmdb/watchlist-view.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<WatchlistMovieEntity> getWatchlistMoviesFromDB() {
        List<WatchlistMovieEntity> result = new ArrayList<>();
        try {
            QueryBuilder<WatchlistMovieEntity, Long> queryBuilder = databaseManager.getWatchlistMovieDao().queryBuilder();

            // Получаем результат запроса
            result = queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}