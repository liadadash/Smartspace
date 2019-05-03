package smartspace.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;
import smartspace.data.UserEntity;
import smartspace.data.UserKey;
import smartspace.data.UserRole;
import smartspace.data.util.Faker;
import smartspace.infra.ActionService;
import smartspace.infra.ElementService;
import smartspace.layout.ActionBoundary;
import smartspace.layout.ElementBoundary;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = { "spring.profiles.active=default" })
public class ActionControllerIntegrationTests {

	private String baseUrl;
	private String elementUrl;
	private int port;
	private RestTemplate restTemplate;
	private EnhancedActionDao actionDao;
	private EnhancedUserDao<UserKey> userDao;
	private EnhancedElementDao<ElementKey> elementDao;
	private ActionService actionService;
	private ElementService elementService;
	private Faker faker;

	@Autowired
	public void setActionDao(EnhancedActionDao actionDao) {
		this.actionDao = actionDao;
	}

	@Autowired
	public void setUserDao(EnhancedUserDao<UserKey> userDao) {
		this.userDao = userDao;
	}

	@Autowired
	public void setElementDao(EnhancedElementDao<ElementKey> elementDao) {
		this.elementDao = elementDao;
	}

	@Autowired
	public void setActionService(ActionService actionService) {
		this.actionService = actionService;
	}

	@Autowired
	public void setElementService(ElementService elementService) {
		this.elementService = elementService;
	}

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
		this.restTemplate = new RestTemplate();
	}

	@PostConstruct
	public void init() {
		this.baseUrl = "http://localhost:" + port + "/smartspace/admin/actions/{adminSmartspace}/{adminEmail}";
		this.elementUrl = "http://localhost:" + port + "/smartspace/admin/elements/{adminSmartspace}/{adminEmail}";
		this.faker = new Faker();
	}

	@Before
	public void setUp() throws Exception {
		this.actionDao.deleteAll();
		this.userDao.deleteAll();
		this.elementDao.deleteAll();
	}

	@After
	public void tearDown() {
		this.actionDao.deleteAll();
		this.userDao.deleteAll();
		this.elementDao.deleteAll();
	}

	@Test
	public void testPostNewActionWhenTheRelevantElementInDB() throws Exception {
		// GIVEN the database contain admin and element

		UserEntity admin = this.faker.entity().user();
		admin.setRole(UserRole.ADMIN);
		this.userDao.create(admin);

		ElementBoundary[] element = this.faker.boundary().elementArray(1);
		this.restTemplate.postForObject(this.elementUrl, element, ElementBoundary[].class, admin.getUserSmartspace(),
				admin.getUserEmail());

		// WHEN i post new action
		ActionBoundary[] action = this.faker.boundary().actionArray(element, 1);
		this.restTemplate.postForObject(this.baseUrl, action, ActionBoundary[].class, admin.getUserSmartspace(),
				admin.getUserEmail());

		// THEN the database contains a single action
		assertThat(this.actionDao.readAll()).hasSize(1);

	}

	@Test(expected = Exception.class)
	public void testPostNewActionWhenTheRelevantElementNotInDB() throws Exception {
		// GIVEN the database contain admin and element

		UserEntity admin = this.faker.entity().user();
		admin.setRole(UserRole.ADMIN);
		this.userDao.create(admin);

		ElementBoundary[] elements = this.faker.boundary().elementArray(1);
		this.restTemplate.postForObject(this.elementUrl, elements, ElementBoundary[].class, admin.getUserSmartspace(),
				admin.getUserEmail());

		// element not in id DB
		ElementBoundary[] elementsNotInDB = this.faker.boundary().elementArray(1);

		// WHEN i post new action that not match element in DB

		ActionBoundary[] action = this.faker.boundary().actionArray(elementsNotInDB, 1);
		this.restTemplate.postForObject(this.baseUrl, action, ActionBoundary[].class, admin.getUserSmartspace(),
				admin.getUserEmail());

		// THEN the database contains a single action
		assertThat(this.actionDao.readAll()).hasSize(0);

	}

	@Test(expected = Exception.class)
	public void testPostNewActionWhenNoElementsInDB() throws Exception {
		// GIVEN the database contain admin

		UserEntity admin = this.faker.entity().user();
		admin.setRole(UserRole.ADMIN);
		this.userDao.create(admin);

		// element not in id DB
		ElementBoundary[] elementsNotInDB = this.faker.boundary().elementArray(1);

		// WHEN i post new action that not match element in DB

		ActionBoundary[] action = this.faker.boundary().actionArray(elementsNotInDB, 1);
		this.restTemplate.postForObject(this.baseUrl, action, ActionBoundary[].class, admin.getUserSmartspace(),
				admin.getUserEmail());

		// THEN the database contains a single action
		assertThat(this.actionDao.readAll()).hasSize(0);

	}

	@Test(expected = Exception.class)
	public void testPostNewActionWithPlayerRole() throws Exception {
		// GIVEN the database contain admin and element

		UserEntity admin = this.faker.entity().user();
		admin.setRole(UserRole.ADMIN);
		this.userDao.create(admin);

		UserEntity player = this.faker.entity().user();
		admin.setRole(UserRole.PLAYER);
		this.userDao.create(admin);

		ElementBoundary[] elements = this.faker.boundary().elementArray(1);
		this.restTemplate.postForObject(this.elementUrl, elements, ElementBoundary[].class, admin.getUserSmartspace(),
				admin.getUserEmail());

		// WHEN i post new action that not match element in DB

		ActionBoundary[] action = this.faker.boundary().actionArray(elements, 1);
		this.restTemplate.postForObject(this.baseUrl, action, ActionBoundary[].class, player.getUserSmartspace(),
				player.getUserEmail());

		// THEN the database contains a single action
		assertThat(this.actionDao.readAll()).hasSize(0);

	}

	@Test(expected = Exception.class)
	public void testPostNewActionWithManagerRole() throws Exception {
		// GIVEN the database contain admin and element

		UserEntity admin = this.faker.entity().user();
		admin.setRole(UserRole.ADMIN);
		this.userDao.create(admin);

		UserEntity manager = this.faker.entity().user();
		admin.setRole(UserRole.MANAGER);
		this.userDao.create(admin);

		ElementBoundary[] elements = this.faker.boundary().elementArray(1);
		this.restTemplate.postForObject(this.elementUrl, elements, ElementBoundary[].class, admin.getUserSmartspace(),
				admin.getUserEmail());

		// WHEN i post new action that not match element in DB

		ActionBoundary[] action = this.faker.boundary().actionArray(elements, 1);
		this.restTemplate.postForObject(this.baseUrl, action, ActionBoundary[].class, manager.getUserSmartspace(),
				manager.getUserEmail());

		// THEN the database contains a single action
		assertThat(this.actionDao.readAll()).hasSize(0);

	}

	@Test
	public void testGetAllActionsUsingPagination() throws Exception {
		// GIVEN the database contains 3 actions and admin

		UserEntity admin = this.faker.entity().user();
		admin.setRole(UserRole.ADMIN);
		this.userDao.create(admin);

		int size = 3;
		IntStream.range(1, size + 1).mapToObj(i -> new ActionEntity("demo" + i, "2019B.nadav.peleg", "test", new Date(),
				"test@test.com", "2019B.nadav.peleg", new HashMap<>())).forEach(this.actionDao::create);

		// WHEN I GET actions of size 10 and page 0
		ActionBoundary[] response = this.restTemplate.getForObject(this.baseUrl + "?size={size}&page={page}",
				ActionBoundary[].class, admin.getUserSmartspace(), admin.getUserEmail(), 10, 0);

		// THEN I receive 3 actions
		assertThat(response).hasSize(size);
	}

	@Test
	public void testGetAllACtionsUsingPaginationAndValidContent() throws Exception {
		// GIVEN the database contains 3 actions and admin

		UserEntity admin = this.faker.entity().user();
		admin.setRole(UserRole.ADMIN);
		this.userDao.create(admin);

		int size = 3;
		List<ActionBoundary> all = IntStream.range(1, size + 1)
				.mapToObj(i -> new ActionEntity("demo" + i, "2019B.nadav.peleg", "test", new Date(), "test@test.com",
						"2019B.nadav.peleg", new HashMap<>()))
				.map(this.actionDao::create).map(ActionBoundary::new).collect(Collectors.toList());

		// WHEN I GET actions of size 10 and page 0
		ActionBoundary[] response = this.restTemplate.getForObject(this.baseUrl + "?size={size}&page={page}",
				ActionBoundary[].class, admin.getUserSmartspace(), admin.getUserEmail(), 10, 0);

		// THEN I receive 3 actions
		assertThat(response)
				.usingElementComparatorOnFields("actionKey", "type", "created", "player", "properties", "element")
				.containsExactlyElementsOf(all);
	}

	@Test
	public void testGetAllACtionsUsingPaginationAndValidContentFromSecondPage() throws Exception {
		// GIVEN the database contains 11 actions and admin

		UserEntity admin = this.faker.entity().user();
		admin.setRole(UserRole.ADMIN);
		this.userDao.create(admin);

		int size = 11;
		List<ActionEntity> all = IntStream
				.range(0, size).mapToObj(i -> new ActionEntity("demo" + i, "2019B.nadav.peleg", "test", new Date(),
						"test@test.com", "2019B.nadav.peleg", new HashMap<>()))
				.map(this.actionDao::create).collect(Collectors.toList());

		ActionBoundary last = all.stream().skip(size - 1).limit(1).map(ActionBoundary::new).findFirst()
				.orElseThrow(() -> new RuntimeException("no actions after skipping"));

		// WHEN I GET actions of size 10 and page 1
		ActionBoundary[] response = this.restTemplate.getForObject(this.baseUrl + "?size={size}&page={page}",
				ActionBoundary[].class, admin.getUserSmartspace(), admin.getUserEmail(), 10, 1);

		// THEN I receive 1 actions (last action)
		assertThat(response).usingElementComparator((b1, b2) -> b1.toString().compareTo(b2.toString()))
				.containsExactly(last);
	}

	@Test
	public void testGetAllActionsUsingPaginationOfSecondNonExistingPage() throws Exception {
		// GIVEN the database contains 10 actions and admin

		UserEntity admin = this.faker.entity().user();
		admin.setRole(UserRole.ADMIN);
		this.userDao.create(admin);

		int size = 10;
		IntStream
				.range(0, size).mapToObj(i -> new ActionEntity("demo" + i, "2019B.nadav.peleg", "test", new Date(),
						"test@test.com", "2019B.nadav.peleg", new HashMap<>()))
				.map(this.actionDao::create).collect(Collectors.toList());

		// WHEN I GET actions of size 10 and page 1
		ActionBoundary[] response = this.restTemplate.getForObject(this.baseUrl + "?size={size}&page={page}",
				ActionBoundary[].class, admin.getUserSmartspace(), admin.getUserEmail(), 10, 1);

		// THEN I receive response in empty
		assertThat(response).isEmpty();
	}

	@Test(expected = Exception.class)
	public void testGetAllActionsUsingPaginationWithPlayerRole() throws Exception {
		// GIVEN the database contains 3 actions and player

		UserEntity player = this.faker.entity().user();
		player.setRole(UserRole.PLAYER);
		this.userDao.create(player);

		int size = 3;
		IntStream.range(1, size + 1).mapToObj(i -> new ActionEntity("demo" + i, "2019B.nadav.peleg", "test", new Date(),
				"test@test.com", "2019B.nadav.peleg", new HashMap<>())).forEach(this.actionDao::create);

		// WHEN I GET actions of size 10 and page 0
		this.restTemplate.getForObject(this.baseUrl + "?size={size}&page={page}", ActionBoundary[].class,
				player.getUserSmartspace(), player.getUserEmail(), 10, 0);

	}

	@Test(expected = Exception.class)
	public void testGetAllActionsUsingPaginationWithManagerRole() throws Exception {
		// GIVEN the database contains 3 actions and manager

		UserEntity manager = this.faker.entity().user();
		manager.setRole(UserRole.MANAGER);
		this.userDao.create(manager);

		int size = 3;
		IntStream.range(1, size + 1).mapToObj(i -> new ActionEntity("demo" + i, "2019B.nadav.peleg", "test", new Date(),
				"test@test.com", "2019B.nadav.peleg", new HashMap<>())).forEach(this.actionDao::create);

		// WHEN I GET actions of size 10 and page 0
		this.restTemplate.getForObject(this.baseUrl + "?size={size}&page={page}", ActionBoundary[].class,
				manager.getUserSmartspace(), manager.getUserEmail(), 10, 0);

	}

	@Test
	public void testGetAllActionsUsingPaginationOfSecondNonExistingPageWithService() throws Exception {
		// GIVEN the database contains 10 actions and admin

		UserEntity admin = this.faker.entity().user();
		admin.setRole(UserRole.ADMIN);
		this.userDao.create(admin);

		int size = 10;

		List<ElementEntity> elements = this.faker.entity().elementList(size);
		List<ActionEntity> actions = this.faker.entity().actionList(elements, size);

		this.elementService.importElements(elements, admin.getUserSmartspace(), admin.getUserEmail());
		this.actionService.importActions(actions, admin.getUserSmartspace(), admin.getUserEmail());

		// WHEN I GET actions of size 10 and page 1
		ActionBoundary[] response = this.restTemplate.getForObject(this.baseUrl + "?size={size}&page={page}",
				ActionBoundary[].class, admin.getUserSmartspace(), admin.getUserEmail(), 10, 1);

		// THEN I receive response in empty
		assertThat(response).isEmpty();
	}

	@Test
	public void testGetAllActionsUsingPaginationOfFirstPageWithService() throws Exception {
		// GIVEN the database contains 7 actions and admin

		UserEntity admin = this.faker.entity().user();
		admin.setRole(UserRole.ADMIN);
		this.userDao.create(admin);

		int size = 7;

		List<ElementEntity> elements = this.faker.entity().elementList(size);
		List<ActionEntity> actions = this.faker.entity().actionList(elements, size);

		this.elementService.importElements(elements, admin.getUserSmartspace(), admin.getUserEmail());
		this.actionService.importActions(actions, admin.getUserSmartspace(), admin.getUserEmail());

		// WHEN I GET actions of size 10 and page 0
		ActionBoundary[] response = this.restTemplate.getForObject(this.baseUrl + "?size={size}&page={page}",
				ActionBoundary[].class, admin.getUserSmartspace(), admin.getUserEmail(), 10, 0);

		// THEN I receive response has size of 7
		assertThat(response).hasSize(size);
	}

}
