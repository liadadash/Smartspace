package smartspace.data;

//aviel
public class UserEntity implements SmartspaceEntity<UserKey> {

	private String userSmartspace;
	private String userEmail;
	private String username;
	private String avatar;
	private UserRole role;
	private long points;
	private UserKey userKey;

	// default constructor
	public UserEntity() {
	}

	public UserEntity(String userEmail, String userSmartspace, String username, String avatar, UserRole role,
			long points) {
		this.userEmail = userEmail;
		this.userSmartspace = userSmartspace;
		this.username = username;
		this.avatar = avatar;
		this.role = role;
		this.points = points;
	}

	public String getUserSmartspace() {
		return userSmartspace;
	}

	public void setUserSmartspace(String userSmartspace) {
		this.userSmartspace = userSmartspace;
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
	public String toString() {
		return "UserEntity [userSmartspace=" + userSmartspace + ", userEmail=" + userEmail + ", username=" + username
				+ ", avatar=" + avatar + ", role=" + role + ", points=" + points + "]";
	}

	/*
	 * @author liadk
	 *
	 * @see smartspace.data.SmartspaceEntity#getKey()
	 * 
	 */
	@Override
	public UserKey getKey() {
		return new UserKey(userSmartspace, userEmail);
	}

	/*
	 * @author liadk
	 *
	 * @see smartspace.data.SmartspaceEntity#setKey(java.lang.Object)
	 * 
	 */
	@Override
	public void setKey(UserKey k) {
		this.setUserSmartspace(k.getUserSmartspace());
		this.setUserEmail(k.getUserEmail());
	}

}
