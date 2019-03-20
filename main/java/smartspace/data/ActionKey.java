
package smartspace.data;

/**
 * @author liadk
 *
 */
public class ActionKey {

	private String actionSmartspace;
	private long id;

	/**
	 * @author liadk
	 *
	 */
	public ActionKey() {

	}

	/**
	 * @author liadk
	 *
	 * @param actionSmartspace
	 * @param id
	 */
	public ActionKey(String actionSmartspace, long id) {
		super();
		this.actionSmartspace = actionSmartspace;
		this.id = id;
	}

	/**
	 * @author liadk
	 *
	 * @return the actionSmartspace
	 */
	public String getActionSmartspace() {
		return actionSmartspace;
	}

	/**
	 * @author liadk
	 *
	 * @param actionSmartspace the actionSmartspace to set
	 */
	public void setActionSmartspace(String actionSmartspace) {
		this.actionSmartspace = actionSmartspace;
	}

	/**
	 * @author liadk
	 *
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @author liadk
	 *
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
}
