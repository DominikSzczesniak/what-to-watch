package pl.szczesniak.dominik.whattowatch.users.domain.model;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import static com.google.common.base.Preconditions.checkArgument;

@Embeddable
@Getter
@ToString
@RequiredArgsConstructor
@EqualsAndHashCode
public class Username {

	@Column(name = "username_value")
	String value;

	public Username(final String value) {
		checkArgument(value != null, "Must contatin username");
		checkArgument(usernameContainsOnlyLetters(value), "Username can contain only letters");
		checkArgument(value.trim().length() > 1 && value.trim().length() < 26, "Username must be 1-25 letters long");
		this.value = value;
	}

	private boolean usernameContainsOnlyLetters(String name) {
		return name.matches("(?i)[a-z]([- ',.a-z]{0,23}[a-z])?");
	}

}
