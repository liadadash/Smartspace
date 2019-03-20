package smartspace.data;

/**
 * @author liadk
 *
 */
public class ElementKey {

	private String elementSmartspace;
	private long id;

	/**
	 * @author liadk
	 *
	 */
	public ElementKey() {

	}

	/**
	 * @author liadk
	 *
	 * @param elementSmartspace
	 * @param id
	 */
	public ElementKey(String elementSmartspace, long id) {
		super();
		this.elementSmartspace = elementSmartspace;
		this.id = id;
	}

	/**
	 * @author liadk
	 *
	 * @return the elementSmartspace
	 */
	public String getElementSmartspace() {
		return elementSmartspace;
	}

	/**
	 * @author liadk
	 *
	 * @param elementSmartspace the elementSmartspace to set
	 */
	public void setElementSmartspace(String elementSmartspace) {
		this.elementSmartspace = elementSmartspace;
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
		result = prime * result + ((this.elementSmartspace == null) ? 0 : this.elementSmartspace.hashCode());
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
		ElementKey ElementKey = (ElementKey) obj;
		return this.elementSmartspace.equals(ElementKey.elementSmartspace) && this.id == ElementKey.id;
	}
}
