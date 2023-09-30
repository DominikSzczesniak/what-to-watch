package pl.szczesniak.dominik.whattowatch.recommendations.domain.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@Embeddable
@Getter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ConfigurationId {

	private Long value;

	public ConfigurationId(@NonNull final Long value) {
		this.value = value;
	}

}
