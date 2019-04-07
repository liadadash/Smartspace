package smartspace.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
@TestPropertySource(properties = { "spring.profiles.active=default" })
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

	@Test
	public void testCreateSomeElementsDeleteById() throws Exception {
		// GIVEN dao is initialized and clean

		// WHEN i create some elements
		// AND read element by key
		// AND delete element by key'

		int size = 10;
		int chosenElementIndex = 5;
		String smartspace = "2019B.nadav.peleg";

		List<ElementEntity> elementsInDB = IntStream.range(1, size + 1)
				.mapToObj(i -> this.factory.createNewElement("test" + i, "testType" + i, new Location(), new Date(),
						"test" + i + "@test.com", smartspace, false, new HashMap<>()))
				.map(this.dao::create).collect(Collectors.toList());

		// AND the dao not contain the chosen element and the dao size is size-1

		ElementEntity chosenElement = elementsInDB.get(chosenElementIndex);
		this.dao.deleteByKey(chosenElement.getKey());
		elementsInDB.remove(chosenElement);

		assertThat(this.dao.readAll()).doesNotContain(chosenElement).hasSize(size - 1);

	}

	@Test(expected = Exception.class)
	public void testDeleteByKeyWithNullValue() throws Exception {
		// GIVEN nothing
		// WHEN I create null message
		this.dao.deleteByKey(null);
		// THEN deleteByKey method throws exception
	}

	@Test(expected = Exception.class)
	public void testReadByIdWithNullElement() throws Exception {
		// GIVEN nothing
		// WHEN I create null message
		this.dao.readById(null);
		// THEN deleteByKey method throws exception }
	}

	@Test(expected = Exception.class)
	public void testUpdateWithNullElement() throws Exception {
		// GIVEN nothing
		// WHEN I create null message
		this.dao.update(null);
		// THEN deleteByKey method throws exception
	}

	@Test(expected = Exception.class)
	public void testUpdateWithElementWithNullKey() throws Exception {
		// GIVEN nothing
		// WHEN I create null message
		Map<String, Object> moreAttributes = new HashMap<String, Object>();
		moreAttributes.put("item1", "test1");
		ElementEntity update = this.factory.createNewElement("ListElement", "testType", new Location(1, 1), new Date(),
				"test@gmail.com", "testSmartspace", false, moreAttributes);
		update.setElementSmartspace("2019B.nadav.peleg");
		update.setKey(null);

		this.dao.update(update);
		// THEN deleteByKey method throws exception }
	}

	@Test(expected = Exception.class)
	public void testReadByIdNullKey() throws Exception {
		// GIVEN nothing
		// WHEN I want to find element with null key
		this.dao.readById(null);
		// THEN readById method throws exception
	}

	@Test
	public void testDeleteAllOnEmptyDB() throws Exception{
		// GIVEN nothing
		// WHEN I want to delete all elements in empty DB
		this.dao.deleteAll();
		// THEN deleteAll method deleteAll
	
	}

	@Test(expected = Exception.class)
	public void testUpdateElementThatNotInDBWIthoutAnyKey() throws Exception{
		// GIVEN nothing
		// WHEN I want to update element that not in DB
		String smartspace = "2019B.nadav.peleg";
		ElementEntity update = new ElementEntity("test1",
				"test",
				new Location(1.0,2.0),
				new Date()
				, "test@gmail.com",
				smartspace,
				true, new HashMap<>());
		
		
		// THEN update method throws exception
		this.dao.update(update);

	}

	@Test(expected = Exception.class)
	public void testUpdateElementThatNotInDBWithRandomKey() throws Exception{
		// GIVEN nothing
		// WHEN I want to update element that not in DB
		String smartspace = "2019B.nadav.peleg";
		ElementEntity update = new ElementEntity("test1",
				"test",
				new Location(1.0,2.0),
				new Date()
				, "test@gmail.com",
				smartspace,
				true, new HashMap<>());
		update.setKey(new ElementKey(smartspace, -15));
		
		// THEN update method throws exception
		this.dao.update(update);

	}

	@Test(expected = Exception.class)
	public void testCreateReadByIdDeleteByIdAndUpdate() throws Exception{
		// GIVEN dao is initialized and clean

				// WHEN i create elements
				// AND read by key element
				// AND delete by id element


		Map<String, Object> moreAttributes = new HashMap<String, Object>();
		moreAttributes.put("item1", "test1");
		ElementEntity elementEntity = this.factory.createNewElement("ListElement", "testType", new Location(1, 1),
		new Date(), "test@gmail.com", "testSmartspace", false, moreAttributes);
		elementEntity.setElementSmartspace("2019B.nadav.peleg");
		
		ElementEntity elementInDB = this.dao.create(elementEntity);	


		ElementEntity fromDb =this.dao.readById(elementInDB.getKey())
				.orElseThrow(() -> new RuntimeException("could not find message by key"));
		
		this.dao.deleteByKey(fromDb.getKey());
		fromDb.setExpired(true);
		this.dao.update(fromDb);
		

		// THEN readById throws Exception
				
	}

	@Test
	public void testCreateUpdateReadByIdDeleteByIdReadAll() throws Exception {

		// GIVEN dao is initialized and clean

		// WHEN i create element
		// AND update element
		// AND read by key element
		// AND delete by key element
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
		this.dao.deleteByKey(update.getKey());
		
		List<ElementEntity> rvElements = this.dao.readAll();

		// THEN created element id >0
		// AND the dao is empty
		
		assertThat(elementInDB.getKey().getId()).isNotNull().isGreaterThan(0);
		assertThat(rvElements.isEmpty());

	}

}
