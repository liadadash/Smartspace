
package smartspace.data;

import java.io.Serializable;

import javax.persistence.Embeddable;

/**
 * The Class UserKey.
 *
 * @author liadk
 */

@Embeddable
public class UserKey implements Comparable<UserKey>, Serializable {

	/** The user smartspace. */
	private String userSmartspace;
	
	/** The user email. */
	private String userEmail;

	/**
	 * Instantiates a new user key.
	 *
	 * @author liadk
	 */
	public UserKey() {
	}

	/**
	 * Instantiates a new user key.
	 *
	 * @author liadk
	 * @param userSmartspace the user smartspace
	 * @param userEmail the user email
	 */
	public UserKey(String userSmartspace, String userEmail) {
		this.userSmartspace = userSmartspace;
		this.userEmail = userEmail;
	}

	/**
	 * Gets the user smartspace.
	 *
	 * @author liadk
	 * @return the userSmartspace
	 */
	public String getUserSmartspace() {
		return userSmartspace;
	}

	/**
	 * Sets the user smartspace.
	 *
	 * @author liadk
	 * @param userSmartspace the userSmartspace to set
	 */
	public void setUserSmartspace(String userSmartspace) {
		this.userSmartspace = userSmartspace;
	}

	/**
	 * Gets the user email.
	 *
	 * @author liadk
	 * @return the userEmail
	 */
	public String getUserEmail() {
		return userEmail;
	}

	/**
	 * Sets the user email.
	 *
	 * @author liadk
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.userSmartspace + "#" + this.getClass().getSimpleName() + "#" + this.userEmail;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(UserKey o) {
		int res = this.userSmartspace.compareTo(o.userSmartspace);
		return res == 0 ? this.userEmail.compareTo(o.userEmail) : res;
	}
}
