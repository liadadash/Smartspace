package smartspace.dao;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;
import smartspace.data.Location;
import smartspace.data.util.EntityFactoryImpl;

//nofar 21.3

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties= {"spring.profiles.active=default"})
public class ElementDaoIntegrationTests {
	private ElementDao<ElementKey> dao;
	private EntityFactoryImpl factory;

	@Autowired
	public void setDao(ElementDao<ElementKey> dao) {
		this.dao = dao;
	}

	@Autowired
	public void seFactory(EntityFactoryImpl factory) {
		this.factory = factory;
	}

	@Before
	public void setup() {
		dao.deleteAll();
	}

	@After
	public void teardown() {
		dao.deleteAll();
	}

	@Test(expected = Exception.class)
	public void testCreateNullElement() throws Exception {
		// GIVEN nothing

		// WHEN I create null message
		this.dao.create(null);

		// THEN create method throws exception
	}

	@Test
	public void testCreateReadByIdDeleteById() throws Exception {
		// GIVEN dao is initialized and clean

		// WHEN i create element
		// AND read element by key
		// AND delete element by key

		Map<String, Object> moreAttributes = new HashMap<String, Object>();
		moreAttributes.put("item1", "test1");
		ElementEntity elementEntity = this.factory.createNewElement("ListElement", "testType", new Location(1, 1),
				new Date(), "test@gmail.com", "testSmartspace", false, moreAttributes);
		elementEntity.setElementSmartspace("2019B.nadav.peleg");
		
		ElementEntity elementInDB = this.dao.create(elementEntity);
		ElementEntity elementFromDB = this.dao.readById(elementInDB.getKey())
				.orElseThrow(() -> new RuntimeException("could not find message by key"));
		this.dao.deleteByKey(elementInDB.getKey());

		// THEN created element id >0
		// AND return the same element
		// AND the dao not contain the element
		assertThat(elementInDB.getKey().getId()).isNotNull().isGreaterThan(0);

		assertThat(elementFromDB).isNotNull()
				.extracting("name", "type", "creatorEmail", "expired", "moreAttributes", "creatorSmartspace")
				.containsExactly("ListElement", "testType", "test@gmail.com", false, moreAttributes, "testSmartspace");

		assertThat(this.dao.readAll()).isNotNull().doesNotContain(elementInDB);
	}

	@Test
	public void testCreateUpdateReadByIdDeleteAllReadAll() throws Exception {

		// GIVEN dao is initialized and clean

		// WHEN i create elements
		// AND update element
		// AND read by key elements
		// AND delete all elements
		// AND read all elements

		Map<String, Object> moreAttributes = new HashMap<String, Object>();
		moreAttributes.put("item1", "test1");
		ElementEntity elementEntity = this.factory.createNewElement("ListElement", "testType", new Location(1, 1),
				new Date(), "test@gmail.com", "testSmartspace", false, moreAttributes);
		elementEntity.setElementSmartspace("2019B.nadav.peleg");
		
		ElementEntity elementInDB = this.dao.create(elementEntity);
		ElementEntity update = new ElementEntity();
		update.setKey(elementInDB.getKey());
		update.setCreatorEmail("update@gmail.com");
		update.setCreatorSmartspace("updateSmartspace");

		this.dao.update(update);
		this.dao.readById(elementInDB.getKey())
				.orElseThrow(() -> new RuntimeException("could not find message by key"));
		this.dao.deleteAll();
		List<ElementEntity> rvElements = this.dao.readAll();

		// THEN created element id >0
		// AND the dao is empty
		assertThat(elementInDB.getKey().getId()).isNotNull().isGreaterThan(0);
		assertThat(rvElements.isEmpty());

	}

	@Test
	public void testReadAllFromEmptyDao() throws Exception {

		// GIVEN dao is initialized and clean

		// WHEN i read all elements
		List<ElementEntity> rvElements = this.dao.readAll();

		// THEN return null list
		assertThat(rvElements).isNullOrEmpty();
	}
}
