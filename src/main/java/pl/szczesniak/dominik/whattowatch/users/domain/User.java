package pl.szczesniak.dominik.whattowatch.users.domain;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.BaseEntity;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserPassword;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

@Entity
@Table(name = "app_user")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class User extends BaseEntity {

	@AttributeOverride(name = "value", column = @Column(name = "user_name", unique = true))
	private Username username;

	@AttributeOverride(name = "value", column = @Column(name = "password"))
	private UserPassword userPassword;

	@ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinTable
	@ToString.Exclude
	private List<UserRole> roles;

	public User(final Username username, final UserPassword userPassword) {
		this.username = requireNonNull(username, "Username must not be null.");
		this.userPassword = requireNonNull(userPassword, "UserPassword must not be null.");
		this.roles = new ArrayList<>();
	}

	void addRole(final UserRole role) {
		roles.add(role);
	}

	public UserId getUserId() {
		return new UserId(getId());
	}

	public UserPassword getUserPassword() {
		return this.userPassword;
	}

	public List<UserRole> getRoles() {
		return this.roles;
	}

}
