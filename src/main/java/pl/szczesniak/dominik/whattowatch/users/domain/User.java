package pl.szczesniak.dominik.whattowatch.users.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserPassword;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;

import static java.util.Objects.requireNonNull;

@Entity
@Table(name = "app_user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"userId"})
public class User {

	@EmbeddedId
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UserId userId;

	@Embedded
	private Username username;

	@Embedded
	private UserPassword userPassword;

	public User(final Username username, final UserId userId, final UserPassword userPassword) {
		this.username = requireNonNull(username, "Username must not be null.");
		this.userId = requireNonNull(userId, "UserId must not be null.");
		this.userPassword = requireNonNull(userPassword, "UserPassword must not be null.");
	}

	@Override
	public String toString() {
		return "User{" +
				"userId=" + userId +
				", username=" + username +
				'}';
	}
}
