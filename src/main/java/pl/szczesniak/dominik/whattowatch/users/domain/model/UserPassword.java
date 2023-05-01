package pl.szczesniak.dominik.whattowatch.users.domain.model;

import lombok.NonNull;
import lombok.Value;

import static com.google.common.base.Preconditions.checkArgument;

@Value
public class UserPassword {

	@NonNull
	String value;

	public UserPassword(final String value) {
		checkArgument(value != null, "Must contain password");
		checkArgument(value.trim().length() > 2, "Password must have at least 3 characters");
		this.value = value;
	}

}
