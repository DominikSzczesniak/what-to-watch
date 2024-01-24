package pl.szczesniak.dominik.whattowatch.users.domain;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.BaseEntity;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserPassword;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Objects.requireNonNull;

@Entity
@Table(name = "app_user")
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity implements UserDetails {

	@AttributeOverride(name = "value", column = @Column(name = "user_name", unique = true))
	private Username username;

	@AttributeOverride(name = "value", column = @Column(name = "password"))
	private UserPassword userPassword;

	@ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinTable
	@ToString.Exclude
	private List<UserRole> roles;

	public User(final Username username, final UserPassword userPassword) {
		this.username = requireNonNull(username, "Username must not be null.");
		this.userPassword = requireNonNull(userPassword, "UserPassword must not be null.");
		this.roles = new ArrayList<>();
	}

	public UserId getUserId() {
		return new UserId(getId());
	}

	public void addRole(final UserRole role, final User user) {
		roles.add(role);
		roles.forEach(roleEntity -> {
			final List<User> users = roleEntity.getUsers();
			if (!users.contains(user)) {
				users.add(user);
			}
		});
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>();

		if (roles != null) {
			for (UserRole role : roles) {
				authorities.add(new SimpleGrantedAuthority(role.getRoleName().getValue()));
			}
		}

		return authorities;
	}

	@Override
	public String getPassword() {
		return userPassword.getValue();
	}

	@Override
	public String getUsername() {
		return username.getValue();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public UserPassword getUserPassword() {
		return this.userPassword;
	}

	public List<UserRole> getRoles() {
		return this.roles;
	}
}
