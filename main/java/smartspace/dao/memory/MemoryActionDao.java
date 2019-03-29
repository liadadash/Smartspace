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
 * @author liadk
 *
 */

//@Repository
public class MemoryActionDao implements ActionDao {

	private List<ActionEntity> actions;
	private AtomicLong nextId;

	/**
	 * @author liadk
	 *
	 */
	public MemoryActionDao() {
		this.actions = Collections.synchronizedList(new ArrayList<>());
		this.nextId = new AtomicLong(1);
	}

	@Override
	public ActionEntity create(ActionEntity actionEntity) {
		actionEntity.setActionSmartspace("2019B.nadav.peleg");
		actionEntity.setKey(new ActionKey(actionEntity.getActionSmartspace(), nextId.getAndIncrement()));
		this.actions.add(actionEntity);
		return actionEntity;
	}

	/**
	 * @author liadk
	 *
	 * @return the actions
	 */
	public List<ActionEntity> getActions() {
		return actions;
	}

	/**
	 * @author liadk
	 *
	 * @param actions the actions to set
	 */
	public void setActions(List<ActionEntity> actions) {
		this.actions = actions;
	}

	/**
	 * @author liadk
	 *
	 * @return the nextId
	 */
	public AtomicLong getNextId() {
		return nextId;
	}

	/**
	 * @author liadk
	 *
	 * @param nextId the nextId to set
	 */
	public void setNextId(AtomicLong nextId) {
		this.nextId = nextId;
	}

	@Override
	public List<ActionEntity> readAll() {
		return this.actions;
	}

	@Override
	public void deleteAll() {
		this.actions.clear();
	}

}
