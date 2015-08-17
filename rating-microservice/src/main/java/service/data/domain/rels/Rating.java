package service.data.domain.rels;

import service.data.domain.entity.User;
import service.data.domain.entity.Product;
import org.springframework.data.neo4j.annotation.*;

@RelationshipEntity(type = "Rating")
public class Rating {
    @GraphId
    Long id;

    @StartNode
    private User user;

    @EndNode
    private Product product;

    @Indexed
    String knownId;

    Long timestamp;
    Integer rating;

    public Rating() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rating rating1 = (Rating) o;

        if (id != null ? !id.equals(rating1.id) : rating1.id != null) return false;
        if (user != null ? !user.equals(rating1.user) : rating1.user != null) return false;
        if (product != null ? !product.equals(rating1.product) : rating1.product != null) return false;
        if (knownId != null ? !knownId.equals(rating1.knownId) : rating1.knownId != null) return false;
        if (timestamp != null ? !timestamp.equals(rating1.timestamp) : rating1.timestamp != null) return false;
        return !(rating != null ? !rating.equals(rating1.rating) : rating1.rating != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (product != null ? product.hashCode() : 0);
        result = 31 * result + (knownId != null ? knownId.hashCode() : 0);
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        result = 31 * result + (rating != null ? rating.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "id=" + id +
                ", user=" + user +
                ", product=" + product +
                ", knownId='" + knownId + '\'' +
                ", timestamp=" + timestamp +
                ", rating=" + rating +
                '}';
    }
}
