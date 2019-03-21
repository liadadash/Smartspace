package smartspace.dao.memory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import smartspace.dao.ElementDao;
import smartspace.data.ElementEntity;

@Repository
public class MemoryElementDao implements ElementDao<String> {

	private List<ElementEntity> elements;
	private AtomicLong nextId;

	public MemoryElementDao() {
		this.elements = Collections.synchronizedList(new ArrayList<>());
		this.nextId = new AtomicLong(1);
	}

	@Override
	public ElementEntity create(ElementEntity elementEntity) {
		elementEntity.setKey(elementEntity.getElementSmartspace() + "." + elementEntity.getType() + "#"
				+ this.nextId.getAndIncrement());
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
					.orElseThrow(() -> new RuntimeException("no element to update "));

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
			if (update.getElementSmartspace() != null)
				existing.setElementSmartspace(update.getElementSmartspace());

			if (update.getCreatorEmail() != null)
				existing.setCreatorEmail(update.getCreatorEmail());
			
			if (update.getCreatorSmartspace() != null)
				existing.setCreatorSmartspace(update.getCreatorSmartspace());
			
			// i didn't update the creationTimestamp 
		}
	}

	@Override
	public void deleteByKey(String elementKey) {
		synchronized (this.elements) {
			for (ElementEntity current : this.elements) {
				if (current.getKey().equals(elementKey))
				{
					this.elements.remove(current);
					break; // important iterating a list after it changed gives exception
				}
			}
		}
	}

	@Override
	public void delete(ElementEntity elementEntity) {
		synchronized (this.elements) {
			this.elements.remove(elementEntity);
		}
	}

	@Override
	public void deleteAll() {
		this.elements.clear();
	}

}
