package smartspace.data;

//aviel
public class UserEntity implements SmartspaceEntity<String> {

	private String userSmartSpace;
	private String userEmail;
	private String username;
	private String avatar;
	private UserRole role;
	private long points;
	

	// default constructor
	public UserEntity() {
	}

	public String getUserSmartSpace() {
		return userSmartSpace;
	}

	public void setUserSmartSpace(String userSmartSpace) {
		this.userSmartSpace = userSmartSpace;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
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

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public long getPoints() {
		return points;
	}

	public void setPoints(long points) {
		this.points = points;
	}

	@Override
	public String getKey() {
		return this.userEmail + this.userSmartSpace;
	}

	@Override
	public void setKey(String k) {
		//not sure yet
	}

	@Override
	public String toString() {
		return "UserEntity [userSmartSpace=" + userSmartSpace + ", userEmail=" + userEmail + ", username=" + username
				+ ", avatar=" + avatar + ", role=" + role + ", points=" + points +  "]";
	}

}
