package at.ac.fhcampuswien.fhmdb.models;


public class WatchlistMovie {
    private final long id;

    private final String apiId;

    public WatchlistMovie(long id, String apiId) {
        this.id = id;
        this.apiId = apiId;
    }

    public long getId() {
        return id;
    }

    public String getApiId() {
        return apiId;
    }

    @Override
    public String toString() {
        return this.apiId;
    }
}
