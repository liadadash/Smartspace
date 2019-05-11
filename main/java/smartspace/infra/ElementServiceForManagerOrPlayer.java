/**
 * @author liadkh	08-05-2019
 */
package smartspace.infra;

import java.util.List;

import smartspace.data.ElementEntity;
import smartspace.data.UserRole;

/**
 * The Interface ElementServiceForManagerOrPlayer.
 * @author liadkh
 */
public interface ElementServiceForManagerOrPlayer {

	/**
	 * Gets the elements using pagination.
	 *
	 * @param role the role
	 * @param size the size
	 * @param page the page
	 * @return the elements using pagination
	 */
	public List<ElementEntity> getElementsUsingPagination(UserRole role, int size, int page);

	/**
	 * Gets the elements search by value using pagination.
	 *
	 * @param role the role
	 * @param searchBy the search by
	 * @param value the value
	 * @param size the size
	 * @param page the page
	 * @return the elements search by value using pagination
	 */
	public List<ElementEntity> getElementsSearchByValueUsingPagination(UserRole role, String searchBy, String value,
			int size, int page);
}
