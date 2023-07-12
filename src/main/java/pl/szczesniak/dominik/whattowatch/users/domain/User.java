package pl.szczesniak.dominik.whattowatch.users.domain;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserPassword;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;

import static java.util.Objects.requireNonNull;

@Entity
@Table(name = "app_user")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"userId"})
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@AttributeOverride(name = "value", column = @Column(name = "user_id"))
	@Setter(AccessLevel.PACKAGE)
	private Integer userId;

	@AttributeOverride(name = "value", column = @Column(name = "user_name", unique = true))
	private Username username;

	@AttributeOverride(name = "value", column = @Column(name = "password"))
	private UserPassword userPassword;

	public User(final Username username, final UserPassword userPassword) {
		this.username = requireNonNull(username, "Username must not be null.");
		this.userPassword = requireNonNull(userPassword, "UserPassword must not be null.");
	}

	public UserId getUserId() {
		return new UserId(userId);
	}

}
