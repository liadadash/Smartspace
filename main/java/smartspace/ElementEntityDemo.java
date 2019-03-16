package smartspace;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import smartspace.dao.ElementDao;
import smartspace.data.ElementEntity;
import smartspace.data.Location;
import smartspace.data.util.EntityFactoryImpl;

@Component
public class ElementEntityDemo implements CommandLineRunner {
	private EntityFactoryImpl factory;
	private ElementDao<String> dao;

	public ElementEntityDemo() {
	}

	@Autowired
	public ElementEntityDemo(EntityFactoryImpl factory, ElementDao<String> dao) {
		this.factory = factory;
		this.dao = dao;
	}

	@Override
	public void run(String... args) throws Exception {

		Map<String, Object> moreAtributes = new HashMap<>();
		moreAtributes.put("title", "Food");
		moreAtributes.put("created", new Date());
		moreAtributes.put("langauge", "EN");
		moreAtributes.put("important", false);

		ElementEntity entity1 = this.factory.createNewElement("category", "category", new Location(0, 0), new Date(), "john123@gmail.com", "ShoppingList", false, moreAtributes);
		entity1.setElementSmartspace("ShoppingList");

		System.err.println("entity before saving to database: " + entity1);

		entity1 = this.dao.create(entity1);
		System.err.println("message after saving to database: " + entity1);

		ElementEntity updatedElement = new ElementEntity();

		// update some values
		updatedElement.setLocation(new Location(1, 0));
		updatedElement.setKey(entity1.getKey());

		// update moreAttributes
		Map<String, Object> updatedAttributes = new HashMap<>(moreAtributes);
		updatedAttributes.put("title", "Drinks");
		updatedAttributes.put("maxItems", 50);
		updatedElement.setMoreAttributes(updatedAttributes);

		// update in database
		this.dao.update(updatedElement);

		// load from database by id
		Optional<ElementEntity> entityLoad = this.dao.readById(entity1.getKey());
		if (entityLoad.isPresent()) {
			entity1 = entityLoad.get();
		} else {
			throw new RuntimeException("entity was lost after update");
		}
		
		System.err.println("entity after update: " + entity1);

		// delete all entities
		this.dao.deleteAll();
		if (this.dao.readAll().isEmpty()) {
			System.err.println("all entities deleted successfully");
		} else {
			throw new RuntimeException("some entities were not deleted");
		}

	}

}
