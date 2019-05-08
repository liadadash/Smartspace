package smartspace.layout;

import smartspace.data.UserEntity;
import smartspace.data.UserRole;

public class UserFormBoundary {
	private String email;
	private UserRole role;
	private String username;
	private String avatar;

	// convert UserEntity to UserFormBoundary
	public UserFormBoundary(UserEntity entity) {
		this.email = entity.getUserEmail();
		this.role = entity.getRole();
		this.username = entity.getUsername();
		this.avatar = entity.getAvatar();
	}

	public UserFormBoundary() {
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public UserEntity convertToEntity() {
		UserEntity entity = new UserEntity();

		entity.setUserEmail(this.email);
		entity.setRole(this.role);
		entity.setUsername(this.username);
		entity.setAvatar(this.avatar);

		return entity;
	}

	@Override
	public String toString() {
		return "NewUserForm [email=" + email + ", role=" + role + ", username=" + username + ", avatar=" + avatar + "]";
	}
}
