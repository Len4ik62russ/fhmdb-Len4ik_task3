package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.database.DatabaseManager;
import at.ac.fhcampuswien.fhmdb.database.MovieEntity;
import at.ac.fhcampuswien.fhmdb.database.WatchlistMovieEntity;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.WatchlistMovie;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import at.ac.fhcampuswien.fhmdb.ui.WatchlistMovieCell;
import com.j256.ormlite.stmt.QueryBuilder;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class WatchlistController implements Initializable {
    @FXML
    private VBox watchlistView;
    @FXML
    private JFXListView movieListView;
    private final JFXButton watchlistBtn = new JFXButton("Delete to Watchlist");
    public final ObservableList<Movie> observableMovies = FXCollections.observableArrayList();
    private DatabaseManager databaseManager;

    public WatchlistController() {
    }


    public WatchlistController(VBox watchlistView) {
        this.watchlistView = watchlistView;
    }
    private final WatchlistMovieCell.ClickEventHandler<Movie> onRemoveClicked = (clickedItem) -> {
        Movie movie = clickedItem;
        databaseManager = DatabaseManager.getDatabaseManager();
        deleteMovieFromWatchlist(movie);
    };
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        databaseManager = DatabaseManager.getDatabaseManager();
        List<WatchlistMovieEntity> watchlistMoviesFromDB = getWatchlistMoviesFromDB();
        List<Movie> moviesFromDB = getMoviesFromDB(watchlistMoviesFromDB);
        observableMovies.addAll(moviesFromDB);
        movieListView.setItems(observableMovies);
        movieListView.setCellFactory(movieListView -> new WatchlistMovieCell(onRemoveClicked));

    }

   public void deleteMovieFromWatchlist(Movie movie) {

       try {
           List<WatchlistMovieEntity> result = new ArrayList<>();
           QueryBuilder<WatchlistMovieEntity, Long> queryBuilder = databaseManager.getWatchlistMovieDao().queryBuilder();

           // Добавляем условие поиска по полю
           queryBuilder.where().like("APIID", "%" + movie.getId() + "%"); // поиск подстроки

           // Получаем результат запроса
           result = queryBuilder.query();
           databaseManager.getWatchlistMovieDao().delete(result);
              observableMovies.remove(movie);
              WatchlistMovieCell watchlistMovieCell = new WatchlistMovieCell(onRemoveClicked);
                watchlistMovieCell.updateItem(movie, true);


       } catch (SQLException e) {
           throw new RuntimeException(e);
       }
   }
       @FXML
    public void showHomeScreen() throws IOException {
        Stage stage = (Stage) watchlistView.getScene().getWindow();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/at/ac/fhcampuswien/fhmdb/home-view.fxml")));
        stage.setScene(scene);
        stage.show();

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

    public List<Movie> getMoviesFromDB(List<WatchlistMovieEntity> watchlistMovieEntities) {
        List<Movie> result = new ArrayList<>();
        for (WatchlistMovieEntity watchlistMovieEntity : watchlistMovieEntities) {
            MovieEntity movieEntity = new MovieEntity();
            try {
                QueryBuilder<MovieEntity, Long> queryBuilder = databaseManager.getMovieDao().queryBuilder();

                queryBuilder.where().like("APIID", "%" + watchlistMovieEntity.getApiId() + "%"); // поиск подстроки
                // Получаем результат запроса
                movieEntity = queryBuilder.query().get(0);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Movie movie = convertMovieEntityToMovie(movieEntity);
            result.add(movie);
        }
        return result;
    }

    public Movie convertMovieEntityToMovie(MovieEntity movieEntity) {
        String gen = movieEntity.getGenres();
        List<String> genres;
        genres = Arrays.asList(gen.split(", "));
        Movie movie = new Movie(
                String.valueOf(movieEntity.getId()),
                movieEntity.getTitle(),
                movieEntity.getDescription(),
                // TODO дописать конвертацию жанров
                genres.stream().map(Genre::valueOf).collect(Collectors.toList()),



                movieEntity.getReleaseYear(),
                movieEntity.getImgUrl(),
                movieEntity.getLengthInMinutes(),
                movieEntity.getRatingFrom());

        return movie;
    }

}
