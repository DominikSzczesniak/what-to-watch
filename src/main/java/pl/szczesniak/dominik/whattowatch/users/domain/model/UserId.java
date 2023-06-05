package pl.szczesniak.dominik.whattowatch.users.domain.model;

import lombok.Value;

import static com.google.common.base.Preconditions.checkArgument;

@Value
public class UserId {

	int value;

	public UserId(final int value) {
		this.value = value;
		checkArgument(value > 0, "UserId value must be higher than 0");
	}

}
