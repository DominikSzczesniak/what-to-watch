package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.ConfigurationId;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenre;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

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
	@Setter(AccessLevel.PACKAGE)
	private Long configurationId;

	@ElementCollection(targetClass = MovieGenre.class)
	@Enumerated(EnumType.STRING)
	@CollectionTable(name = "configuration_genres", joinColumns = @JoinColumn(name = "configuration_id"))
	@Column(name = "genre")
	private Set<MovieGenre> genres;

	@AttributeOverride(name = "value", column = @Column(name = "user_id"))
	private UserId userId;

	RecommendationConfiguration(@NonNull final Set<MovieGenre> genres, @NonNull final UserId userId) {
		this.genres = genres;
		this.userId = userId;
	}

	void update(final Set<MovieGenre> genres) {
		this.genres = genres;
	}

	public ConfigurationId getConfigurationId() {
		return new ConfigurationId(configurationId);
	}

}
