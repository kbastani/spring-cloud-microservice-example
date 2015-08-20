package service.models;

public class Movie {
    private Long id;
    private String title;
    private String url;
    private Genre[] genres;
    private String knownId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Genre[] getGenres() {
        return genres;
    }

    public void setGenres(Genre[] genres) {
        this.genres = genres;
    }

    public String getKnownId() {
        return knownId;
    }

    public void setKnownId(String knownId) {
        this.knownId = knownId;
    }
}
