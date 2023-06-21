package pl.szczesniak.dominik.whattowatch.users.domain.model;

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
public class UserId {

	Integer value;

	public UserId(final int value) {
		this.value = value;
		checkArgument(value > 0, "UserId value must be higher than 0");
	}

}
