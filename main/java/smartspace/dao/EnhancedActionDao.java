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
	 * Import actions.
	 *
	 * @param actions the actions
	 */
	public void importActions(ActionEntity[] actions);

}
