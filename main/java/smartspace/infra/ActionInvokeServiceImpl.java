/**
 * @author liadkh	09-05-2019
 */
package smartspace.infra;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import smartspace.aop.LoggerService;
import smartspace.dao.EnhancedActionDao;
import smartspace.dao.EnhancedElementDao;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.ActionEntity;
import smartspace.data.ActionTypes;
import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;
import smartspace.data.UserEntity;
import smartspace.data.UserKey;
import smartspace.data.UserRole;

/**
 * The Class ActionInvokeServiceImpl.
 */
@Service
public class ActionInvokeServiceImpl implements ActionInvokeService {

	/** The action dao. */
	private EnhancedActionDao actionDao;

	/** The user dao. */
	private EnhancedUserDao<UserKey> userDao;

	/** The element dao. */
	private EnhancedElementDao<ElementKey> elementDao; // used to check that action's element was imported before action

	/**
	 * Instantiates a new action invoke service impl.
	 *
	 * @param actionDao  the action dao
	 * @param elementDao the element dao
	 * @param userDao    the user dao
	 */
	@Autowired
	public ActionInvokeServiceImpl(EnhancedActionDao actionDao, EnhancedElementDao<ElementKey> elementDao,
			EnhancedUserDao<UserKey> userDao) {
		this.actionDao = actionDao;
		this.elementDao = elementDao;
		this.userDao = userDao;
	}

	/**
	 * Invoke action.
	 *
	 * @param actionEntity the action entity
	 * @return the action entity
	 */
	@Override
	@LoggerService
	public ActionEntity invokeAction(ActionEntity actionEntity) {
		ActionEntity rv = validate(actionEntity);
		rv.setCreationTimestamp(new Date());

		if (rv.getActionType().equals(ActionTypes.ECHO.name()))
			rv = this.actionDao.create(rv);
		return rv;
	}

	/**
	 * Validate.
	 *
	 * @param action the action
	 * @return the action entity
	 */
	private ActionEntity validate(ActionEntity entity) {
			
		if (isEmpty(entity.getActionSmartspace())) {
			throw new RuntimeException("Action smartspace must not be empty");
		} 
		else if (entity.getActionId() == null) {
			throw new RuntimeException("Action id must not be empty");
		}
		else if (isEmpty(entity.getActionType())) {
			throw new RuntimeException("Action type must not be empty");
		}
		else if (isEmpty(entity.getElementId())) {
			throw new RuntimeException("Element id must not be empty");
		}
		else if (isEmpty(entity.getElementSmartspace())) {
			throw new RuntimeException("Element smartspace must not be empty");
		}
		else if (isEmpty(entity.getPlayerSmartspace())) {
			throw new RuntimeException("Player smartspace must not be empty");
		}
		else if (isEmpty(entity.getPlayerEmail())) {
			throw new RuntimeException("Player email must not be empty");
		}
		else if (entity.getMoreAttributes() == null) {
			throw new RuntimeException("Attributes must be defined");
		}

		if (!checkActionType(entity.getActionType())) {
			throw new RuntimeException("Action type is not supported: " + entity.getActionType());
		}

		Optional<UserEntity> userOp = userDao.readById(new UserKey(entity.getPlayerSmartspace(), entity.getPlayerEmail()));
		if (!userOp.isPresent()) {
			throw new RuntimeException("User not found");
		}
		else if (userOp.get().getRole() != UserRole.PLAYER) {
			throw new RuntimeException("Only players can invoke actions");
		}
		
		Long elementId = Long.parseLong(entity.getElementId());
		Optional<ElementEntity> elememtOp = elementDao.readById(new ElementKey(entity.getElementSmartspace(), elementId));
		if (!elememtOp.isPresent()) {
			throw new RuntimeException("the action is invoked on a non existing element");
		}
		else if (elememtOp.get().getExpired()) {
			throw new RuntimeException("element is expired");
		}
		
		return entity;
	}
	
	private boolean isEmpty(String str) {
		return (str == null || str.trim().isEmpty());
	}

	/**
	 * Check action type.
	 *
	 * @param checkType the check type
	 * @return true, if successful
	 */
	private boolean checkActionType(String checkType) {
		for (ActionTypes type : ActionTypes.values()) {
			if (type.name().equals(checkType)) {
				return true;
			}
		}
		return false;
	}
}
