package smartspace.dao.memory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import smartspace.dao.ElementDao;
import smartspace.data.ElementEntity;

public class MemoryElementDao implements ElementDao<String> {

	private List<ElementEntity> elements;

	public MemoryElementDao() {
		this.elements = Collections.synchronizedList(new ArrayList<>());
	}

	@Override
	public ElementEntity create(ElementEntity elementEntity) {
		// elementEntity.setKey(somthing)
		this.elements.add(elementEntity);
		return elementEntity;
	}

	@Override
	public Optional<ElementEntity> readById(String elementKey) {
		ElementEntity target = null;
		for (ElementEntity current : this.elements) {
			if (current.getKey().equals(elementKey))
				target = current;
		}

		if (target != null)
			return Optional.of(target);
		else
			return Optional.empty();

	}

	@Override
	public List<ElementEntity> readAll() {
		return this.elements;
	}

	@Override
	public void update(ElementEntity update) {
		synchronized (this.elements) {

			ElementEntity existing = this.readById(update.getKey())
					.orElseThrow(() -> new RuntimeException("not element to update "));

			if (update.getLocation() != null)
				existing.setLocation(update.getLocation());

			if (update.getMoreAttributes() != null)
				existing.setMoreAttributes(update.getMoreAttributes());

			if (update.getName() != null)
				existing.setName(update.getName());

			if (update.getType() != null)
				existing.setType(update.getType());

			// expired is a boolean
			existing.setExpired(update.isExpired());

			// not sure about it yet
			if (update.getElementSmartSpace() != null)
				existing.setElementSmartSpace(update.getElementSmartSpace());

			// i didn't update the creationTimestamp, creatorEmail , creatorSmartspace
		}
	}

	@Override
	public void deleteByKey(String elementKey) {
		for (ElementEntity current : this.elements) {
			if (current.getKey().equals(elementKey))
				this.elements.remove(current);
		}
	}

	@Override
	public void delete(ElementEntity elementEntity) {
		this.elements.remove(elementEntity);
	}

	@Override
	public void deleteAll() {
		this.elements.clear();
	}

}
