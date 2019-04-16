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
	 * Read all.
	 *
	 * @param size the size
	 * @param page the page
	 * @return the list
	 */
	public List<ActionEntity> readAll(int size, int page);
	//TODO add import option

}
