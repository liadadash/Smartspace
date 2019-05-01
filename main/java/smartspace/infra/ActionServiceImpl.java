package smartspace.infra;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import smartspace.dao.EnhancedActionDao;
import smartspace.dao.EnhancedElementDao;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.ActionEntity;
import smartspace.data.ElementKey;
import smartspace.data.UserKey;

@Service
public class ActionServiceImpl implements ActionService {
	private EnhancedActionDao actionDao;
	private EnhancedUserDao<UserKey> userDao; // used for admin check
	private EnhancedElementDao<ElementKey> elementDao; // used to check that action's element was imported before action

	@Value("${smartspace.name}")
	private String appSmartspace;
	
	@Autowired
	public ActionServiceImpl(EnhancedActionDao actionDao, EnhancedUserDao<UserKey> userDao, EnhancedElementDao<ElementKey> elementDao) {
		this.actionDao=actionDao;
		this.userDao=userDao;
		this.elementDao = elementDao;
	}
	
	
	@Override
	public List<ActionEntity> importActions(List<ActionEntity> entities, String adminSmartspace, String adminEmail) {
		// check that the user has ADMIN privileges (code is okay)
		if(!this.userDao.userIsAdmin(new UserKey(adminSmartspace,adminEmail))) {
			throw new RuntimeException("this user is not allowed to import actions");
		}
		
		// check that all elements are valid
		boolean allValid = entities.stream().allMatch(this::valiadate);
		
		// if all valid save to database
		if(allValid) {
			return entities.stream().map(this.actionDao::importAction).collect(Collectors.toList());
		} else {
			throw new RuntimeException("one or more of the given Actions are invalid");
		}

		
	}

	@Override
	public List<ActionEntity> getUsingPagination(int size, int page, String adminSmartspace, String adminEmail) {

		// check that the user has ADMIN privileges (code is okay)
		if(!this.userDao.userIsAdmin(new UserKey(adminSmartspace,adminEmail))) {
			throw new RuntimeException("this user is not allowed to import actions");
		}
		return this.actionDao.readAllWithPaging(size, page);
	}
	
	private boolean valiadate(ActionEntity entity) {
		boolean actionIsValid = (entity.getActionSmartspace() != null
				&& !entity.getActionSmartspace().equals(appSmartspace) 
				&& notEmpty(entity.getActionId())
				&& entity.getCreationTimestamp() != null
				&& notEmpty(entity.getElementId())
				&& notEmpty(entity.getElementSmartspace())
				&& notEmpty(entity.getPlayerSmartspace())
				&& notEmpty(entity.getPlayerEmail())
				&& notEmpty(entity.getActionType())
				&& entity.getKey() != null
				&& notEmpty(entity.getKey().getActionSmartspace())
				&& entity.getMoreAttributes() != null);
		
		// return false when action data is invalid
		if (!actionIsValid) {
			return false;
		}
		
		// data is valid -> the action's element needs to be in the database (must import elements before actions)
		Long elementId = Long.parseLong(entity.getElementId());
		return elementDao.readById(new ElementKey(entity.getElementSmartspace(), elementId)).isPresent(); // return if element exists
	}
	
	private boolean notEmpty(String str) {
		return (str != null && !str.trim().isEmpty());
	}

}
