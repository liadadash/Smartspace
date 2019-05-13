package smartspace.infra;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import smartspace.aop.LoggerService;
import smartspace.aop.ManagerOnly;
import smartspace.dao.EnhancedElementDao;
import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;

@Service
@LoggerService
public class ElementServiceForManagerOnlylmpl implements ElementServiceForManagerOnly {

	private EnhancedElementDao<ElementKey> elementDao;

	@Autowired
	public ElementServiceForManagerOnlylmpl(EnhancedElementDao<ElementKey> elementDao) {
		this.elementDao = elementDao;
	}
	
	@Override
	@ManagerOnly
	public ElementEntity createNewElement(String managerSmartspace,String managerEmail,ElementEntity newElement) {
		newElement.setCreationTimestamp(new Date());
		newElement.setCreatorSmartspace(managerSmartspace);
		newElement.setCreatorEmail(managerEmail);
		return this.elementDao.create(isValidElement(newElement));// create throw exception if element already exists
		
	}

	public ElementEntity isValidElement(ElementEntity newElement) {

		if (newElement.getType() == null || newElement.getType().trim().isEmpty())
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Type must not be empty");
		if (newElement.getName() == null && newElement.getName().trim().isEmpty())
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username must not be empty");
		if (newElement.getLocation() == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Location must not be empty");
		if (newElement.getMoreAttributes() == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attributes must not be empty");
		return newElement;

	}

}
