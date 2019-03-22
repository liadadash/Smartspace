package smartspace.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import smartspace.dao.memory.MemoryActionDao;
import smartspace.data.ActionEntity;
import smartspace.data.util.EntityFactoryImpl;

// Amit 22/3
@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = { "spring.profiles.active=default" })
public class MemoryActionDaoIntegrationTests {

	private MemoryActionDao dao;
	private EntityFactoryImpl factory;

	@Autowired
	public void setDao(MemoryActionDao dao) {
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
	public void testCreateNullAction() throws Exception {
		// GIVEN nothing

		// WHEN I create null message
		this.dao.create(null);

		// THEN create method throws exception
	}

	@Test
	public void testReadAllFromEmptyDao() throws Exception {

		// GIVEN dao is initialized and clean

		// WHEN i read all actions
		List<ActionEntity> rvActions = this.dao.readAll();

		// THEN return null list
		assertThat(rvActions).isNullOrEmpty();
	}
}
