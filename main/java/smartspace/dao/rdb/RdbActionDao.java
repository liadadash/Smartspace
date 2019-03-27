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
		this.nextId = new AtomicLong(100);
	}

	@Override
	@Transactional
	public ActionEntity create(ActionEntity actionEntity) {
		// SQL: INSERT INTO MESSAGES (ID, NAME) VALUES (?,?);
		// TODO replace this with id stored in db
		ActionKey actionKey = new ActionKey();
		actionKey.setId(nextId.getAndIncrement());
		actionEntity.setKey(actionKey);

		if (!this.actionCrud.existsById(actionEntity.getKey().getId())) {
			ActionEntity rv = this.actionCrud.save(actionEntity);
			return rv;
		} else {
			throw new RuntimeException("message already exists with key: " + actionEntity.getKey().getId());
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<ActionEntity> readAll() {
		List<ActionEntity> rv = new ArrayList<>();
		// SQL: SELECT
		this.actionCrud.findAll().forEach(rv::add);

		return rv;
	}

	@Override
	@Transactional
	public void deleteAll() {
		// SQL: DELETE
		this.actionCrud.deleteAll();
	}

}
