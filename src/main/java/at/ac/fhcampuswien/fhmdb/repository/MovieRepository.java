package at.ac.fhcampuswien.fhmdb.repository;

import at.ac.fhcampuswien.fhmdb.database.MovieEntity;
import at.ac.fhcampuswien.fhmdb.exception.DatabaseException;

import java.util.List;

public interface MovieRepository {
    void save(MovieEntity movieEntity) throws DatabaseException;

    MovieEntity get(String apiId);

    List<MovieEntity> getAll();
}
