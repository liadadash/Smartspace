package smartspace.dao.rdb;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import smartspace.dao.EnhancedElementDao;
import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;

@Repository
public class RdbElementDao implements EnhancedElementDao<ElementKey> {
	public static final String SEQUENCE_NAME = "elements_sequence";
	public static final String SHOPPING_LIST_TYPE = "ShoppingList";
	public static final String SHOPPING_ITEM_TYPE = "ShoppingItem";
	
	private ElementCrud elementCrud;
	private RdbSequenceDao sequenceGenerator;

	private String appSmartspace;

	/**
	 * Instantiates a new rdb element dao.
	 *
	 * @author liadkh
	 * @param elementCrud            the element crud
	 * @param genericIdGeneratorCrud the generic id generator crud
	 */
	public RdbElementDao(ElementCrud elementCrud, RdbSequenceDao sequenceGenerator) {
		super();
		this.elementCrud = elementCrud;
		this.sequenceGenerator = sequenceGenerator;
	}
	
	@Value("${smartspace.name}") 
	public void setAppSmartspace(String appSmartspace) {
		this.appSmartspace = appSmartspace;
	}

	@Override
	@Transactional
	public ElementEntity create(ElementEntity elementEntity) {
		elementEntity.setElementSmartspace(appSmartspace);
		elementEntity.setKey(new ElementKey(elementEntity.getElementSmartspace(), sequenceGenerator.generateNextId(SEQUENCE_NAME)));

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
	@Transactional(readOnly = true)
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

			existing.setExpired(update.getExpired());
		// Eyal said to not update key attributes
//		if (update.getElementSmartspace() != null)
//			existing.setElementSmartspace(update.getElementSmartspace());

		// this can not update (attributes of user key)
//		if (update.getCreatorSmartspace() != null)
//			existing.setCreatorSmartspace(update.getCreatorSmartspace());

		// this can not update (attributes of user key)
//		if (update.getCreatorEmail() != null)
//			existing.setCreatorEmail(update.getCreatorEmail());
		
		existing.setExpired(update.getExpired());

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
	@Transactional(readOnly = true)
	public List<ElementEntity> readAllWithPaging(int size, int page) {
		return this.elementCrud.findAll(PageRequest.of(page, size)).getContent();
	}

	/**
	 * Import element.
	 *
	 * @param element the element
	 * @return the element entity
	 */
	@Override
	@Transactional(readOnly = true)
	public List<ElementEntity> readAllWithPaging(String sortBy, int size, int page) {
		return this.elementCrud.findAll(PageRequest.of(page, size, Direction.ASC, sortBy)).getContent();
	}

	@Override
	@Transactional
	public ElementEntity importElement(ElementEntity element) {
		if (element.getKey() != null) {
			return this.elementCrud.save(element);
		}
		return null;
	}

	/**
	 * Read all using paging.
	 *
	 * @param showExpired the show expired
	 * @param size        the size
	 * @param page        the page
	 * @return the list
	 */
	@Override
	@Transactional(readOnly = true)
	public List<ElementEntity> readAllUsingPaging(boolean showExpired, int size, int page) {
		PageRequest p = PageRequest.of(page, size, Direction.ASC, "elementId");
		if (showExpired)
			return elementCrud.findAll(p).getContent();
		else
			return elementCrud.findAllByExpired(showExpired, PageRequest.of(page, size, Direction.ASC, "elementId"));
	}

	/**
	 * Read all with same values using paging.
	 *
	 * @param showExpired the show expired
	 * @param searchBy    the search by
	 * @param value       the value
	 * @param size        the size
	 * @param page        the page
	 * @return the list
	 */
	@Override
	@Transactional(readOnly = true)
	public List<ElementEntity> readAllWithSameValuesUsingPaging(boolean showExpired, String searchBy, String value, int size, int page) {
		PageRequest p = PageRequest.of(page, size, Direction.ASC, "elementId");

		switch (searchBy) {
		case "name":
			if (showExpired)
				return elementCrud.findAllByName(value, p);
			else
				return elementCrud.findAllByNameAndExpired(value, showExpired, p);

		case "type":
			if (showExpired)
				return elementCrud.findAllByType(value, p);
			else
				return elementCrud.findAllByTypeAndExpired(value, showExpired, p);

		default:
			break;
		}
		return null;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<ElementEntity> searchByLocation(boolean includeExpired, Double x, Double y, Double distance, int size, int page) {
		PageRequest pageable = PageRequest.of(page, size, Direction.ASC, "key");
		
		// linear search
		// TODO: change to radius search
		Double xMin = x - distance;
		Double xMax = x + distance;
		Double yMin = y - distance;
		Double yMax = y + distance;
		
		if (includeExpired) {
			return this.elementCrud.findAllByLocation_XBetweenAndLocation_YBetween(xMin, xMax, yMin, yMax, pageable);
		} 
		
		return this.elementCrud.findAllByExpiredFalseAndLocation_XBetweenAndLocation_YBetween(xMin, xMax, yMin, yMax, pageable);
	}

	@Override
	public List<ElementEntity> readAllListsByCreator(String creatorSmartspace, String creatorEmail, int size, int page) {
		PageRequest pageable = PageRequest.of(page, size, Direction.ASC, "creationTimestamp");
		return this.elementCrud.findAllByCreatorSmartspaceAndCreatorEmailAndType(creatorSmartspace, creatorEmail, SHOPPING_LIST_TYPE, pageable);
	}

	@Override
	public List<ElementEntity> readAllListsByMember(String userSmartspace, String userEmail, int size, int page) {
		PageRequest pageable = PageRequest.of(page, size, Direction.ASC, "creationTimestamp");
		
		return this.elementCrud.findShoppingListsByUser(userSmartspace, userEmail, SHOPPING_LIST_TYPE, pageable);
	}
	
	@Override
	public List<ElementEntity> readAllItemsByShoppingList(String smartspcae, String id, int size, int page) {
		PageRequest pageable = PageRequest.of(page, size, Direction.ASC, "creationTimestamp");
		return this.elementCrud.findItemsByShoppingList(smartspcae, id, SHOPPING_ITEM_TYPE, pageable);
	}

}
