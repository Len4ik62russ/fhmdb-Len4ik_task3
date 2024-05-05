package at.ac.fhcampuswien.fhmdb.repository;

import at.ac.fhcampuswien.fhmdb.database.MovieEntity;

import java.util.List;

public interface MovieRepository {
    void save(MovieEntity movieEntity);

    MovieEntity get(String apiId);

    List<MovieEntity> getAll();
}
