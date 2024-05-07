package at.ac.fhcampuswien.fhmdb.service.impl;

import at.ac.fhcampuswien.fhmdb.database.WatchlistMovieEntity;
import at.ac.fhcampuswien.fhmdb.exception.DatabaseException;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.repository.WatchListMovieRepository;
import at.ac.fhcampuswien.fhmdb.repository.impl.WatchListMovieRepositoryImpl;
import at.ac.fhcampuswien.fhmdb.service.HomeService;
import at.ac.fhcampuswien.fhmdb.service.WatchListService;

import java.util.ArrayList;
import java.util.List;

public class WatchListServiceImpl implements WatchListService {
    private WatchListMovieRepository watchListMovieRepository;
    private HomeService homeService;

    public WatchListServiceImpl() {
        homeService = new HomeServiceImpl();
        watchListMovieRepository = new WatchListMovieRepositoryImpl();
    }

    public WatchListServiceImpl(HomeServiceImpl homeService) {
        watchListMovieRepository = new WatchListMovieRepositoryImpl();
        this.homeService = homeService;
    }

    @Override
    public void save(WatchlistMovieEntity watchlistMovieEntity) {
        watchListMovieRepository.save(watchlistMovieEntity);
    }

    @Override
    public WatchlistMovieEntity get(String apiId) {
        return watchListMovieRepository.get(apiId);
    }

    @Override
    public List<WatchlistMovieEntity> getAll() {
        return watchListMovieRepository.getAll();
    }

    @Override
    public void delete(String apiId) throws DatabaseException {
        watchListMovieRepository.delete(apiId);
    }

    @Override
    public List<Movie> getMoviesFromDB(List<WatchlistMovieEntity> watchlistMovieEntities) throws DatabaseException {
        List<Movie> result = new ArrayList<>();
        for (WatchlistMovieEntity watchlistMovieEntity : watchlistMovieEntities) {
            Movie movie = homeService.getMovie(watchlistMovieEntity.getApiId());
            result.add(movie);
        }
        return result;
    }

}
