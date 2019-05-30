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

	List<ElementEntity> readAllWithPaging(String sortBy, int size, int page);

	
	/**
	 * Read all using paging.
	 *
	 * @param showExpired the show expired
	 * @param size the size
	 * @param page the page
	 * @return the list
	 */
	public List<ElementEntity> readAllUsingPaging(boolean showExpired, int size, int page);

	/**
	 * Read all with same values using paging.
	 *
	 * @param showExpired the show expired
	 * @param searchBy the search by
	 * @param value the value
	 * @param size the size
	 * @param page the page
	 * @return the list
	 */
	public List<ElementEntity> readAllWithSameValuesUsingPaging(boolean showExpired, String searchBy, String value, int size, int page);

	// search by location
	public List<ElementEntity> searchByLocation(boolean includeExpired, Double x, Double y, Double distance, int size, int page);
	
	// get manager's lists
	public List<ElementEntity> readAllListsByCreator(String creatorSmartspace, String creatorEmail, int size, int page);
	
	// get user's lists
	public List<ElementEntity> readAllListsByMember(String userSmartspace, String userEmail, int size, int page);

	// get list's items
	public List<ElementEntity> readAllItemsByShoppingList(String smartspcae, String id, int size, int page);

}
