package smartspace.infra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import smartspace.aop.LoggerService;
import smartspace.aop.ManagerOrPlayerOnly;
import smartspace.dao.EnhancedElementDao;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;
import smartspace.data.UserEntity;
import smartspace.data.UserKey;
import smartspace.data.UserRole;
import smartspace.layout.ElementBoundary;

@Service
@LoggerService
public class getSpecificElementServiceImpl implements getSpecificElementService {

	private EnhancedElementDao<ElementKey> elementDao;
	private EnhancedUserDao<UserKey> userDao;

	@Autowired
	public getSpecificElementServiceImpl(EnhancedElementDao<ElementKey> elementDao, EnhancedUserDao<UserKey> userDao) {
		this.elementDao = elementDao;
		this.userDao = userDao;
	}

	@Override
	@ManagerOrPlayerOnly
	public ElementEntity getElement(String userSmartspace, String userEmail, String elementSmartspace,
			String elementId) {

		UserEntity user = this.userDao.readById(new UserKey(userSmartspace, userEmail))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not in DB"));
		
		ElementEntity element = elementDao.readById(new ElementKey(elementSmartspace, Long.parseLong(elementId))).orElseThrow(
				()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Element not in DB"));
		
		ElementEntity rv = null;
		
		if(user.getRole() == UserRole.MANAGER) {
			rv = element;
		}
		else if(user.getRole() == UserRole.PLAYER){ 
			//checking expired
			if(element.getExpired())
				new ResponseStatusException(HttpStatus.NOT_FOUND, "Element not found");
			else
				rv = element;
		}
		return rv;
	}

}
