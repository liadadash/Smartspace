package smartspace.data;

import java.io.Serializable;

import javax.persistence.Embeddable;

/**
 * The Class ElementKey.
 */
@Embeddable
public class ElementKey implements Comparable<ElementKey>, Serializable {

	/** The element smartspace. */
	private String elementSmartspace;
	
	/** The id. */
	private long id;

	/**
	 * Instantiates a new element key.
	 */
	public ElementKey() {

	}

	/**
	 * Instantiates a new element key.
	 *
	 * @param elementSmartspace the element smartspace
	 * @param id the id
	 */
	public ElementKey(String elementSmartspace, long id) {
		super();
		this.elementSmartspace = elementSmartspace;
		this.id = id;
	}

	/**
	 * Gets the element smartspace.
	 *
	 * @return the elementSmartspace
	 */
	public String getElementSmartspace() {
		return elementSmartspace;
	}

	/**
	 * Sets the element smartspace.
	 *
	 * @param elementSmartspace the elementSmartspace to set
	 */
	public void setElementSmartspace(String elementSmartspace) {
		this.elementSmartspace = elementSmartspace;
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
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.elementSmartspace == null) ? 0 : this.elementSmartspace.hashCode());
		result = (int) (prime * result + this.id);
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.elementSmartspace + "#" + this.getClass().getSimpleName() + "#" + this.id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(ElementKey o) {
		int res = this.elementSmartspace.compareTo(o.elementSmartspace);
		return (int) (res == 0 ? this.id - o.id : res);
	}
}
