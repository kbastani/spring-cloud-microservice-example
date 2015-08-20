package service.models;

public class Product {
    private Long id;
    private String knownId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKnownId() {
        return knownId;
    }

    public void setKnownId(String knownId) {
        this.knownId = knownId;
    }
}
