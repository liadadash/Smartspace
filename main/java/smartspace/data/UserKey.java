
package smartspace.data;

/**
 * @author liadk
 *
 */
public class UserKey {

	private String userSmartspace;
	private String userEmail;

	/**
	 * @author liadk
	 *
	 */
	public UserKey() {
	}

	/**
	 * @author liadk
	 *
	 */
	public UserKey(String userSmartspace, String userEmail) {
		this.userSmartspace = userSmartspace;
		this.userEmail = userEmail;
	}

	/**
	 * @author liadk
	 *
	 * @return the userSmartspace
	 */
	public String getUserSmartspace() {
		return userSmartspace;
	}

	/**
	 * @author liadk
	 *
	 * @param userSmartspace the userSmartspace to set
	 */
	public void setUserSmartspace(String userSmartspace) {
		this.userSmartspace = userSmartspace;
	}

	/**
	 * @author liadk
	 *
	 * @return the userEmail
	 */
	public String getUserEmail() {
		return userEmail;
	}

	/**
	 * @author liadk
	 *
	 * @param userEmail the userEmail to set
	 */
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	/*
	 * @author liadk
	 *
	 * @see java.lang.Object#hashCode()
	 * 
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.userSmartspace == null) ? 0 : this.userEmail.hashCode());
		result = prime * result + ((this.userEmail == null) ? 0 : this.userSmartspace.hashCode());
		return result;
	}

	/*
	 * @author liadk
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		UserKey UserKey = (UserKey) obj;
		return this.userSmartspace.equals(UserKey.userSmartspace) && this.userEmail.equals(UserKey.userEmail);
	}
	
	@Override
	public String toString() {
		return this.userSmartspace + "#" + this.getClass().getSimpleName() + "#" + this.userEmail;
	}
}
