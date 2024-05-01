package at.ac.fhcampuswien.fhmdb.database;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

@DatabaseTable(tableName = "movies")
public class MovieEntity {

    @DatabaseField(generatedId = true)
    private long id;
    @DatabaseField
    private String apiId;
    @DatabaseField
    private String title;
    @DatabaseField
    String description;
    @DatabaseField
    private String genres;
    @DatabaseField
    int releaseYear;
    @DatabaseField
    String imgUrl;
    @DatabaseField
    int lengthInMinutes;
    @DatabaseField
    double ratingFrom;

    public MovieEntity() {
    }

    public MovieEntity(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public MovieEntity(long id, String apiId, String title, String genres, String description, int releaseYear, String imgUrl, int lengthInMinutes, double ratingFrom) {
        this.id = id;
        this.apiId = apiId;
        this.title = title;
        this.genres = genres;
        this.description = description;
        this.releaseYear = releaseYear;
        this.imgUrl = imgUrl;
        this.lengthInMinutes = lengthInMinutes;
        this.ratingFrom = ratingFrom;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getLengthInMinutes() {
        return lengthInMinutes;
    }

    public void setLengthInMinutes(int lengthInMinutes) {
        this.lengthInMinutes = lengthInMinutes;
    }

    public double getRatingFrom() {
        return ratingFrom;
    }

    public void setRatingFrom(double ratingFrom) {
        this.ratingFrom = ratingFrom;
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

}
