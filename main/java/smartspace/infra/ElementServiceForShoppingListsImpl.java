/**
 * @author liadkh	08-05-2019
 */
package smartspace.infra;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import smartspace.dao.EnhancedElementDao;
import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;

@Service
public class ElementServiceForShoppingListsImpl implements ElementServiceForShoppingLists {

	private EnhancedElementDao<ElementKey> elementDao;

	@Autowired
	public ElementServiceForShoppingListsImpl(EnhancedElementDao<ElementKey> elementDao) {
		this.elementDao = elementDao;
	}

	@Override
	public List<ElementEntity> getShoppingListsUsingPagination(String userSmartspace, String userEmail, int size, int page) {
		
		// add all lists where user is a member
		List<ElementEntity> allShoppingLists = this.elementDao.readAllListsByMember(userSmartspace, userEmail, size, page);
		
		// add all lists where user created the list
		allShoppingLists.addAll(this.elementDao.readAllListsByCreator(userSmartspace, userEmail, size, page));
		
		return allShoppingLists;
	}

	
}
