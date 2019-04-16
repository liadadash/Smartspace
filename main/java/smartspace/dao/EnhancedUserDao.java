/**
 * @author liadkh	16-04-2019
 */
package smartspace.dao;

import java.util.List;

import smartspace.data.UserEntity;

/**
 * The Interface EnhancedUserDao.
 *
 * @author liadkh
 * @param <UserKey> the generic type
 */
public interface EnhancedUserDao<UserKey> extends UserDao<UserKey> {

	/**
	 * Read all.
	 *
	 * @param size the size
	 * @param page the page
	 * @return the list
	 */
	public List<UserEntity> readAll(int size, int page);
	
	//TODO add import option
}
