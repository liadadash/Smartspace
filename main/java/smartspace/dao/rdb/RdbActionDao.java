package smartspace.dao.rdb;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import smartspace.dao.ActionDao;
import smartspace.data.ActionEntity;
import smartspace.data.ActionKey;

@Repository
public class RdbActionDao implements ActionDao {
	private ActionCrud actionCrud;
	private GenericIdGeneratorCrud<ActionKey> genericIdGeneratorCrud;
	
	@Value( "${app.smartspace}" )
	private String appSmartspace;

	/**
	 * @author liadkh
	 * @param actionCrud
	 * @param genericIdGeneratorCrud
	 */
	@Autowired
	public RdbActionDao(ActionCrud actionCrud, GenericIdGeneratorCrud<ActionKey> genericIdGeneratorCrud) {
		super();
		this.actionCrud = actionCrud;
		this.genericIdGeneratorCrud = genericIdGeneratorCrud;
	}

	@Override
	@Transactional
	public ActionEntity create(ActionEntity actionEntity) {
		GenericIdGenerator nextId = this.genericIdGeneratorCrud.save(new GenericIdGenerator());
		actionEntity.setActionSmartspace(appSmartspace);
		actionEntity.setKey(new ActionKey(actionEntity.getActionSmartspace(), nextId.getId()));
		this.genericIdGeneratorCrud.delete(nextId);

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
