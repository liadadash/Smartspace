package smartspace.dao.rdb;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import smartspace.dao.EnhancedActionDao;
import smartspace.data.ActionEntity;
import smartspace.data.ActionKey;
import smartspace.data.ElementKey;

@Repository
public class RdbActionDao implements EnhancedActionDao {
	private ActionCrud actionCrud;
	private GenericIdGeneratorCrud<ActionKey> genericIdGeneratorCrud;

	/** The element CRUD in order to check if the import action has the element. */
	private ElementCrud elementCrud;

	@Value("${smartspace.name}")
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

	/**
	 * Sets the element crud.
	 *
	 * @param elementCrud the elementCrud to set
	 */
	public void setElementCrud(ElementCrud elementCrud) {
		this.elementCrud = elementCrud;
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

	/**
	 * Read all with paging.
	 *
	 * @param size the size
	 * @param page the page
	 * @return the list
	 */
	@Override
	public List<ActionEntity> readAllWithPaging(int size, int page) {
		return this.actionCrud.findAll(PageRequest.of(page, size)).getContent();
	}

	/**
	 * Import actions.
	 *
	 * @param actions the actions
	 */
	@Override
	@Transactional
	public void importActions(ActionEntity[] actions) {
		for (ActionEntity action : actions) {
			elementCrud.findById(new ElementKey(action.getActionSmartspace(), Long.parseLong(action.getActionId())))
					.orElseThrow(() -> new RuntimeException("could not find element that the action is on"));
			this.actionCrud.save(action);
		}
	}

}
