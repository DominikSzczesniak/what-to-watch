package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = {"commentId"})
public class MovieComment {

	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "comment_id"))
	private CommentId commentId;

	private Integer movieId;

	private String text;

}
