package smartspace.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;
import smartspace.data.Location;
import smartspace.data.util.EntityFactory;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = { "spring.profiles.active=default" })
public class MoreElementDaoIntegrationTests {

	private EnhancedElementDao<ElementKey> elementDao;
	private EntityFactory factory;

	@Autowired
	public void setElementDao(EnhancedElementDao<ElementKey> elementDao) {
		this.elementDao = elementDao;
	}

	@Autowired
	public void setFactory(EntityFactory factory) {
		this.factory = factory;
	}

	@After
	public void teardown() {
		this.elementDao.deleteAll();
	}

	@Test
	public void testReadAllWithPaginationOfEmptyDB() throws Exception {
		// GIVEN the database is empty

		// WHEN I read 10 elements after skipping first 10 elements
		List<ElementEntity> result = this.elementDao.readAllWithPaging(10, 1);
		// THEN I receive no results
		assertThat(result).hasSize(0).isEmpty();
	}

	@Test
	public void testReadAllWithPagination() throws Exception {
		// GIVEN the database contains 50 elements
		createSteamElementsToDB(0, 50);
		// WHEN I read 10 elements after skipping first 10 elements
		List<ElementEntity> result = this.elementDao.readAllWithPaging(10, 1);

		// THEN I receive 10 results
		assertThat(result).hasSize(10);
	}

	@Test
	public void testReadAllWithPaginationOfSmallerDB() throws Exception {
		// GIVEN the database contains 12 elements
		createSteamElementsToDB(0, 12);
		// WHEN I read 10 elements after skipping first 10 elements
		List<ElementEntity> result = this.elementDao.readAllWithPaging(10, 1);
		// THEN I receive 2 results
		assertThat(result).hasSize(2);
	}

	@Test
	public void testReadAllWithPaginationWithInvalidPage() throws Exception {
		// GIVEN the database contains 12 elements
		createSteamElementsToDB(0, 12);
		// WHEN I read 10 elements after skipping first 20 elements
		List<ElementEntity> result = this.elementDao.readAllWithPaging(10, 2);
		// THEN I receive no results
		assertThat(result).hasSize(0).isEmpty();
	}

	@Test(expected = Exception.class)
	public void testReadAllWithPaginationWithInvalidSize() throws Exception {
		// GIVEN the database contains 12 elements
		createSteamElementsToDB(0, 12);
		// WHEN I read -10 elements after skipping first 20 elements
		List<ElementEntity> result = this.elementDao.readAllWithPaging(-10, 2);
		// THEN I throw exception
		assertThat(result);
	}

	public void createSteamElementsToDB(int start, int end) {
		IntStream.range(start, end)
				.mapToObj(i -> this.factory.createNewElement("element" + i, "testType", new Location(1, 2), new Date(),
						"test@gamil.com", "testSmartspace", false, new HashMap<String, Object>()))
				.forEach(this.elementDao::create);

	}
}
