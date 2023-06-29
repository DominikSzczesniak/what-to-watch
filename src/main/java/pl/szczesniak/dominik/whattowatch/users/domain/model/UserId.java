package pl.szczesniak.dominik.whattowatch.users.domain.model;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static com.google.common.base.Preconditions.checkArgument;

@Embeddable
@Getter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class UserId {

	private Long value;

	public UserId(final Long value) {
		checkArgument(value > 0L, "UserId value must be higher than 0");
		this.value = value;
	}

}
