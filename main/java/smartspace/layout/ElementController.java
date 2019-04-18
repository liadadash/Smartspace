package smartspace.layout;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import smartspace.data.ElementEntity;
import smartspace.infra.ElementService;

@RestController
public class ElementController {
	private ElementService elementService;

	@Autowired
	public ElementController(ElementService elementService) {
		this.elementService = elementService;
	}

	@RequestMapping(
			path="/smartspace/admin/elements/{adminSmartspace}/{adminEmail}",
			method=RequestMethod.POST,
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	
	public ElementBoundary[] newMessage (@RequestBody ElementBoundary[] boundaryElements, @PathVariable("adminSmartspace") String adminSmartspace, @PathVariable("adminEmail") String adminEmail) {
		
		// convert ElementBoundary Array to ElementEntity List.
		List<ElementEntity> entities = Stream.of(boundaryElements).map(ElementBoundary::convertToEntity).collect(Collectors.toList());
		
		// import the elements
		return this.elementService.importElements(entities, adminSmartspace, adminEmail).stream()
				.map(ElementBoundary::new).collect(Collectors.toList()).toArray(new ElementBoundary[0]);
	}

	@RequestMapping(
			path="/smartspace/admin/elements/{adminSmartspace}/{adminEmail}",
			method=RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE)
	
	public ElementBoundary[] getUsingPagination (
			@PathVariable("adminSmartspace") String adminSmartspace,
			@PathVariable("adminEmail") String adminEmail,
			@RequestParam(name="size", required=false, defaultValue="10") int size,
			@RequestParam(name="page", required=false, defaultValue="0") int page) {
		
		return this.elementService.getUsingPagination(size, page, adminSmartspace, adminEmail).stream()
				.map(ElementBoundary::new).collect(Collectors.toList()).toArray(new ElementBoundary[0]);
	}
}
