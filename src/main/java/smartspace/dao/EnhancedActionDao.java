/**
 * @author liadkh	16-04-2019
 */
package smartspace.dao;

import java.util.List;

import smartspace.data.ActionEntity;

/**
 * The Interface EnhancedActionDao.
 *
 * @author liadkh
 */
public interface EnhancedActionDao extends ActionDao {

	/**
	 * Read all with paging.
	 *
	 * @param size the size
	 * @param page the page
	 * @return the list
	 */
	public List<ActionEntity> readAllWithPaging(int size, int page);

	/**
	 * Import action.
	 *
	 * @param action the action
	 * @return the action entity
	 */
	public ActionEntity importAction(ActionEntity action);

	List<ActionEntity> readAllWithPaging(String sortBy, int size, int page);

}
