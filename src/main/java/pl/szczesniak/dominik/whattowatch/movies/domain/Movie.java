package pl.szczesniak.dominik.whattowatch.movies.domain;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTitle;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Entity
@Getter
@Table
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"movieId"})
public class Movie {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@AttributeOverride(name = "value", column = @Column(name = "movieid_value"))
	@Setter(AccessLevel.PACKAGE)
	private Integer movieId;

	@AttributeOverride(name = "value", column = @Column(name = "userid_value"))
	private UserId userId;

	@AttributeOverride(name = "value", column = @Column(name = "movietitle_value"))
	private MovieTitle title;

	private MovieCover cover;

	Movie(final UserId userId, final MovieTitle title) {
		this.userId = requireNonNull(userId, "UserId cannot be null");
		this.title = requireNonNull(title, "MovieTitle cannot be null");
	}

	WatchedMovie markAsWatched() {
		return new WatchedMovie(new MovieId(movieId), userId, title);
	}

	public void update(final MovieTitle title) {
		this.title = title;
	}

	public void updateCover(final MovieCover cover) {
		this.cover = cover;
	}

	public MovieId getMovieId() {
		return new MovieId(movieId);
	}

	public Optional<MovieCover> getCover() {
		return Optional.ofNullable(cover);
	}

}
