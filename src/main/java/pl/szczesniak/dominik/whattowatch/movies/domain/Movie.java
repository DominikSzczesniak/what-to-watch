package pl.szczesniak.dominik.whattowatch.movies.domain;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
import lombok.Setter;
import lombok.ToString;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.CommentId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieComment;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTitle;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

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
	@AttributeOverride(name = "value", column = @Column(name = "movie_id"))
	@Setter(AccessLevel.PACKAGE)
	private Integer movieId;

	@AttributeOverride(name = "value", column = @Column(name = "user_id"))
	private UserId userId;

	@AttributeOverride(name = "value", column = @Column(name = "movie_title"))
	private MovieTitle title;

	private MovieCover cover;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "movieId")
	private Set<MovieComment> comments;

	Movie(final UserId userId, final MovieTitle title) {
		this.userId = requireNonNull(userId, "UserId cannot be null");
		this.title = requireNonNull(title, "MovieTitle cannot be null");
		comments = new HashSet<>();
	}

	WatchedMovie markAsWatched() {
		return new WatchedMovie(new MovieId(movieId), userId, title);
	}

	public void updateMovieTitle(final MovieTitle title) {
		this.title = title;
	}

	public void updateCover(final MovieCover cover) {
		this.cover = cover;
	}

	public UUID addComment(final String comment) {
		final UUID commentId = UUID.randomUUID();
		this.comments.add(new MovieComment(new CommentId(commentId), movieId, comment));
		return commentId;
	}

	public void deleteComment(final CommentId commentId) {
		comments.removeIf(movieComment -> movieComment.getCommentId().equals(commentId));
	}

	public MovieId getMovieId() {
		return new MovieId(movieId);
	}

	public Optional<MovieCover> getCover() {
		return Optional.ofNullable(cover);
	}

}
