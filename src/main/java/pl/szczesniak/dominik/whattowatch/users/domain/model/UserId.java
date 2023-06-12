package pl.szczesniak.dominik.whattowatch.users.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkArgument;

@Getter
@ToString
@RequiredArgsConstructor
@EqualsAndHashCode
public class UserId implements Serializable {

	int value;

	public UserId(final int value) {
		this.value = value;
		checkArgument(value > 0, "UserId value must be higher than 0");
	}

}
