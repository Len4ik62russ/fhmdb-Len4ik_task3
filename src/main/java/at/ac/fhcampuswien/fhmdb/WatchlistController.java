package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.database.WatchlistMovieEntity;
import at.ac.fhcampuswien.fhmdb.exception.DatabaseException;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.service.WatchListService;
import at.ac.fhcampuswien.fhmdb.service.impl.WatchListServiceImpl;
import at.ac.fhcampuswien.fhmdb.ui.WatchlistMovieCell;
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

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class WatchlistController implements Initializable {
    @FXML
    private VBox watchlistView;
    @FXML
    private JFXListView movieListView;
    private final JFXButton watchlistBtn = new JFXButton("Delete to Watchlist");
    public final ObservableList<Movie> observableMovies = FXCollections.observableArrayList();
    private WatchListService watchListService;

    public WatchlistController() {
    }

    public WatchlistController(VBox watchlistView) {
        this.watchlistView = watchlistView;
    }

    private final WatchlistMovieCell.ClickEventHandler<Movie> onRemoveClicked = (clickedItem) -> {
        Movie movie = clickedItem;
        deleteMovieFromWatchlist(movie);
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        watchListService = new WatchListServiceImpl();
        List<WatchlistMovieEntity> watchlistMoviesFromDB = getWatchlistMoviesFromDB();

        List<Movie> moviesFromDB = null;
        try {
            moviesFromDB = watchListService.getMoviesFromDB(watchlistMoviesFromDB);
        } catch (DatabaseException e) {
            System.err.println(e.getMessage());
            showErrorDialog("DatabaseException: failed to get movies from Database");
        }
        observableMovies.addAll(moviesFromDB);
        movieListView.setItems(observableMovies);
        movieListView.setCellFactory(movieListView -> new WatchlistMovieCell(onRemoveClicked));
    }

    public void deleteMovieFromWatchlist(Movie movie) {
        try {
            watchListService.delete(movie.getId());
        } catch (DatabaseException e) {
            System.err.println("Error deleting movie from watchlist: " + e.getMessage());
            showErrorDialog("DatabaseException: " + e.getMessage());
        }
        observableMovies.remove(movie);
        WatchlistMovieCell watchlistMovieCell = new WatchlistMovieCell(onRemoveClicked);
        watchlistMovieCell.updateItem(movie, true);
    }

    @FXML
    public void showHomeScreen() throws IOException {
        Stage stage = (Stage) watchlistView.getScene().getWindow();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/at/ac/fhcampuswien/fhmdb/home-view.fxml")));
        stage.setScene(scene);
        stage.show();
    }

    public List<WatchlistMovieEntity> getWatchlistMoviesFromDB() {
        try {
            return watchListService.getAll();
        } catch (DatabaseException e) {
            System.err.println("Error getting watchlist movies from DB: " + e.getMessage());
            showErrorDialog("DatabaseException: " + e.getMessage());
            return null;
        }
    }

    public static void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
