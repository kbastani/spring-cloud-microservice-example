package service.data.domain.entity;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.*;

import java.io.Serializable;
import java.util.Set;

@NodeEntity
public class User implements Serializable {
    
	private static final long serialVersionUID = -100759321281659379L;

	@GraphId
	Long id;

	private String age;
	private String gender;
	private String occupation;
	private String zipcode;

	public User() { }

	@Fetch
	@RelatedTo(type = "FOLLOWS", direction = Direction.OUTGOING, elementClass = User.class)
	Set<User> follows;

	@Fetch
	@RelatedTo(type = "FOLLOWS", direction = Direction.INCOMING, elementClass = User.class)
	Set<User> followers;

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public Long getId() {
		return id;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        if (age != null ? !age.equals(user.age) : user.age != null) return false;
        if (gender != null ? !gender.equals(user.gender) : user.gender != null) return false;
        if (occupation != null ? !occupation.equals(user.occupation) : user.occupation != null) return false;
        if (zipcode != null ? !zipcode.equals(user.zipcode) : user.zipcode != null) return false;
        if (follows != null ? !follows.equals(user.follows) : user.follows != null) return false;
        return !(followers != null ? !followers.equals(user.followers) : user.followers != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (age != null ? age.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + (occupation != null ? occupation.hashCode() : 0);
        result = 31 * result + (zipcode != null ? zipcode.hashCode() : 0);
        result = 31 * result + (follows != null ? follows.hashCode() : 0);
        result = 31 * result + (followers != null ? followers.hashCode() : 0);
        return result;
    }
}
