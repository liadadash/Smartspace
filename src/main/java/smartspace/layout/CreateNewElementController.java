package smartspace.layout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import smartspace.infra.ElementServiceForManagerOnly;

@RestController
public class CreateNewElementController {
	private ElementServiceForManagerOnly elementService;

	@Autowired
	public CreateNewElementController(ElementServiceForManagerOnly elementService) {
		this.elementService = elementService;
	}
	
	@RequestMapping(
			path="/smartspace/elements/{managerSmartspace}/{managerEmail}",
			method=RequestMethod.POST,
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	
	public ElementBoundary newElement (@RequestBody ElementBoundary boundaryElements, @PathVariable("managerSmartspace") String managerSmartspace, @PathVariable("managerEmail") String managerEmail) {
		
		return new ElementBoundary(this.elementService.createNewElement(managerSmartspace,managerEmail,boundaryElements.convertToEntity()));
	}

}







