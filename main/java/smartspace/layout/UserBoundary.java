package smartspace.layout;

import java.util.Map;
import java.util.TreeMap;

import smartspace.data.UserEntity;
import smartspace.data.UserKey;
import smartspace.data.UserRole;

public class UserBoundary {
	private Map<String, String> key;
	private UserRole role;
	private String username;

	private String avatar;
	private long points;

	public UserBoundary() {
	}

	// convert UserEntity to UserBoundary
	public UserBoundary(UserEntity entity) {
		this.key = new TreeMap<String, String>();
		this.key.put("smartspace", entity.getUserSmartspace());
		this.key.put("email", entity.getUserEmail());

		this.role = entity.getRole();
		this.username = entity.getUsername();
		this.avatar = entity.getAvatar();
		this.points = entity.getPoints();

		// TreeMap put order is important and decides the order in the JSON file

		this.key = new TreeMap<String, String>();
		this.key.put("smartspace", entity.getUserSmartspace());
		this.key.put("email", entity.getUserEmail());

	}

	public Map<String, String> getKey() {
		return key;
	}

	public void setKey(Map<String, String> key) {
		this.key = key;
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

	public long getPoints() {
		return points;
	}

	public void setPoints(long points) {
		this.points = points;
	}

	public UserEntity convertToEntity() {
		UserEntity entity = new UserEntity();

		// check that TreeMap is not null and contains the required keys
		if (this.key != null && this.key.get("smartspace") != null && this.key.get("email") != null) {
			entity.setKey(new UserKey(this.key.get("smartspace"), this.key.get("email")));
		}
		entity.setAvatar(this.avatar);
		entity.setPoints(this.points);
		entity.setRole(this.role);
		entity.setUserSmartspace(this.key.get("smartspace"));
		entity.setUserEmail(this.key.get("email"));
		entity.setUsername(this.username);

		// check that TreeMap is not null and contains the required keys

		if (this.key != null && this.key.get("email") != null && this.key.get("smartspace") != null) {
			entity.setUserSmartspace(this.key.get("smartspace"));
			entity.setUserEmail(this.key.get("email"));
		}

		return entity;
	}

}
