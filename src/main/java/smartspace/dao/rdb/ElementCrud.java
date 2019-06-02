package smartspace.dao.rdb;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;

/**
 * The Interface ElementCrud.
 * 
 * @author liadkh
 */
public interface ElementCrud extends
//CrudRepository<ElementEntity, ElementKey>{
		MongoRepository<ElementEntity, ElementKey> {

	/**
	 * Find all by expired.
	 *
	 * @param expired  the expired
	 * @param pageable the pageable
	 * @return the list
	 */
	public List<ElementEntity> findAllByExpired(boolean expired, Pageable pageable);

	/**
	 * Find all by name.
	 *
	 * @param name     the name
	 * @param pageable the pageable
	 * @return the list
	 */
	public List<ElementEntity> findAllByName(String name, Pageable pageable);

	/**
	 * Find all by type.
	 *
	 * @param type     the type
	 * @param pageable the pageable
	 * @return the list
	 */
	public List<ElementEntity> findAllByType(String type, Pageable pageable);

	/**
	 * Find all by name and expired.
	 *
	 * @param name     the name
	 * @param expired  the expired
	 * @param pageable the pageable
	 * @return the list
	 */
	public List<ElementEntity> findAllByNameAndExpired(String name, boolean expired, Pageable pageable);

	/**
	 * Find all by type and expired.
	 *
	 * @param type     the type
	 * @param expired  the expired
	 * @param pageable the pageable
	 * @return the list
	 */
	public List<ElementEntity> findAllByTypeAndExpired(String type, boolean expired, Pageable pageable);

	// TODO: change to radius search
	// search by location manager - include expired
	public List<ElementEntity> findAllByLocation_XBetweenAndLocation_YBetween(Double xMin, Double xMax, Double yMin,
			Double yMax, Pageable pageable);

	// TODO: change to radius search
	// search by location player - only expired = false
	public List<ElementEntity> findAllByExpiredFalseAndLocation_XBetweenAndLocation_YBetween(Double xMin, Double xMax,
			Double yMin, Double yMax, Pageable pageable);

	// find elements by creator and type
	public List<ElementEntity> findAllByCreatorSmartspaceAndCreatorEmailAndType(String smartspace, String email,
			String elementType, Pageable pageable);

	// find all shopping lists where moreAttributes.members contains user with {smartspace, email}
	@Query("{ 'moreAttributes.members': {'userSmartspace': ?0, 'userEmail': ?1}, 'type': ?2 }")
	public List<ElementEntity> findShoppingListsByUser(String smartspace, String email, String elementType, Pageable pageable);
	
	// find all items in list
	@Query("{ 'moreAttributes.listKey.smartspace': ?0, 'moreAttributes.listKey.id': ?1, 'type': ?2 }")
	public List<ElementEntity> findItemsByShoppingList(String listSmartspace, String listId, String elementType, Pageable pageable);


}
