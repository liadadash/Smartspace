/**
 * @author liadkh	09-05-2019
 */
package smartspace.infra;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	private ActionEntity validate(ActionEntity action) {
		if (!isValid(action)) {
			throw new RuntimeException("one or more of the given actions are invalid");
		}
		return action;
	}

	/**
	 * Checks if is valid.
	 *
	 * @param entity the entity
	 * @return true, if is valid
	 */
	private boolean isValid(ActionEntity entity) {
		boolean actionIsValid = (entity.getActionSmartspace() == null && entity.getActionId() == null
				&& notEmpty(entity.getActionType()) && entity.getCreationTimestamp() != null
				&& notEmpty(entity.getElementId()) && notEmpty(entity.getElementSmartspace())
				&& notEmpty(entity.getPlayerSmartspace()) && notEmpty(entity.getPlayerEmail())
				&& notEmpty(entity.getActionType()) && entity.getMoreAttributes() != null);

		if (!actionIsValid)
			return false;

		if (!checkActionType(entity.getActionType()))
			return false;

		Long elementId = Long.parseLong(entity.getElementId());
		Optional<UserEntity> userOp = userDao
				.readById(new UserKey(entity.getPlayerSmartspace(), entity.getPlayerEmail()));

		Optional<ElementEntity> elememtOp = elementDao
				.readById(new ElementKey(entity.getElementSmartspace(), elementId));

		return userOp.isPresent() && userOp.get().getRole() == UserRole.PLAYER && elememtOp.isPresent()
				&& !elememtOp.get().getExpired();
	}

	/**
	 * Not empty.
	 *
	 * @param str the str
	 * @return true, if successful
	 */
	private boolean notEmpty(String str) {
		return (str != null && !str.trim().isEmpty());
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
		throw new RuntimeException("No such action type");
	}
}
