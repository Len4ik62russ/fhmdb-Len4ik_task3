package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.database.DatabaseManager;
import at.ac.fhcampuswien.fhmdb.database.WatchlistMovieEntity;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.WatchlistMovie;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
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
import java.util.List;
import java.util.ResourceBundle;

public class WatchlistController implements Initializable {
    @FXML
    private VBox watchlistView;
    @FXML
    private JFXListView movieListView;
    private final JFXButton watchlistBtn = new JFXButton("Delete to Watchlist");
    public final ObservableList<WatchlistMovieEntity> observableMovies = FXCollections.observableArrayList();
    private DatabaseManager databaseManager;

    public WatchlistController() {
    }


    public WatchlistController(VBox watchlistView) {
        this.watchlistView = watchlistView;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        databaseManager = DatabaseManager.getDatabaseManager();
        List<WatchlistMovieEntity> watchlistMoviesFromDB = getWatchlistMoviesFromDB();
        observableMovies.addAll(watchlistMoviesFromDB);
        movieListView.setItems(observableMovies);
        //movieListView.setCellFactory(movieListView -> new MovieCell(onRemoveClicked));

    }

    private final MovieCell.ClickEventHandler<WatchlistMovie> onRemoveClicked = (clickedItem) -> {

    };

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


}
