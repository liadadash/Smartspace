package smartspace.dao.rdb;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import smartspace.dao.ElementDao;
import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;

@Repository
public class RdbElementDao implements ElementDao<ElementKey> {
	private ElementCrud elementCrud;

	// TODO remove this
	private AtomicLong nextId;

	@Autowired
	public RdbElementDao(ElementCrud elementCrud) {
		super();
		this.elementCrud = elementCrud;

		// TODO remove this
		this.nextId = new AtomicLong(0);
	}

	@Override
	@Transactional
	public ElementEntity create(ElementEntity elementEntity) {
		elementEntity.setElementSmartspace("2019B.nadav.peleg");
		elementEntity.setKey(new ElementKey(elementEntity.getElementSmartspace(), nextId.getAndIncrement()));

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

}
