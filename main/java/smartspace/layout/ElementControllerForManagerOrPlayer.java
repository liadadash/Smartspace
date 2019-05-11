/**
 * @author liadkh	08-05-2019
 */
package smartspace.layout;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import smartspace.infra.ElementServiceForManagerOrPlayerImpl;

/**
 * The Class ElementControllerForManagerOrPlayer.
 */
@RestController
public class ElementControllerForManagerOrPlayer {

	/** The base path. */
	private final String BASE_PATH = "/smartspace/elements";

	/** The element service. */
	private ElementServiceForManagerOrPlayerImpl elementService;

	/**
	 * Instantiates a new element controller for manager or player.
	 *
	 * @param elementService the element service
	 */
	@Autowired
	public ElementControllerForManagerOrPlayer(ElementServiceForManagerOrPlayerImpl elementService) {
		this.elementService = elementService;
	}

	/**
	 * Gets the all elements by value.
	 *
	 * @param userSmartspace the user smartspace
	 * @param userEmail the user email
	 * @param size the size
	 * @param page the page
	 * @param search the search
	 * @param value the value
	 * @return the all elements by value
	 */
	@RequestMapping(path = BASE_PATH
			+ "/{userSmartspace}/{userEmail}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] getAllElementsByValue(@PathVariable("userSmartspace") String userSmartspace,
			@PathVariable("userEmail") String userEmail,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "search", required = false) String search,
			@RequestParam(name = "value", required = false) String value) {

		// search by value if search argument is one of these keys
		String[] searchKeysByValue = { "name", "type" };

		if (search == null || value == null) {
			return this.elementService.getElementsUsingPagination(null, userSmartspace, userEmail, size, page).stream()
					.map(ElementBoundary::new).collect(Collectors.toList()).toArray(new ElementBoundary[0]);
		} else if (Arrays.asList(searchKeysByValue).contains(search)) {
			return this.elementService
					.getElementsSearchByValueUsingPagination(null, userSmartspace, userEmail, search, value, size, page)
					.stream().map(ElementBoundary::new).collect(Collectors.toList()).toArray(new ElementBoundary[0]);
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Page not found with this search option: " + value);
		}
	}
}
