package pl.szczesniak.dominik.whattowatch.users.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserPassword;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;

import static java.util.Objects.requireNonNull;

@Entity
@Table(name = "app_user")
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"userId"})
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer userId;

	private String userName;

	private String userPassword;

	public User(final Username username, final UserId userId, final UserPassword userPassword) {
		this.userName = requireNonNull(username, "Username must not be null.").getValue();
		this.userId = requireNonNull(userId, "UserId must not be null.").getValue();
		this.userPassword = requireNonNull(userPassword, "UserPassword must not be null.").getValue();
	}

	public Username getUserName() {
		return new Username(userName);
	}

	public UserId getUserId() {
		return new UserId(userId);
	}

}
