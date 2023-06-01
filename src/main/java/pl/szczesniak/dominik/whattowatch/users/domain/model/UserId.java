package pl.szczesniak.dominik.whattowatch.users.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Value;

import static com.google.common.base.Preconditions.checkArgument;

@Value
public class UserId {

	@Getter
	int value;

	@JsonCreator
	public UserId(@JsonProperty("value") final int value) {
		this.value = value;
		checkArgument(value > 0, "UserId value must be higher than 0");
	}

}
