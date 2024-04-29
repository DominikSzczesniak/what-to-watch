package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.BaseEntity;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.ConfigurationId;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenre;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.Set;

@Entity
@Table
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class RecommendationConfiguration extends BaseEntity {

	@ElementCollection(fetch = FetchType.EAGER, targetClass = MovieGenre.class)
	@Enumerated(EnumType.STRING)
	@CollectionTable(name = "configuration_genres", joinColumns = @JoinColumn(name = "configuration_id"))
	@Column(name = "genre")
	private Set<MovieGenre> genres;

	@AttributeOverride(name = "value", column = @Column(name = "user_id", unique = true))
	private UserId userId;

	RecommendationConfiguration(@NonNull final Set<MovieGenre> genres, @NonNull final UserId userId) {
		this.genres = genres;
		this.userId = userId;
	}

	void update(final Set<MovieGenre> genres) {
		this.genres = genres;
	}

	ConfigurationId getConfigurationId() {
		return new ConfigurationId(getId());
	}

}
