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

	private Integer value;

	public UserId(final int value) {
		checkArgument(value > 0, "UserId value must be higher than 0");
		this.value = value;
	}

}
