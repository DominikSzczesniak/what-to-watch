package pl.szczesniak.dominik.whattowatch.movies.domain;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTitle;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import static java.util.Objects.requireNonNull;

@Entity
@Getter
@Table
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"movieId"})
public class WatchedMovie {

	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "movieid_value"))
	private MovieId movieId;

	@AttributeOverride(name = "value", column = @Column(name = "userid_value"))
	private UserId userId;

	@AttributeOverride(name = "value", column = @Column(name = "movietitle_value"))
	private MovieTitle title;

	WatchedMovie(final MovieId movieId, final UserId userId, final MovieTitle title) {
		this.movieId = requireNonNull(movieId, "MovieId cannot be null");
		this.userId = requireNonNull(userId, "UserId cannot be null");
		this.title = requireNonNull(title, "MovieTitle cannot be null");
	}

}
