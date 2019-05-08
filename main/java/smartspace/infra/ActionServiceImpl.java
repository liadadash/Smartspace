package smartspace.infra;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sun.istack.internal.NotNull;

import smartspace.aop.AdminOnly;
import smartspace.dao.EnhancedActionDao;
import smartspace.dao.EnhancedElementDao;
import smartspace.data.ActionEntity;
import smartspace.data.ElementKey;

@Service
public class ActionServiceImpl implements ActionService {

	Log logger = LogFactory.getLog(ActionServiceImpl.class);

	private EnhancedActionDao actionDao;
	private EnhancedElementDao<ElementKey> elementDao; // used to check that action's element was imported before action

	@Value("${smartspace.name}")
	private String appSmartspace;

	@Autowired
	public ActionServiceImpl(EnhancedActionDao actionDao, EnhancedElementDao<ElementKey> elementDao) {
		this.actionDao = actionDao;
		this.elementDao = elementDao;
	}

	@Override
	@Transactional
	@AdminOnly
	public List<ActionEntity> importActions(String adminSmartspace, String adminEmail,
			@NotNull List<ActionEntity> entities) {
		logger.info("Try to import " + entities.size() + " actions by: " + adminSmartspace + "#" + adminEmail);
		return entities.stream().map(this::validate).map(this.actionDao::importAction).collect(Collectors.toList());
	}

	@Override
	@AdminOnly
	public List<ActionEntity> getUsingPagination(String adminSmartspace, String adminEmail, int size, int page) {
		logger.info("Get actions using pagination by: " + adminSmartspace + "#" + adminEmail + " size: " + size
				+ ", page: " + page);
		return this.actionDao.readAllWithPaging("key", size, page);
	}

	private ActionEntity validate(ActionEntity action) {
		logger.debug("Check if action is valid");
		if (!isValid(action)) {
			logger.debug("Action isn't valid");
			throw new RuntimeException("one or more of the given actions are invalid");
		}
		logger.debug("Action is valid");
		return action;
	}

	private boolean isValid(ActionEntity entity) {
		boolean actionIsValid = (entity.getActionSmartspace() != null
				&& !entity.getActionSmartspace().equals(appSmartspace) && notEmpty(entity.getActionId())
				&& entity.getCreationTimestamp() != null && notEmpty(entity.getElementId())
				&& notEmpty(entity.getElementSmartspace()) && notEmpty(entity.getPlayerSmartspace())
				&& notEmpty(entity.getPlayerEmail()) && notEmpty(entity.getActionType()) && entity.getKey() != null
				&& notEmpty(entity.getKey().getActionSmartspace()) && entity.getMoreAttributes() != null);

		// return false when action data is invalid
		if (!actionIsValid) {
			return false;
		}

		// data is valid -> the action's element needs to be in the database (must
		// import elements before actions)
		Long elementId = Long.parseLong(entity.getElementId());
		return elementDao.readById(new ElementKey(entity.getElementSmartspace(), elementId)).isPresent(); // return if
																											// element
																											// exists
	}

	private boolean notEmpty(String str) {
		return (str != null && !str.trim().isEmpty());
	}

}
