/**
 * @author liadkh	02-04-2019
 */
package smartspace.dao.rdb;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author liadkh
 *
 */
public class GenericIdGenerator {
private Long id;
	
	public GenericIdGenerator() {
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
