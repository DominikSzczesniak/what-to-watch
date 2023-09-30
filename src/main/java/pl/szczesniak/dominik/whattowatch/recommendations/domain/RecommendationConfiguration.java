package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.ConfigurationId;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.GenreId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;
import java.util.Set;

@Entity
@Table
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"configurationId"})
public class RecommendationConfiguration {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@AttributeOverride(name = "value", column = @Column(name = "configuration_id"))
	private Long configurationId;

	@ElementCollection
	@AttributeOverride(name = "value", column = @Column(name = "genre_id"))
	private Set<GenreId> genres;

	@AttributeOverride(name = "value", column = @Column(name = "user_id"))
	private UserId userId;

	RecommendationConfiguration(final Set<GenreId> genres, final UserId userId) {
		this.genres = genres;
		this.userId = userId;
	}

}
