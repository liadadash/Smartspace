package smartspace.layout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;
import smartspace.infra.updateElementService;

@RestController
public class updateElementController {

	private updateElementService elementService;
	
	@Autowired
	public updateElementController(updateElementService updateElementService) {
		this.elementService = updateElementService;
	}
	
	@RequestMapping(path = "/smartspace/elements/{managerSmartspace}/{managerEmail}/{elementSmartspace}/{elementId}",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateUElement(
			@PathVariable("managerSmartspace") String managerSmartspace,
			@PathVariable("managerEmail") String managerEmail,
			@PathVariable("elementSmartspace") String elementSmartspace,
			@PathVariable("elementId") String elementId,
			@RequestBody ElementBoundary elementBoundary) {
		
		ElementEntity elementEntity = elementBoundary.convertToEntity();
		elementEntity.setKey(new ElementKey(elementSmartspace, Long.parseLong(elementId)));
		this.elementService.updateElement(managerSmartspace, managerEmail, elementEntity);
		
	}
	
}
