package pl.szczesniak.dominik.whattowatch.movies.domain;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.BaseEntity;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.CommentId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTagId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTagLabel;
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
class Movie extends BaseEntity {

	@AttributeOverride(name = "value", column = @Column(name = "user_id"))
	private UserId userId;

	@AttributeOverride(name = "value", column = @Column(name = "movie_title"))
	private MovieTitle title;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "cover_id")
	private MovieCover cover;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "movieId")
	private Set<MovieComment> comments;

	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinTable
	@ToString.Exclude
	private Set<MovieTag> tags;

	Movie(final UserId userId, final MovieTitle title) {
		this.userId = requireNonNull(userId, "UserId cannot be null");
		this.title = requireNonNull(title, "MovieTitle cannot be null");
		comments = new HashSet<>();
		tags = new HashSet<>();
	}

	WatchedMovie markAsWatched() {
		return new WatchedMovie(new MovieId(getId()), userId, title);
	}

	void updateMovieTitle(final MovieTitle title) {
		this.title = title;
	}

	void updateCover(final MovieCover cover) {
		this.cover = cover;
	}

	UUID addComment(final String comment) {
		final UUID commentId = UUID.randomUUID();
		this.comments.add(new MovieComment(new CommentId(commentId), getId(), comment));
		return commentId;
	}

	void deleteComment(final CommentId commentId) {
		comments.removeIf(movieComment -> movieComment.getCommentId().equals(commentId));
	}

	MovieId getMovieId() {
		return new MovieId(getId());
	}

	Optional<MovieCover> getCover() {
		return Optional.ofNullable(cover);
	}

	MovieTagId addTag(final MovieTagId tagId, final MovieTagLabel tagLabel, final UserId userId) {
		tags.add(new MovieTag(tagId, tagLabel, userId));
		return tagId;
	}

	void deleteTag(final MovieTagId tagId) {
		tags.removeIf(movieTag -> movieTag.getTagId().equals(tagId));
	}

	Set<MovieTag> getTags() {
		return tags;
	}

}
