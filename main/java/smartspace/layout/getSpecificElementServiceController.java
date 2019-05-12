package smartspace.layout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import smartspace.infra.GetSpecificElementService;

@RestController
public class getSpecificElementServiceController {
	
	private GetSpecificElementService elementService;
	
	@Autowired
	public getSpecificElementServiceController(GetSpecificElementService elementService) {
		this.elementService = elementService;
	}
	

	
	@RequestMapping(path = "/smartspace/elements/{userSmartspace}/{userEmail}/{elementSmartspace}/{elementId}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	
	public ElementBoundary getElementBoundry(
			@PathVariable("userSmartspace") String userSmartspace,
			@PathVariable("userEmail") String userEmail,
			@PathVariable("elementSmartspace") String elementSmartspace,
			@PathVariable("elementId") String elementId ){
		
	return new ElementBoundary(elementService.getElement(userSmartspace, userEmail,elementSmartspace,elementId));
		
	}
	
}
