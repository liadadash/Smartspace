
package smartspace.data;

import java.io.Serializable;

import javax.persistence.Embeddable;

/**
 * The Class ActionKey.
 *
 * @author liadk
 */

@Embeddable
public class ActionKey implements Comparable<ActionKey>, Serializable {

	/** The action smartspace. */
	private String actionSmartspace;
	
	/** The id. */
	private long id;

	/**
	 * Instantiates a new action key.
	 *
	 * @author liadk
	 */
	public ActionKey() {

	}

	/**
	 * Instantiates a new action key.
	 *
	 * @author liadk
	 * @param actionSmartspace the action smartspace
	 * @param id the id
	 */
	public ActionKey(String actionSmartspace, long id) {
		super();
		this.actionSmartspace = actionSmartspace;
		this.id = id;
	}

	/**
	 * Gets the action smartspace.
	 *
	 * @author liadk
	 * @return the actionSmartspace
	 */

	public String getActionSmartspace() {
		return actionSmartspace;
	}

	/**
	 * Sets the action smartspace.
	 *
	 * @author liadk
	 * @param actionSmartspace the actionSmartspace to set
	 */
	public void setActionSmartspace(String actionSmartspace) {
		this.actionSmartspace = actionSmartspace;
	}

	/**
	 * Gets the id.
	 *
	 * @author liadk
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @author liadk
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
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
		result = prime * result + ((this.actionSmartspace == null) ? 0 : this.actionSmartspace.hashCode());
		result = (int) (prime * result + this.id);
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
		ActionKey ActionKey = (ActionKey) obj;
		return this.actionSmartspace.equals(ActionKey.actionSmartspace) && this.id == ActionKey.id;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.actionSmartspace + "#" + this.getClass().getSimpleName() + "#" + this.id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(ActionKey o) {
		int res = this.actionSmartspace.compareTo(o.actionSmartspace);
		return (int) (res == 0 ? this.id - o.id : res);
	}
}
