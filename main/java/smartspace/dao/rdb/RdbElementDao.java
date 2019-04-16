package smartspace.dao.rdb;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import smartspace.dao.EnhancedElementDao;
import smartspace.data.ActionKey;
import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;

@Repository
public class RdbElementDao implements EnhancedElementDao<ElementKey> {
	private ElementCrud elementCrud;
	private GenericIdGeneratorCrud<ActionKey> genericIdGeneratorCrud;

	@Value("${smartspace.name}")
	private String appSmartspace;

	/**
	 * Instantiates a new rdb element dao.
	 *
	 * @author liadkh
	 * @param elementCrud            the element crud
	 * @param genericIdGeneratorCrud the generic id generator crud
	 */
	public RdbElementDao(ElementCrud elementCrud, GenericIdGeneratorCrud<ActionKey> genericIdGeneratorCrud) {
		super();
		this.elementCrud = elementCrud;
		this.genericIdGeneratorCrud = genericIdGeneratorCrud;
	}

	@Override
	@Transactional
	public ElementEntity create(ElementEntity elementEntity) {
		GenericIdGenerator nextId = this.genericIdGeneratorCrud.save(new GenericIdGenerator());
		elementEntity.setElementSmartspace(appSmartspace);
		elementEntity.setKey(new ElementKey(elementEntity.getElementSmartspace(), nextId.getId()));
		this.genericIdGeneratorCrud.delete(nextId);

		if (!this.elementCrud.existsById(elementEntity.getKey())) {
			ElementEntity rv = this.elementCrud.save(elementEntity);
			return rv;
		} else
			throw new RuntimeException("Element already exists with key: " + elementEntity.getKey());
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<ElementEntity> readById(ElementKey elementKey) {
		return this.elementCrud.findById(elementKey);
	}

	@Override
	public List<ElementEntity> readAll() {
		List<ElementEntity> rv = new ArrayList<>();

		this.elementCrud.findAll().forEach(rv::add);
		return rv;
	}

	@Override
	@Transactional
	public void update(ElementEntity update) {
		ElementEntity existing = this.readById(update.getKey())
				.orElseThrow(() -> new RuntimeException("no element to update"));

		if (update.getName() != null)
			existing.setName(update.getName());

		if (update.getType() != null)
			existing.setType(update.getType());

		if (update.getMoreAttributes() != null)
			existing.setMoreAttributes(update.getMoreAttributes());

		if (update.getLocation() != null)
			existing.setLocation(update.getLocation());

		// Eyal said to not update key attributes
//		if (update.getElementSmartspace() != null)
//			existing.setElementSmartspace(update.getElementSmartspace());

		// this can not update (attributes of user key)
//		if (update.getCreatorSmartspace() != null)
//			existing.setCreatorSmartspace(update.getCreatorSmartspace());

		// this can not update (attributes of user key)
//		if (update.getCreatorEmail() != null)
//			existing.setCreatorEmail(update.getCreatorEmail());

		this.elementCrud.save(existing);
	}

	@Override
	@Transactional
	public void deleteByKey(ElementKey elementKey) {
		this.elementCrud.deleteById(elementKey);
	}

	@Override
	@Transactional
	public void delete(ElementEntity elementEntity) {
		this.elementCrud.delete(elementEntity);
	}

	@Override
	@Transactional
	public void deleteAll() {
		this.elementCrud.deleteAll();
	}

	/**
	 * Read all with paging.
	 *
	 * @param size the size
	 * @param page the page
	 * @return the list
	 */
	@Override
	public List<ElementEntity> readAllWithPaging(int size, int page) {
		return this.elementCrud.findAll(PageRequest.of(page, size)).getContent();
	}

	/**
	 * Import elements.
	 *
	 * @param elements the elements
	 */
	@Override
	@Transactional
	public void importElements(ElementEntity[] elements) {
		Stream.of(elements).map(this.elementCrud::save);
	}

}
