package smartspace.layout;

import java.util.Map;
import java.util.TreeMap;

import smartspace.data.UserEntity;
import smartspace.data.UserKey;
import smartspace.data.UserRole;

public class UserBoundary {
	private Map<String, String> key;
	private String role;
	private String username;
	private String avatar;
	private Long points;

	public UserBoundary() {
	}

	// convert UserEntity to UserBoundary
	public UserBoundary(UserEntity entity) {
		this.key = new TreeMap<String, String>();
		this.key.put("smartspace", entity.getUserSmartspace());
		this.key.put("email", entity.getUserEmail());

		this.role = entity.getRole().toString();
		this.username = entity.getUsername();
		this.avatar = entity.getAvatar();
		this.points = entity.getPoints();
	}

	public Map<String, String> getKey() {
		return key;
	}

	public void setKey(Map<String, String> key) {
		this.key = key;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
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

	public Long getPoints() {
		return points;
	}

	public void setPoints(Long points) {
		this.points = points;
	}

	public UserEntity convertToEntity() {
		UserEntity entity = new UserEntity();

		// check that TreeMap is not null and contains the required keys
		if (this.key != null && this.key.get("smartspace") != null && this.key.get("email") != null) {
			entity.setKey(new UserKey(this.key.get("smartspace"), this.key.get("email")));
		}

		entity.setRole(UserRole.valueOf(this.role));
		entity.setUsername(this.username);
		entity.setAvatar(this.avatar);

		if (this.points != null) {
			entity.setPoints(this.points);
		} else {
			entity.setPoints(0);
		}

		return entity;
	}

	@Override
	public String toString() {
		return "UserBoundary [key=" + key + ", role=" + role + ", username=" + username + ", avatar=" + avatar
				+ ", points=" + points + "]";
	}
}
