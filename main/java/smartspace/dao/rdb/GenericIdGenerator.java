/**
 * @author liadkh	02-04-2019
 */
package smartspace.dao.rdb;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * The Class GenericIdGenerator.
 *
 * @author liadkh
 */
@Entity
public class GenericIdGenerator {

/** The id. */
private Long id;
	
	/**
	 * Instantiates a new generic id generator.
	 */
	public GenericIdGenerator() {
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(Long id) {
		this.id = id;
	}
}
