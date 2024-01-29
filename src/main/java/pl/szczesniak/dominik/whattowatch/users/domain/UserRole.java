package pl.szczesniak.dominik.whattowatch.users.domain;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.BaseEntity;
import pl.szczesniak.dominik.whattowatch.users.domain.model.RoleName;

@Entity
@Table
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRole extends BaseEntity {

	@AttributeOverride(name = "value", column = @Column(name = "role_name"))
	@Enumerated(value = EnumType.STRING)
	private RoleName roleName;

//	@ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, mappedBy = "roles")
//	@JsonIgnoreProperties("roles")
//	private List<User> users;

	public UserRole(@NonNull final RoleName roleName) {
		this.roleName = roleName;
//		users = new ArrayList<>();
	}

}
