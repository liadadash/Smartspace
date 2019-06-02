/*
 * @author liadkh
 */

package smartspace.data;

import java.io.Serializable;

import javax.persistence.Embeddable;

/**
 * The Class ActionKey.
 */

@Embeddable
public class ActionKey implements Comparable<ActionKey>, Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8226215253488114082L;

	/** The action smartspace. */
	private String actionSmartspace;
	
	/** The id. */
	private long id;

	/**
	 * Instantiates a new action key.
	 */
	public ActionKey() {

	}

	/**
	 * Instantiates a new action key.
	 *
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
	 * @return the action smartspace
	 */

	public String getActionSmartspace() {
		return actionSmartspace;
	}

	/**
	 * Sets the action smartspace.
	 *
	 * @param actionSmartspace the new action smartspace
	 */
	public void setActionSmartspace(String actionSmartspace) {
		this.actionSmartspace = actionSmartspace;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Hash code.
	 *
	 * @return the int
	 */
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

	/**
	 * Equals.
	 *
	 * @param obj the obj
	 * @return true, if successful
	 */
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

	/**
	 * To string.
	 *
	 * @return the string
	 */
	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.actionSmartspace + "#" + this.getClass().getSimpleName() + "#" + this.id;
	}

	/**
	 * Compare to.
	 *
	 * @param o the o
	 * @return the int
	 */
	/*
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(ActionKey o) {
		int res = this.actionSmartspace.compareTo(o.actionSmartspace);
		return (int) (res == 0 ? this.id - o.id : res);
	}
}
