package smartspace.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection="USERS")
public class UserEntity implements SmartspaceEntity<UserKey> {

	private String userSmartspace;
	private String userEmail;
	private String username;
	private String avatar;
	private UserRole role;
	private long points;
	private UserKey key;

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
	
	@JsonIgnore
	public String getUserSmartspace() {
		return userSmartspace;
	}

	public void setUserSmartspace(String userSmartspace) {
		this.userSmartspace = userSmartspace;
	}
	
	@JsonIgnore
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
	@Id
	public UserKey getKey() {
		this.key = new UserKey(this.userSmartspace, this.userEmail);
		return this.key;
	}


	@Override
	public void setKey(UserKey k) {
		this.setUserSmartspace(k.getUserSmartspace());
		this.setUserEmail(k.getUserEmail());
		
		this.key = new UserKey(k.getUserSmartspace(), k.getUserEmail());
	}
	
	@Override
	public String toString() {
		return "UserEntity [userSmartspace=" + userSmartspace + ", userEmail=" + userEmail + ", username=" + username
				+ ", avatar=" + avatar + ", role=" + role + ", points=" + points + "]";
	}

}
