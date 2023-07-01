package pl.szczesniak.dominik.whattowatch.users.domain.model;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static com.google.common.base.Preconditions.checkArgument;

@Embeddable
@Getter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class UserPassword {

	private String value;

	public UserPassword(final String value) {
		checkArgument(value != null, "Must contain password");
		checkArgument(value.trim().length() > 2, "Password must have at least 3 characters");
		this.value = value;
	}

}