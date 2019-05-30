/**
 * @author liadkh	08-05-2019
 */
package smartspace.infra;

import java.util.List;

import smartspace.data.ElementEntity;

/**
 * The Interface ElementServiceForManagerOrPlayer.
 */
public interface ElementServiceForShoppingLists {
	
	public List<ElementEntity> getShoppingListsUsingPagination(String userSmartspace, String userEmail, int size, int page);

	public List<ElementEntity> getShoppingItemsByList(String listSmartspace, String listId, int size, int page);
	
}
