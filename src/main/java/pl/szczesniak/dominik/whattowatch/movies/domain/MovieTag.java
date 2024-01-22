package pl.szczesniak.dominik.whattowatch.movies.domain;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTagId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTagLabel;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import static java.util.Objects.requireNonNull;

@Entity
@Table(name = "tags", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"tag_user_id", "tag_label"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@EqualsAndHashCode(of = {"tagId"})
public class MovieTag {

	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "tag_id"))
	private MovieTagId tagId;

	@AttributeOverride(name = "value", column = @Column(name = "tag_label"))
	private MovieTagLabel label;

	@AttributeOverride(name = "value", column = @Column(name = "tag_user_id"))
	private UserId userId;

	MovieTag(final MovieTagId tagId, final MovieTagLabel label, final UserId userId) {
		this.tagId = requireNonNull(tagId, "TagId cannot be null");
		this.label = requireNonNull(label, "TagLabel cannot be null");
		this.userId = requireNonNull(userId, "UserId cannot be null");
	}

}
