/**
 * @author liadkh	08-05-2019
 */
package smartspace.layout;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import smartspace.data.ElementEntity;
import smartspace.infra.ElementServiceForShoppingListsImpl;

/**
 * The Class ElementControllerForManagerOrPlayer.
 */
@RestController
public class ListElementController {

	/** The base path. */
	private final String PATH = "/smartspace/shoppingLists/{userSmartspace}/{userEmail}";

	/** The element service. */
	private ElementServiceForShoppingListsImpl shoppingListService;

	/**
	 * Instantiates a new element controller for manager or player.
	 *
	 * @param elementService the element service
	 */
	@Autowired
	public ListElementController(ElementServiceForShoppingListsImpl shoppingListService) {
		this.shoppingListService = shoppingListService;
	}

	@RequestMapping(path = PATH, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] getAllListsForUser(@PathVariable("userSmartspace") String userSmartspace,
			@PathVariable("userEmail") String userEmail,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {

		return toBoundary(this.shoppingListService.getShoppingListsUsingPagination(userSmartspace, userEmail, size, page));
	}

	private ElementBoundary[] toBoundary(List<ElementEntity> list) {
		return list.stream().map(ElementBoundary::new).collect(Collectors.toList()).toArray(new ElementBoundary[0]);

	}
}
