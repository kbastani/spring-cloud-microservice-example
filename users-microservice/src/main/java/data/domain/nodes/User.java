package data.domain.nodes;

import data.domain.rels.Action;
import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.*;

import java.util.Set;

@NodeEntity
public class User {

	@GraphId
	Long id;

	private String firstName;
	private String lastName;
	private String phone;
	private String email;
	@GraphProperty(propertyType = Long.class)
	private Long birthDate;

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
}
