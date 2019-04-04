package smartspace.dao.memory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import smartspace.dao.ActionDao;
import smartspace.data.ActionEntity;
import smartspace.data.ActionKey;

/**
 * The Class MemoryActionDao.
 *
 * @author liadk
 */

//@Repository
public class MemoryActionDao implements ActionDao {

	/** The actions. */
	private List<ActionEntity> actions;
	
	/** The next id. */
	private AtomicLong nextId;

	/**
	 * Instantiates a new memory action dao.
	 *
	 * @author liadk
	 */
	public MemoryActionDao() {
		this.actions = Collections.synchronizedList(new ArrayList<>());
		this.nextId = new AtomicLong(1);
	}

	/* (non-Javadoc)
	 * @see smartspace.dao.ActionDao#create(smartspace.data.ActionEntity)
	 */
	@Override
	public ActionEntity create(ActionEntity actionEntity) {
		actionEntity.setActionSmartspace("2019B.nadav.peleg");
		actionEntity.setKey(new ActionKey(actionEntity.getActionSmartspace(), nextId.getAndIncrement()));
		this.actions.add(actionEntity);
		return actionEntity;
	}

	/**
	 * Gets the actions.
	 *
	 * @author liadk
	 * @return the actions
	 */
	public List<ActionEntity> getActions() {
		return actions;
	}

	/**
	 * Sets the actions.
	 *
	 * @author liadk
	 * @param actions the actions to set
	 */
	public void setActions(List<ActionEntity> actions) {
		this.actions = actions;
	}

	/**
	 * Gets the next id.
	 *
	 * @author liadk
	 * @return the nextId
	 */
	public AtomicLong getNextId() {
		return nextId;
	}

	/**
	 * Sets the next id.
	 *
	 * @author liadk
	 * @param nextId the nextId to set
	 */
	public void setNextId(AtomicLong nextId) {
		this.nextId = nextId;
	}

	/* (non-Javadoc)
	 * @see smartspace.dao.ActionDao#readAll()
	 */
	@Override
	public List<ActionEntity> readAll() {
		return this.actions;
	}

	/* (non-Javadoc)
	 * @see smartspace.dao.ActionDao#deleteAll()
	 */
	@Override
	public void deleteAll() {
		this.actions.clear();
	}

}
