package data.domain.nodes;

import data.domain.rels.Action;
import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.*;

import java.io.Serializable;
import java.util.Random;
import java.util.Set;

@NodeEntity
public class User implements Serializable {

	@GraphId
	Long id;

	private String firstName;
	private String lastName;
	private String phone;
	private String email;
	@GraphProperty(propertyType = Long.class)
	private Long birthDate;

	public User() {

	}

	@Fetch
	@RelatedTo(type = "FOLLOWS", direction = Direction.OUTGOING, elementClass = User.class)
	Set<User> follows;

	@Fetch
	@RelatedTo(type = "FOLLOWS", direction = Direction.INCOMING, elementClass = User.class)
	Set<User> followers;

	@RelatedToVia(type = "ACTION", direction = Direction.OUTGOING)
	Set<Action> actions;

	@RelatedTo(type = "ACTION", direction = Direction.OUTGOING)
	Set<Event> events;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Long birthDate) {
		this.birthDate = birthDate;
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
		if (firstName != null ? !firstName.equals(user.firstName) : user.firstName != null) return false;
		if (lastName != null ? !lastName.equals(user.lastName) : user.lastName != null) return false;
		if (phone != null ? !phone.equals(user.phone) : user.phone != null) return false;
		if (email != null ? !email.equals(user.email) : user.email != null) return false;
		if (birthDate != null ? !birthDate.equals(user.birthDate) : user.birthDate != null) return false;
		if (follows != null ? !follows.equals(user.follows) : user.follows != null) return false;
		if (followers != null ? !followers.equals(user.followers) : user.followers != null) return false;
		if (actions != null ? !actions.equals(user.actions) : user.actions != null) return false;
		return !(events != null ? !events.equals(user.events) : user.events != null);

	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
		result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
		result = 31 * result + (phone != null ? phone.hashCode() : 0);
		result = 31 * result + (email != null ? email.hashCode() : 0);
		result = 31 * result + (birthDate != null ? birthDate.hashCode() : 0);
		result = 31 * result + (follows != null ? follows.hashCode() : 0);
		result = 31 * result + (followers != null ? followers.hashCode() : 0);
		result = 31 * result + (actions != null ? actions.hashCode() : 0);
		result = 31 * result + (events != null ? events.hashCode() : 0);
		return result;
	}
}
