/**
 * @author liadkh	09-05-2019
 */
package smartspace.layout;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import smartspace.dao.EnhancedActionDao;
import smartspace.dao.EnhancedElementDao;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.ActionTypes;
import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;
import smartspace.data.UserEntity;
import smartspace.data.UserKey;
import smartspace.data.UserRole;
import smartspace.data.util.Faker;

/**
 * The Class ActionInvokeTests.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = { "spring.profiles.active=default, test" })
public class ActionInvokeTests {

	/** The base url. */
	private String baseUrl;

	/** The port. */
	private int port;

	/** The rest template. */
	private RestTemplate restTemplate;

	/** The user dao. */
	private EnhancedUserDao<UserKey> userDao;

	/** The element dao. */
	private EnhancedElementDao<ElementKey> elementDao;

	/** The action dao. */
	private EnhancedActionDao actionDao;

	/** The faker. */
	private Faker faker;

	/**
	 * Sets the port.
	 *
	 * @param port the new port
	 */
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
		this.restTemplate = new RestTemplate();
	}

	/**
	 * Sets the user dao.
	 *
	 * @param userDao the new user dao
	 */
	@Autowired
	public void setUserDao(EnhancedUserDao<UserKey> userDao) {
		this.userDao = userDao;
	}

	/**
	 * Sets the user dao.
	 *
	 * @param elementDao the new user dao
	 */
	@Autowired
	public void setUserDao(EnhancedElementDao<ElementKey> elementDao) {
		this.elementDao = elementDao;
	}

	/**
	 * Sets the user dao.
	 *
	 * @param actionDao the new user dao
	 */
	@Autowired
	public void setUserDao(EnhancedActionDao actionDao) {
		this.actionDao = actionDao;
	}

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init() {
		this.baseUrl = "http://localhost:" + port + "/smartspace/actions";
		this.faker = new Faker();
	}

	/**
	 * Tear down.
	 */
	@After
	public void tearDown() {
		this.userDao.deleteAll();
		this.elementDao.deleteAll();
		this.actionDao.deleteAll();
	}

	/**
	 * Test invoke new action with type echo.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testInvokeNewActionWithTypeEcho() throws Exception {
		// GIVEN database with one user as player and one element and empty action db
		UserEntity user = faker.entity().user(UserRole.PLAYER); // fake valid details
		ElementEntity element = faker.entity().element(); // fake valid details
		element.setCreatorSmartspace(user.getUserSmartspace());
		element.setCreatorEmail(user.getUserEmail());

		user = userDao.importUser(user);
		element = elementDao.importElement(element);

		// WHEN player user invoke action on element that exits in db with known type
		ActionBoundary actionBoundary = faker.boundary().action(element);
		Map<String, String> player = new TreeMap<String, String>();
		player.put("smartspace", element.getCreatorSmartspace());
		player.put("email", element.getCreatorEmail());
		actionBoundary.setPlayer(player);
		actionBoundary.setType(ActionTypes.ECHO.name());

		ActionBoundary rv = this.restTemplate.postForObject(baseUrl, actionBoundary, ActionBoundary.class);

		// THEN the database contains a single action that is the same as the return
		// action boundary
		assertThat(this.actionDao.readAll()).hasSize(1);
		ActionBoundary actBoundary = new ActionBoundary(this.actionDao.readAll().get(0));
		assertThat(actBoundary).isEqualToComparingOnlyGivenFields(rv, "actionKey", "type", "player", "properties",
				"element");
	}

	/**
	 * Test invoke new action with type not one of type.
	 *
	 * @throws Exception the exception
	 */
	@Test(expected = Exception.class)
	public void testInvokeNewActionWithTypeNotOneOfType() throws Exception {
		// GIVEN database with one user as player and one element and empty action db
		UserEntity user = faker.entity().user(UserRole.PLAYER); // fake valid details
		ElementEntity element = faker.entity().element(); // fake valid details
		element.setCreatorSmartspace(user.getUserSmartspace());
		element.setCreatorEmail(user.getUserEmail());

		user = userDao.importUser(user);
		element = elementDao.importElement(element);

		// WHEN player user invoke action on element that exits in db with no known type
		ActionBoundary actionBoundary = faker.boundary().action(element);
		Map<String, String> player = new TreeMap<String, String>();
		player.put("smartspace", element.getCreatorSmartspace());
		player.put("email", element.getCreatorEmail());
		actionBoundary.setPlayer(player);
		actionBoundary.setType("No none type");

		this.restTemplate.postForObject(baseUrl, actionBoundary, ActionBoundary.class);

		// THEN there is exception
	}

	/**
	 * Test invoke new action with type by manager.
	 *
	 * @throws Exception the exception
	 */
	@Test(expected = Exception.class)
	public void testInvokeNewActionWithTypeByManager() throws Exception {
		// GIVEN database with one user as player and one element and empty action db
		UserEntity user = faker.entity().user(UserRole.MANAGER); // fake valid details
		ElementEntity element = faker.entity().element(); // fake valid details
		element.setCreatorSmartspace(user.getUserSmartspace());
		element.setCreatorEmail(user.getUserEmail());

		user = userDao.importUser(user);
		element = elementDao.importElement(element);

		// WHEN player user invoke action on element that exits in db with no known type
		ActionBoundary actionBoundary = faker.boundary().action(element);
		Map<String, String> player = new TreeMap<String, String>();
		player.put("smartspace", element.getCreatorSmartspace());
		player.put("email", element.getCreatorEmail());
		actionBoundary.setPlayer(player);
		actionBoundary.setType(ActionTypes.ECHO.name());

		this.restTemplate.postForObject(baseUrl, actionBoundary, ActionBoundary.class);

		// THEN there is exception
	}
}
