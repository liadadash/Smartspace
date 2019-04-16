/**
 * @author liadkh	16-04-2019
 */
package smartspace.dao;

import java.util.List;

import smartspace.data.ElementEntity;

/**
 * The Interface EnhancedElementDao.
 *
 * @author liadkh
 * @param <ElementKey> the generic type
 */
public interface EnhancedElementDao<ElementKey> extends ElementDao<ElementKey> {

	/**
	 * Read all with paging.
	 *
	 * @param size the size
	 * @param page the page
	 * @return the list
	 */
	public List<ElementEntity> readAllWithPaging(int size, int page);


	/**
	 * Import element.
	 *
	 * @param element the element
	 * @return the element entity
	 */
	public ElementEntity importElement(ElementEntity element);

}
