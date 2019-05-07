package smartspace.data;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="USERS")
public class UserEntity implements SmartspaceEntity<UserKey> {

	@Column(nullable = false)
	private String userSmartspace;
	@Column(nullable = false)
	private String userEmail;
	
	private String username;
	private String avatar;
	private UserRole role;
	private long points;

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
	
	@Transient
	public String getUserSmartspace() {
		return userSmartspace;
	}

	public void setUserSmartspace(String userSmartspace) {
		this.userSmartspace = userSmartspace;
	}
	
	@Transient
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
	
	@Enumerated(EnumType.STRING)
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
	@EmbeddedId
	@Column(name="ID")
	public UserKey getKey() {
		return new UserKey(userSmartspace, userEmail);
	}


	@Override
	public void setKey(UserKey k) {
		this.setUserSmartspace(k.getUserSmartspace());
		this.setUserEmail(k.getUserEmail());
	}
	
	@Override
	public String toString() {
		return "UserEntity [userSmartspace=" + userSmartspace + ", userEmail=" + userEmail + ", username=" + username
				+ ", avatar=" + avatar + ", role=" + role + ", points=" + points + "]";
	}

}
