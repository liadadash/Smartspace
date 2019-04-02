package smartspace.dao.rdb;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import smartspace.dao.ActionDao;
import smartspace.data.ActionEntity;
import smartspace.data.ActionKey;

@Repository
public class RdbActionDao implements ActionDao {
	private ActionCrud actionCrud;

	// TODO remove this
	private AtomicLong nextId;

	@Autowired
	public RdbActionDao(ActionCrud actionCrud) {
		super();
		this.actionCrud = actionCrud;
		
		// TODO remove this
		this.nextId = new AtomicLong(1);
	}

	@Override
	@Transactional
	public ActionEntity create(ActionEntity actionEntity) {
		actionEntity.setActionSmartspace("2019B.nadav.peleg");
		actionEntity.setKey(new ActionKey(actionEntity.getActionSmartspace(), nextId.getAndIncrement()));

		if (!this.actionCrud.existsById(actionEntity.getKey())) {
			ActionEntity rv = this.actionCrud.save(actionEntity);
			return rv;
		} else {
			throw new RuntimeException("action already exists with key: " + actionEntity.getKey());
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<ActionEntity> readAll() {
		List<ActionEntity> rv = new ArrayList<>();
		this.actionCrud.findAll().forEach(rv::add);

		return rv;
	}

	@Override
	@Transactional
	public void deleteAll() {
		this.actionCrud.deleteAll();
	}

}
