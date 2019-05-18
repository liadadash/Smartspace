package smartspace.layout;
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

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = { "spring.profiles.active=default,test" })
public class AddMoreActionInvokeIntegartionTests {
	private String baseUrl;

	private int port;
	private RestTemplate restTemplate;
	private EnhancedUserDao<UserKey> userDao;
	private EnhancedElementDao<ElementKey> elementDao;
	private EnhancedActionDao actionDao;
	private Faker faker;

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
		this.restTemplate = new RestTemplate();
	}

	@Autowired
	public void setUserDao(EnhancedUserDao<UserKey> userDao) {
		this.userDao = userDao;
	}

	@Autowired
	public void setUserDao(EnhancedElementDao<ElementKey> elementDao) {
		this.elementDao = elementDao;
	}

	@Autowired
	public void setUserDao(EnhancedActionDao actionDao) {
		this.actionDao = actionDao;
	}

	@PostConstruct
	public void init() {
		this.baseUrl = "http://localhost:" + port + "/smartspace/actions";
		this.faker = new Faker();
	}

	@After
	public void tearDown() {
		this.userDao.deleteAll();
		this.elementDao.deleteAll();
		this.actionDao.deleteAll();
	}

	@Test(expected = Exception.class)
	public void testInvokeNewActionByAdmin() throws Exception {
		// GIVEN database with one user as admin and one element and empty action db
		UserEntity admin = faker.entity().user(UserRole.ADMIN); // fake valid details
		ElementEntity element = faker.entity().element(); // fake valid details
		element.setCreatorSmartspace(admin.getUserSmartspace());
		element.setCreatorEmail(admin.getUserEmail());

		userDao.create(admin);
		elementDao.create(element);

		// WHEN player user invoke action on element that exits in db with known type
		ActionBoundary actionBoundary = faker.boundary().action(element);
		Map<String, String> player = new TreeMap<String, String>();
		player.put("smartspace", element.getCreatorSmartspace());
		player.put("email", element.getCreatorEmail());
		actionBoundary.setPlayer(player);
		actionBoundary.setType(ActionTypes.ECHO.name());

	  this.restTemplate.postForObject(baseUrl, actionBoundary, ActionBoundary.class);

		// THEN throw exception
		
		
	}
	
	@Test(expected = Exception.class)
	public void testInvokeNewActionWithElementNotInDbByPlayer() throws Exception {
		// GIVEN database with one user as player and empty action and elements db
		UserEntity user = faker.entity().user(UserRole.PLAYER); // fake valid details
		ElementEntity element = faker.entity().element(); // fake valid details
		element.setCreatorSmartspace(user.getUserSmartspace());
		element.setCreatorEmail(user.getUserEmail());

		userDao.create(user);
	
		// WHEN player user invoke action on element that NOT exits in db with known type
		ActionBoundary actionBoundary = faker.boundary().action(element);
		Map<String, String> player = new TreeMap<String, String>();
		player.put("smartspace", element.getCreatorSmartspace());
		player.put("email", element.getCreatorEmail());
		actionBoundary.setPlayer(player);
		actionBoundary.setType(ActionTypes.ECHO.name());

	  this.restTemplate.postForObject(baseUrl, actionBoundary, ActionBoundary.class);

		// THEN throw exception
	}
}
