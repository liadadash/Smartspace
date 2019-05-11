/**
 * @author liadkh	08-05-2019
 */
package smartspace.infra;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import smartspace.aop.LoggerService;
import smartspace.dao.EnhancedElementDao;
import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;
import smartspace.data.UserRole;

/**
 * The Class ElementServiceForManagerOrPlayerImpl.
 */
@Service
@LoggerService
public class ElementServiceForManagerOrPlayerImpl implements ElementServiceForManagerOrPlayer {

	/** The element dao. */
	private EnhancedElementDao<ElementKey> elementDao;

	/**
	 * Instantiates a new element service for manager or player impl.
	 *
	 * @param elementDao the element dao
	 */
	@Autowired
	public ElementServiceForManagerOrPlayerImpl(EnhancedElementDao<ElementKey> elementDao) {
		this.elementDao = elementDao;
	}

	/**
	 * Gets the elements using pagination.
	 *
	 * @param role the role
	 * @param size the size
	 * @param page the page
	 * @return the elements using pagination
	 */
	// TODO:Need to add only manager or player annotation to change role
	@Override
	public List<ElementEntity> getElementsUsingPagination(UserRole role, int size, int page) {
		boolean showExpired = (role == UserRole.MANAGER) ? true : false;
		return elementDao.readAllUsingPaging(showExpired, size, page);
	}

	/**
	 * Gets the elements search by value using pagination.
	 *
	 * @param role     the role
	 * @param searchBy the search by
	 * @param value    the value
	 * @param size     the size
	 * @param page     the page
	 * @return the elements search by value using pagination
	 */
	// TODO:Need to add only manager or player annotation to change role
	@Override
	public List<ElementEntity> getElementsSearchByValueUsingPagination(UserRole role, String searchBy, String value, int size, int page) {
		boolean showExpired = (role == UserRole.MANAGER) ? true : false;

		// search by value if search argument is one of these keys
		String[] searchKeysByValue = {"name", "type"};
		
		if (Arrays.asList(searchKeysByValue).contains(searchBy)) {
			return elementDao.readAllWithSameValuesUsingPaging(showExpired, searchBy, value, size, page);
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Seach by this value is not valid: " + searchBy);
		}
	}
}
