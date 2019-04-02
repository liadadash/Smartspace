/**
 * @author liadkh	02-04-2019
 */
package smartspace.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.HashMap;
import java.util.Set;
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

import smartspace.data.ActionEntity;
import smartspace.data.ActionKey;
import smartspace.data.util.EntityFactoryImpl;

/**
 * @author liadkh
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = { "spring.profiles.active=default" })
public class MoreActionDaoIntegrationTests {

	private ActionDao dao;
	private EntityFactoryImpl factory;

	@Autowired
	public void setDao(ActionDao dao) {
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

	@Test
	public void testSaveActionToDaoTheActionSaveWithID() throws Exception {
		// Given the database is clean

		// When I create a action to DB
		ActionEntity actionEntity = factory.createNewAction(null, null, "Test", new Date(), "Test@mail.com", "Tester",
				new HashMap<>());
		ActionEntity rv = dao.create(actionEntity);
		// Then return action is not empty
		// And the action save with id
		assertThat(rv).isNotNull();
		assertThat(rv.getKey()).isNotNull().extracting("id").isNotNull();
	}

	@Test
	public void testSaveManyActionToDaoTheActionSaveWithUniqueID() throws Exception {
		// Given the database is clean

		// When I create 50 action to DB

		int size = 50;
		Set<ActionKey> keys = IntStream.range(0, size)
				.mapToObj(i -> this.factory.createNewAction(null, null, "Test #" + i, new Date(), "Test@mail.com",
						"TESTER", new HashMap<>()))
				.map(this.dao::create).map(ActionEntity::getKey).collect(Collectors.toSet());

		// Then all the 50 action save to with unique ids
		assertThat(keys).isNotNull().hasSize(size);
	}

	@Test
	public void testreadActionsFromDao() throws Exception {
		// Given the database with 12 actions
		
		int size = 12;
		IntStream.range(0, size).mapToObj(i -> this.factory.createNewAction(null, null, "Test #" + i, new Date(),
				"Test@mail.com", "TESTER", new HashMap<>())).forEach(this.dao::create);
		
		// When I read all the action in the database

		java.util.List<ActionEntity> allActions=  dao.readAll();
		// Then I get 12 actions
		assertThat(allActions).isNotNull().hasSize(size);
	}
}
