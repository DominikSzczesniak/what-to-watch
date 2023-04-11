package pl.szczesniak.dominik.whattowatch.users.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import static com.google.common.base.Preconditions.checkArgument;

@EqualsAndHashCode
@ToString
public class UserId {

	@Getter
	private final int value;

	public UserId(final int value) {
		checkArgument(value > 0, "UserId value must be higher than 0");
		this.value = value;
	}

}
