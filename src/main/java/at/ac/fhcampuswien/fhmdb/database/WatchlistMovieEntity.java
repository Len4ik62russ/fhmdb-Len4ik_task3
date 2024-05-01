package at.ac.fhcampuswien.fhmdb.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "watchlist_movies")
public class WatchlistMovieEntity {

    @DatabaseField(id = true)
    private long id;

    @DatabaseField
    private String apiId;

    public WatchlistMovieEntity() {
    }

    public WatchlistMovieEntity(String apiId) {
        this.apiId = apiId;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

}
