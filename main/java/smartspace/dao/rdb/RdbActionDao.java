package smartspace.dao.rdb;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import smartspace.dao.EnhancedActionDao;
import smartspace.data.ActionEntity;
import smartspace.data.ActionKey;
import org.springframework.data.domain.Sort.Direction;

@Repository
public class RdbActionDao implements EnhancedActionDao {
	public static final String SEQUENCE_NAME = "actions_sequence";
	
	private ActionCrud actionCrud;
	private RdbSequenceDao sequenceGenerator;
	
	private String appSmartspace;
	
	/**
	 * @author liadkh
	 * @param actionCrud
	 * @param sequenceGenerator
	 */
	@Autowired
	public RdbActionDao(ActionCrud actionCrud, RdbSequenceDao sequenceGenerator) {
		super();
		this.actionCrud = actionCrud;
		this.sequenceGenerator = sequenceGenerator;
	}
	
	@Value("${smartspace.name}") 
	public void setAppSmartspace(String appSmartspace) {
		this.appSmartspace = appSmartspace;
	}

	@Override
	@Transactional
	public ActionEntity create(ActionEntity actionEntity) {
		actionEntity.setActionSmartspace(appSmartspace);
		actionEntity.setKey(new ActionKey(actionEntity.getActionSmartspace(), sequenceGenerator.generateNextId(SEQUENCE_NAME)));

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

	/**
	 * Read all with paging.
	 *
	 * @param size the size
	 * @param page the page
	 * @return the list
	 */
	@Override
	@Transactional(readOnly = true)
	public List<ActionEntity> readAllWithPaging(int size, int page) {
		return this.actionCrud.findAll(PageRequest.of(page, size)).getContent();
	}

	@Override
	@Transactional(readOnly = true)
	public List<ActionEntity> readAllWithPaging(String sortBy, int size, int page) {
		return this.actionCrud.findAll(PageRequest.of(page, size, Direction.ASC, sortBy)).getContent();
	}

	/**
	 * Import action.
	 *
	 * @param action the action
	 * @return the action entity
	 */
	@Override
	@Transactional
	public ActionEntity importAction(ActionEntity action) {
		if (action.getKey() != null) {
			return this.actionCrud.save(action);
		}
		return null;
	}
}
