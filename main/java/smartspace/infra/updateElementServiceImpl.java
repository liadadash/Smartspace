package smartspace.infra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import smartspace.aop.LoggerService;
import smartspace.dao.EnhancedElementDao;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;
import smartspace.data.UserEntity;
import smartspace.data.UserKey;
import smartspace.data.UserRole;

@Service
// @LoggerService
public class updateElementServiceImpl implements updateElementService {

	private EnhancedElementDao<ElementKey> elementDao;
	//remove this after using @managaerOnly annotation
	private EnhancedUserDao<UserKey> userDao;
	
	@Autowired
	public updateElementServiceImpl(EnhancedElementDao<ElementKey> elementDao,EnhancedUserDao<UserKey> userDao) {
		this.elementDao = elementDao;
		this.userDao = userDao;
	}
	
	@Override
	//@managerOnly
	public void updateElement(String managerSmartspace, String managerEmail, ElementEntity elementEntity) {
		if(checkIfUserIsManager(managerSmartspace,managerEmail)) {
			this.elementDao.update(elementEntity); // if the elementEntity not in DB elementDao.update() throw runTimeExepcion
		}
		else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This action for Manager only");
		}
	}

	private boolean checkIfUserIsManager(String managerSmartspace, String managerEmail) {
		UserEntity user = this.userDao.readById(new UserKey(managerSmartspace, managerEmail)).orElseThrow(
				()->new ResponseStatusException(HttpStatus.BAD_REQUEST, "user not in DB"));

		if(user.getRole() != UserRole.MANAGER)
			return false;
		else
			return true;
	}


}
