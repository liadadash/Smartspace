package smartspace;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import smartspace.dao.ElementDao;
import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;
import smartspace.data.Location;
import smartspace.data.util.EntityFactoryImpl;

@Component
@Profile("production")
public class ElementEntityDemo implements CommandLineRunner {
	private EntityFactoryImpl factory;
	private ElementDao<ElementKey> dao;

	public ElementEntityDemo() {
	}

	@Autowired
	public ElementEntityDemo(EntityFactoryImpl factory, ElementDao<ElementKey> dao) {
		this.factory = factory;
		this.dao = dao;
	}

	@Override
	public void run(String... args) throws Exception {

		String smartspace = "ShoppingList";
		System.err.println("\n---------- ElementEntityDemo ----------");

		Map<String, Object> moreAttributes = new HashMap<>();
		moreAttributes.put("title", "Food");
		moreAttributes.put("lastUpdate", new Date());
		moreAttributes.put("langauge", "EN");
		moreAttributes.put("important", false);

		ElementEntity entity1 = this.factory.createNewElement("category", "category", new Location(0, 0), new Date(),
				"john123@gmail.com", smartspace, false, moreAttributes);
		entity1.setElementSmartspace(smartspace);

		System.err.println("entity before saving to database: " + entity1);
		System.err.println("key = " + entity1.getKey());

		entity1 = this.dao.create(entity1);
		System.err.println("entity after saving to database: " + entity1);
		System.err.println("key = " + entity1.getKey());

		ElementEntity updatedElement = new ElementEntity();

		// update some values
		updatedElement.setLocation(new Location(1, 0));
		updatedElement.setKey(entity1.getKey());

		// update moreAttributes
		Map<String, Object> updatedAttributes = new HashMap<>(moreAttributes);
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

		// # ---------- check delete by key ---------- #

		moreAttributes = new HashMap<>();
		moreAttributes.put("title", "Dairy");
		moreAttributes.put("lastUpdate", new Date());

		ElementEntity entity2 = this.factory.createNewElement("category", "category", new Location(2, 0), new Date(),
				"david80@gmail.com", smartspace, false, moreAttributes);
		entity2.setElementSmartspace(smartspace);
		entity2 = this.dao.create(entity2);

		System.err.println("\nentity2 after saving to database: " + entity2);

		if (!this.dao.readAll().contains(entity2)) {
			throw new RuntimeException("failed to save entity2");
		}

		System.err.println("elements before delete: " + this.dao.readAll().size());
		this.dao.deleteByKey(entity2.getKey());
		System.err.println("elements after delete: " + this.dao.readAll().size());

		if (this.dao.readAll().contains(entity2)) {
			throw new RuntimeException("failed to delete entity2");
		}

		// delete all entities
		this.dao.deleteAll();
		if (this.dao.readAll().isEmpty()) {
			System.err.println("\nall entities deleted successfully");
		} else {
			throw new RuntimeException("\nsome entities were not deleted");
		}

	}

}
