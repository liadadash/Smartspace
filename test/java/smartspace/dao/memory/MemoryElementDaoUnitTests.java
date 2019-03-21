package smartspace.dao.memory;
///nofar 20.3

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import smartspace.data.ElementEntity;
import smartspace.data.Location;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties= {"spring.profiles.active=default"})
public class MemoryElementDaoUnitTests {

	@Test
	public void testCreateElementEntity() throws Exception {

		// GIVEN a dao is available
		MemoryElementDao dao = new MemoryElementDao();

		// WHEN we create new elementEntity
		// AND we invoke create on the dao

		Map<String, Object> moreAttributes = new HashMap<String, Object>();
		moreAttributes.put("item1", "test1");

		ElementEntity elementEntity = new ElementEntity("ListElement", "testType", new Location(1, 1.1), new Date(),
				"test@gmail.com", "testSmartspace", false, moreAttributes);
		ElementEntity rvElement = dao.create(elementEntity);

		// THEN the element was added to the dao
		// AND the rvElement has a key and the key > 0

		assertThat(dao.readAll()).usingElementComparatorOnFields("name").contains(elementEntity);

		assertThat(rvElement.getKey().getId()).isNotNull().isGreaterThan(0);
	}

	@Test
	public void testReadByIdElementEntity() throws Exception {

		// GIVEN a dao is available
		MemoryElementDao dao = new MemoryElementDao();

		// WHEN we create new elementEntity
		// AND we get by key the element

		Map<String, Object> moreAttributes = new HashMap<String, Object>();
		moreAttributes.put("item1", "test1");

		ElementEntity elementEntity = new ElementEntity("ListElement", "testType", new Location(1, 1.1), new Date(),
				"test@gmail.com", "testSmartspace", false, moreAttributes);
		elementEntity.setElementSmartspace("2019B.nadav.peleg");

		ElementEntity elementInDB = dao.create(elementEntity);
		ElementEntity rvElement = dao.readById(elementInDB.getKey())
				.orElseThrow(() -> new RuntimeException("could not find element by key"));

		// THEN return the same element

		assertThat(rvElement).isNotNull()
				.extracting("name", "type", "creatorEmail", "expired", "moreAttributes", "creatorSmartspace")
				.containsExactly("ListElement", "testType", "test@gmail.com", false, moreAttributes, "testSmartspace");
	}

	@Test
	public void testReadAll() throws Exception{
		// GIVEN a dao is available
		MemoryElementDao dao=new MemoryElementDao();
		
		// WHEN we create three new elements 
		// AND we get all three elements 
		
		Map<String, Object> moreAttributes1=new HashMap<String, Object>();
		moreAttributes1.put("item1", "test1");
		Map<String, Object> moreAttributes2=new HashMap<String, Object>();
		moreAttributes2.put("item2", "test2");
		Map<String, Object> moreAttributes3=new HashMap<String, Object>();
		moreAttributes3.put("item3", "test3");
		
		//element 1
		ElementEntity elementEntity1=new ElementEntity("ListElement1","testType1",new Location(1,1),new Date(),"test1@gmail.com","test1Smartspace",false,moreAttributes1);
		dao.create(elementEntity1);
		//element 2
		ElementEntity elementEntity2=new ElementEntity("ListElement2","testType2",new Location(2,2),new Date(),"test2@gmail.com","test2Smartspace",false,moreAttributes2);
		dao.create(elementEntity2);
		//element 3
		ElementEntity elementEntity3=new ElementEntity("ListElement3","testType3",new Location(3,3),new Date(),"test3@gmail.com","test3Smartspace",false,moreAttributes3);
		dao.create(elementEntity3);
		
		List<ElementEntity> rvElements=dao.readAll();
		
		// THEN return the same elements
		assertThat(rvElements)
		.isNotNull()
		.contains(elementEntity1)
		.contains(elementEntity2)
		.contains(elementEntity2);
	
	}

	@Test
	public void testUpdateElement() throws Exception {
		// GIVEN a dao is available
		MemoryElementDao dao = new MemoryElementDao();

		// WHEN we create new element
		// AND update element

		Map<String, Object> moreAttributes1 = new HashMap<String, Object>();
		moreAttributes1.put("item1", "test1");
		
		ElementEntity elementEntity1 = new ElementEntity("ListElement1", "testType1", new Location(1, 1), new Date(),
				"test1@gmail.com", "test1Smartspace", false, moreAttributes1);
		elementEntity1.setElementSmartspace("2019B.nadav.peleg");
		
		dao.create(elementEntity1);
		
		ElementEntity update = new ElementEntity();
		update.setKey(elementEntity1.getKey());
		update.setName("update1");
		
		dao.update(update);
		ElementEntity rvUpdateElement=dao.readById(elementEntity1.getKey()).orElseThrow(()-> new RuntimeException("could not find element by key"));

		// THEN dao contains the update element
		assertThat(rvUpdateElement)
		.isNotNull()
		.extracting("name")
		.containsExactly("update1");
		
	}
	
	@Test(expected=Exception.class)
	public void testDeleteElementByKey() throws Exception{
		// GIVEN a dao is available
		MemoryElementDao dao = new MemoryElementDao();
		
		// WHEN we create new element
		// AND delete element by id
		
		Map<String, Object> moreAttributes = new HashMap<String, Object>();
		moreAttributes.put("item1", "test1");

		ElementEntity elementEntity = new ElementEntity("ListElement", "testType", new Location(1, 1), new Date(),
				"test@gmail.com", "testSmartspace", false, moreAttributes);
		ElementEntity elementInDB = dao.create(elementEntity);
		dao.deleteByKey(elementInDB.getKey());
		
		//THEN dao will not contain the element
		assertThat(dao.readById(elementInDB.getKey()).orElseThrow(() -> new RuntimeException("could not find element by key")));
		
		
		
	}
	
	@Test(expected=Exception.class)
	public void testDeleteElement() throws Exception{
		// GIVEN a dao is available
		MemoryElementDao dao = new MemoryElementDao();
		
		// WHEN we create new element
		// AND delete element
		
		Map<String, Object> moreAttributes = new HashMap<String, Object>();
		moreAttributes.put("item1", "test1");

		ElementEntity elementEntity = new ElementEntity("ListElement", "testType", new Location(1, 1), new Date(),
				"test@gmail.com", "testSmartspace", false, moreAttributes);
		ElementEntity elementInDB = dao.create(elementEntity);
		dao.delete(elementInDB);
		
		//THEN dao will not contain the element
		assertThat(dao.readById(elementInDB.getKey()).orElseThrow(() -> new RuntimeException("could not find element by key")));
		
		
		
	}
	
	@Test
	public void testDeleteAllElements() throws Exception{
		// GIVEN a dao is available
		MemoryElementDao dao = new MemoryElementDao();
		
		// WHEN we create two new elements
		// AND delete all elements 
		
		Map<String, Object> moreAttributes1 = new HashMap<String, Object>();
		moreAttributes1.put("item1", "test1");
		Map<String, Object> moreAttributes2=new HashMap<String, Object>();
		moreAttributes2.put("item2", "test2");
		
		//element 1
		ElementEntity elementEntity1=new ElementEntity("ListElement1","testType1",new Location(1,1),new Date(),"test1@gmail.com","test1Smartspace",false,moreAttributes1);
	    dao.create(elementEntity1);
		//element 2
		ElementEntity elementEntity2=new ElementEntity("ListElement2","testType2",new Location(2,2),new Date(),"test2@gmail.com","test2Smartspace",false,moreAttributes2);
		dao.create(elementEntity2);
		
		dao.deleteAll();
		
		//THEN dao will not contain elements
		assertThat(dao.readAll().isEmpty());
	
	}
	
	
}