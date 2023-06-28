package pl.szczesniak.dominik.whattowatch.movies.domain;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
public class Movie {

	@EmbeddedId
	@GeneratedValue(strategy = GenerationType.AUTO)
	@AttributeOverride(name = "value", column = @Column(name = "movieid_value"))
	private MovieId movieId;

	@AttributeOverride(name = "value", column = @Column(name = "userid_value"))
	private UserId userId;

	@AttributeOverride(name = "value", column = @Column(name = "movietitle_value"))
	private MovieTitle title;

	Movie(final MovieId movieId, final UserId userId, final MovieTitle title) {
		this.movieId = requireNonNull(movieId, "MovieId cannot be null");
		this.userId = requireNonNull(userId, "UserId cannot be null");
		this.title = requireNonNull(title, "MovieTitle cannot be null");
	}

	public static Movie recreate(final MovieId movieId, final MovieTitle title, final UserId userId) {
		return new Movie(movieId, userId, title);
	}

	WatchedMovie markAsWatched() {
		return new WatchedMovie(movieId, userId, title);
	}

	public void update(final MovieTitle title) {
		this.title = title;
	}

}
