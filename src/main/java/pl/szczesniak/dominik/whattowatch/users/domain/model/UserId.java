package pl.szczesniak.dominik.whattowatch.users.domain.model;

import lombok.Getter;

import static com.google.common.base.Preconditions.checkArgument;

public record UserId(@Getter int value) {

	public UserId {
		checkArgument(value > 0, "UserId value must be higher than 0");
	}

}
