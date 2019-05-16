package smartspace;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.junit.After;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import smartspace.dao.EnhancedActionDao;
import smartspace.dao.EnhancedElementDao;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;
import smartspace.data.Location;
import smartspace.data.UserEntity;
import smartspace.data.UserKey;
import smartspace.data.UserRole;
import smartspace.data.util.Faker;
import smartspace.layout.ActionBoundary;
import smartspace.layout.ElementBoundary;
import smartspace.layout.NewUserForm;
import smartspace.layout.UserBoundary;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = { "spring.profiles.active=default, test" })
public class RestLogicSaintyTests {
	
	private String appSmartspace;
	
	private String baseUrl;
	private int port;
	private RestTemplate restTemplate;
	private Faker faker;
	
	private EnhancedUserDao<UserKey> userDao;
	private EnhancedElementDao<ElementKey> elementDao;
	private EnhancedActionDao actionDao;

	@Autowired
	public void setUserDao(EnhancedUserDao<UserKey> userDao, EnhancedElementDao<ElementKey> elementDao, EnhancedActionDao actionDao) {
		this.userDao = userDao;
		this.elementDao = elementDao;
		this.actionDao = actionDao;
	}

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
		this.restTemplate = new RestTemplate();
	}
	
	@Value("${smartspace.name}") 
	public void setAppSmartspace(String appSmartspace) {
		this.appSmartspace = appSmartspace;
	}

	@PostConstruct
	public void init() {
		this.baseUrl = "http://localhost:" + port + "/smartspace/";
		this.faker = new Faker();
		
	}

	@After
	public void tearDown() {
		this.userDao.deleteAll();
		this.elementDao.deleteAll();
		this.actionDao.deleteAll();
	}
	
	// # ---------------- GET: /smartspace/elements/{userSmartspace}/{userEmail}/{elementSmartspace}/{elementId} ---------------- # 
	
	@Test
	public void testGetSpecificElementWorksPlayer() throws Exception {
		// GIVEN that there is an element and a user in the database
		UserEntity manager = this.userDao.create(faker.entity().user(UserRole.MANAGER));
		ElementEntity element = this.elementDao.create(faker.entity().element());
		
		// WHEN I search for the specific element
		ElementBoundary response = this.restTemplate.getForObject(this.baseUrl + "elements/{userSmartspace}/{userEmail}/{elementSmartspace}/{elementId}",
				ElementBoundary.class, manager.getUserSmartspace(), manager.getUserEmail(), element.getElementSmartspace(), element.getElementId());
		
		// THEN I recived the same element
		assertThat(response.convertToEntity().getKey()).isEqualTo(element.getKey());
	}
	
	@Test
	public void testGetSpecificElementWorksManager() throws Exception {
		// GIVEN that there is an element and a user in the database
		UserEntity manager = this.userDao.create(faker.entity().user(UserRole.PLAYER));
		ElementEntity element = this.elementDao.create(faker.entity().element());
		
		// WHEN I search for the specific element
		ElementBoundary response = this.restTemplate.getForObject(this.baseUrl + "elements/{userSmartspace}/{userEmail}/{elementSmartspace}/{elementId}",
				ElementBoundary.class, manager.getUserSmartspace(), manager.getUserEmail(), element.getElementSmartspace(), element.getElementId());
		
		// THEN I recived the same element
		assertThat(response.convertToEntity().getKey()).isEqualTo(element.getKey());
	}
	
	@Test(expected=Exception.class)
	public void testGetSpecificElementExpiredAsPlayer() throws Exception {
		// GIVEN that there is an element and a user in the database
		UserEntity manager = this.userDao.create(faker.entity().user(UserRole.PLAYER));
		ElementEntity element = faker.entity().element();
		
		// expired = true --> only managers can see the element
		element.setExpired(true);
		element = this.elementDao.create(element);
		
		// WHEN I search for the specific element
		ElementBoundary response = this.restTemplate.getForObject(this.baseUrl + "elements/{userSmartspace}/{userEmail}/{elementSmartspace}/{elementId}",
				ElementBoundary.class, manager.getUserSmartspace(), manager.getUserEmail(), element.getElementSmartspace(), element.getElementId());
		
		// THEN there is an Exception
	}
	
	@Test
	public void testGetSpecificElementExpiredAsManager() throws Exception {
		// GIVEN that there is an element and a user in the database
		UserEntity manager = this.userDao.create(faker.entity().user(UserRole.MANAGER));
		ElementEntity element = faker.entity().element();
		
		// expired = true --> only managers can see the element
		element.setExpired(true);
		element = this.elementDao.create(element);
		
		// WHEN I search for the specific element
		ElementBoundary response = this.restTemplate.getForObject(this.baseUrl + "elements/{userSmartspace}/{userEmail}/{elementSmartspace}/{elementId}",
				ElementBoundary.class, manager.getUserSmartspace(), manager.getUserEmail(), element.getElementSmartspace(), element.getElementId());
		
		// THEN I recived the same element
		assertThat(response.convertToEntity().getKey()).isEqualTo(element.getKey());
	}
	
	@Test(expected=Exception.class)
	public void testGetSpecificElementNotAdmin() throws Exception {
		// GIVEN that there is an element and a user in the database
		UserEntity manager = this.userDao.create(faker.entity().user(UserRole.ADMIN));
		ElementEntity element = this.elementDao.create(faker.entity().element());
		
		// WHEN I search for the specific element
		ElementBoundary response = this.restTemplate.getForObject(this.baseUrl + "elements/{userSmartspace}/{userEmail}/{elementSmartspace}/{elementId}",
				ElementBoundary.class, manager.getUserSmartspace(), manager.getUserEmail(), element.getElementSmartspace(), element.getElementId());
		
		// THEN there is an Exception
	}
	
	
	// # ---------------- GET: /smartspace/elements/{userSmartspace}/{userEmail}/?page={page}&size={size} ---------------- # 
	
	@Test
	public void testGetExpiredElementsAsPlayer() throws Exception {
		// GIVEN that there are size=10 expired elements and size=10 non expired elements + a player in the database
		UserEntity manager = this.userDao.create(faker.entity().user(UserRole.PLAYER));
		
		int size = faker.generateNumber(10, 50);
		List<ElementEntity> elements = faker.entity().elementList(size).stream().map(this.elementDao::create).collect(Collectors.toList());
		List<ElementEntity> elements2 = faker.entity().elementList(size).stream().peek(en->en.setExpired(true)).map(this.elementDao::create).collect(Collectors.toList());
		
		// WHEN I search for size*2 elements as player
		ElementBoundary[] response = this.restTemplate.getForObject(this.baseUrl + "elements/{userSmartspace}/{userEmail}/?page=0&size={size}", ElementBoundary[].class, manager.getUserSmartspace(), manager.getUserEmail(), size*5);
		
		// THEN I recive only the size non expired elements
		assertThat(response).hasSize(size);
	}
	
	@Test
	public void testGetExpiredElementsAsManager() throws Exception {
		// GIVEN that there are size=10 expired elements and size=10 non expired elements + a manager in the database
		UserEntity manager = this.userDao.create(faker.entity().user(UserRole.MANAGER));
		
		int size = faker.generateNumber(10, 50);
		List<ElementEntity> elements = faker.entity().elementList(size).stream().map(this.elementDao::create).collect(Collectors.toList());
		List<ElementEntity> elements2 = faker.entity().elementList(size).stream().peek(en->en.setExpired(true)).map(this.elementDao::create).collect(Collectors.toList());
		
		// WHEN I search for size*2 elements as manager
		ElementBoundary[] response = this.restTemplate.getForObject(this.baseUrl + "elements/{userSmartspace}/{userEmail}/?page=0&size={size}", ElementBoundary[].class, manager.getUserSmartspace(), manager.getUserEmail(), size*5);
		
		// THEN I recive all size*2 elements
		assertThat(response).hasSize(size*2);
	}
	
	// # ---------------- GET: /smartspace/elements/{userSmartspace}/{userEmail}/?search=location ---------------- # 
	
		// disabled for now TODO: enable after radius check
		public void testSearchByLocationAsRadiusCheck() throws Exception {
			// GIVEN that there are some elements and a player user in the database
			UserEntity user = this.userDao.create(faker.entity().user(UserRole.PLAYER));
			
			// create size elements
			int size = faker.generateNumber(10, 11);
			double xStart = faker.generateDouble(0, 100);
			double yStart = faker.generateDouble(0, 100);
			double maxDistance = faker.generateDouble(0, 100);
			
			// create some elements
			List<ElementEntity> elements = faker.entity().elementList(size).stream().map(this.elementDao::create).collect(Collectors.toList());
			
			// filter elements with distance <= maxDistance
			List<ElementEntity> mustInclude = elements.stream().filter(e -> (e.getLocation().distance(xStart, yStart) <= maxDistance)).collect(Collectors.toList());
			List<ElementEntity> mustNotInclude = elements.stream().filter(e -> (e.getLocation().distance(xStart, yStart) > maxDistance)).collect(Collectors.toList());

			// WHEN I search for elements with the same name
			ElementBoundary[] response = this.restTemplate.getForObject(this.baseUrl + "elements/{userSmartspace}/{userEmail}/?search=location&x={x}&y={y}&distance={distance}&page=0&size={size}", ElementBoundary[].class, user.getUserSmartspace(), user.getUserEmail(), xStart, yStart, maxDistance, size);
			
			// THEN all the distances are less than maxDistance
			List<ElementEntity> responseList = Arrays.stream(response).map(ElementBoundary::convertToEntity).collect(Collectors.toList());
			
			assertThat(responseList).hasSize(mustInclude.size());
			
			if (mustInclude.size() > 0) {
				assertThat(responseList).usingElementComparatorOnFields("key").containsExactlyInAnyOrderElementsOf(mustInclude);
			}
			if (mustNotInclude.size() > 0) {
				assertThat(responseList).usingElementComparatorOnFields("key").doesNotContainAnyElementsOf(mustNotInclude); 
			}
		}
		
		@Test
		public void testSearchByLocationLinear() throws Exception {
			// GIVEN that there are some elements and a player user in the database
			UserEntity user = this.userDao.create(faker.entity().user(UserRole.PLAYER));
			
			// create size elements
			int size = faker.generateNumber(10, 11);
			double xStart = faker.generateDouble(0, 100);
			double yStart = faker.generateDouble(0, 100);
			double maxDistance = faker.generateDouble(0, 100);
			
			// create some elements
			List<ElementEntity> elements = faker.entity().elementList(size).stream().map(this.elementDao::create).collect(Collectors.toList());
			
			// filter elements with distance <= maxDistance
			List<ElementEntity> mustInclude = elements.stream().filter(e -> (isInSqaure(e.getLocation(), xStart, yStart, maxDistance))).collect(Collectors.toList());
			List<ElementEntity> mustNotInclude = elements.stream().filter(e -> (!isInSqaure(e.getLocation(), xStart, yStart, maxDistance))).collect(Collectors.toList());

			// WHEN I search for elements with the same name
			ElementBoundary[] response = this.restTemplate.getForObject(this.baseUrl + "elements/{userSmartspace}/{userEmail}/?search=location&x={x}&y={y}&distance={distance}&page=0&size={size}", ElementBoundary[].class, user.getUserSmartspace(), user.getUserEmail(), xStart, yStart, maxDistance, size);
			
			// THEN all the distances are less than maxDistance
			List<ElementEntity> responseList = Arrays.stream(response).map(ElementBoundary::convertToEntity).collect(Collectors.toList());
			
			assertThat(responseList).hasSize(mustInclude.size());
			
			if (mustInclude.size() > 0) {
				assertThat(responseList).usingElementComparatorOnFields("key").containsExactlyInAnyOrderElementsOf(mustInclude);
			}
			if (mustNotInclude.size() > 0) {
				assertThat(responseList).usingElementComparatorOnFields("key").doesNotContainAnyElementsOf(mustNotInclude); 
			}
		}
		
		private boolean isInSqaure(Location point, double xStart, double yStart, double distance) {
			return (point.getX() >= xStart - distance && point.getX() <= xStart + distance && point.getY() >= yStart - distance && point.getY() <= yStart + distance);
		}
		
	// # ---------------- GET: /smartspace/elements/{userSmartspace}/{userEmail}/?search=name ---------------- # 
	
	@Test
	public void testSearchByName() throws Exception {
		// GIVEN that there are some elements and a player user in the database
		UserEntity manager = this.userDao.create(faker.entity().user(UserRole.PLAYER));
		String name = faker.generateName(6);
		
		ElementEntity element = faker.entity().element();
		element.setName(name);
		element = this.elementDao.create(element);
	
		// WHEN I search for elements with the same name
		ElementBoundary[] response = this.restTemplate.getForObject(this.baseUrl + "elements/{userSmartspace}/{userEmail}/?search=name&value={name}", ElementBoundary[].class, manager.getUserSmartspace(), manager.getUserEmail(), name);
		
		// THEN I recive the same element
		assertThat(response[0].convertToEntity().getKey()).isEqualTo(element.getKey());
		assertThat(response[0].convertToEntity().getName()).isEqualTo(name);
	}
	
	@Test
	public void testSearchByNameAsPlayer() throws Exception {
		// GIVEN that there are some elements and a player user in the database
		UserEntity manager = this.userDao.create(faker.entity().user(UserRole.PLAYER));
		
		int size = faker.generateNumber(10, 50);
		String name = faker.generateName(12);
		
		List<ElementEntity> elements = faker.entity().elementList(size).stream().peek(en->en.setName(name)).map(this.elementDao::create).collect(Collectors.toList());
		List<ElementEntity> elements2 = faker.entity().elementList(size).stream().peek(en->en.setName(name)).peek(en->en.setExpired(true)).map(this.elementDao::create).collect(Collectors.toList());
		
		// WHEN I search for all elements with same name as player
		ElementBoundary[] response = this.restTemplate.getForObject(this.baseUrl + "elements/{userSmartspace}/{userEmail}/?search=name&value={name}&page=0&size={size}", ElementBoundary[].class, manager.getUserSmartspace(), manager.getUserEmail(), name, size*5);
		
		// THEN I only recive the non expired elements
		assertThat(response).hasSize(size);
	}
	
	@Test
	public void testSearchByNameAsManager() throws Exception {
		// GIVEN that there are some elements and a manager user in the database
		UserEntity manager = this.userDao.create(faker.entity().user(UserRole.MANAGER));
		
		int size = faker.generateNumber(10, 50);
		String name = faker.generateName(12);
		
		List<ElementEntity> elements = faker.entity().elementList(size).stream().peek(en->en.setName(name)).map(this.elementDao::create).collect(Collectors.toList());
		List<ElementEntity> elements2 = faker.entity().elementList(size).stream().peek(en->en.setName(name)).peek(en->en.setExpired(true)).map(this.elementDao::create).collect(Collectors.toList());
		
		// WHEN I search for all elements with same name as manager
		ElementBoundary[] response = this.restTemplate.getForObject(this.baseUrl + "elements/{userSmartspace}/{userEmail}/?search=name&value={name}&page=0&size={size}", ElementBoundary[].class, manager.getUserSmartspace(), manager.getUserEmail(), name, size*5);
		
		// THEN I recive all elements
		assertThat(response).hasSize(size*2);
	}
	
	// # ---------------- GET: /smartspace/elements/{userSmartspace}/{userEmail}/?search=type ---------------- # 
	
	@Test
	public void testSearchByType() throws Exception {
		// GIVEN that there are some elements and a player user in the database
		UserEntity manager = this.userDao.create(faker.entity().user(UserRole.PLAYER));
		String typeStr = faker.generateName(6);
		
		ElementEntity element = faker.entity().element();
		element.setType(typeStr);
		element = this.elementDao.create(element);
	
		// WHEN I search for elements with the same name
		ElementBoundary[] response = this.restTemplate.getForObject(this.baseUrl + "elements/{userSmartspace}/{userEmail}/?search=type&value={name}", ElementBoundary[].class, manager.getUserSmartspace(), manager.getUserEmail(), typeStr);
		
		// THEN I recive the same element
		assertThat(response[0].convertToEntity().getKey()).isEqualTo(element.getKey());
		assertThat(response[0].convertToEntity().getType()).isEqualTo(typeStr);
	}
	
	@Test
	public void testSearchByTypeAsPlayer() throws Exception {
		// GIVEN that there are some elements and a player user in the database
		UserEntity manager = this.userDao.create(faker.entity().user(UserRole.PLAYER));
		
		int size = faker.generateNumber(10, 50);
		String typeStr = faker.generateName(12);
		
		List<ElementEntity> elements = faker.entity().elementList(size).stream().peek(en->en.setType(typeStr)).map(this.elementDao::create).collect(Collectors.toList());
		List<ElementEntity> elements2 = faker.entity().elementList(size).stream().peek(en->en.setType(typeStr)).peek(en->en.setExpired(true)).map(this.elementDao::create).collect(Collectors.toList());
		
		// WHEN I search for all elements with same type as a player
		ElementBoundary[] response = this.restTemplate.getForObject(this.baseUrl + "elements/{userSmartspace}/{userEmail}/?search=type&value={name}&page=0&size={size}", ElementBoundary[].class, manager.getUserSmartspace(), manager.getUserEmail(), typeStr, size*5);
		
		// THEN I only recive the non expired elements
		assertThat(response).hasSize(size);
	}
	
	@Test
	public void testSearchByTypeAsManager() throws Exception {
		// GIVEN that there are some elements and a manager user in the database
		UserEntity manager = this.userDao.create(faker.entity().user(UserRole.MANAGER));
		
		int size = faker.generateNumber(10, 50);
		String typeStr = faker.generateName(12);
		
		List<ElementEntity> elements = faker.entity().elementList(size).stream().peek(en->en.setType(typeStr)).map(this.elementDao::create).collect(Collectors.toList());
		List<ElementEntity> elements2 = faker.entity().elementList(size).stream().peek(en->en.setType(typeStr)).peek(en->en.setExpired(true)).map(this.elementDao::create).collect(Collectors.toList());
		
		// WHEN I search for all elements with same type as manager
		ElementBoundary[] response = this.restTemplate.getForObject(this.baseUrl + "elements/{userSmartspace}/{userEmail}/?search=type&value={name}&page=0&size={size}", ElementBoundary[].class, manager.getUserSmartspace(), manager.getUserEmail(), typeStr, size*5);
		
		// THEN I recive all elements
		assertThat(response).hasSize(size*2);
	}
	
	// # ---------------- GET: /smartspace/users/login/{userSmartspace}/{userEmail} ---------------- # 
	
	@Test
	public void testGetSpecificUser() throws Exception {
		// GIVEN that there is a user in the database
		UserEntity user = this.userDao.create(faker.entity().user());
		
		// WHEN I search for the specific user
		UserBoundary response = this.restTemplate.getForObject(this.baseUrl + "users/login/{userSmartspace}/{userEmail}", UserBoundary.class, user.getUserSmartspace(), user.getUserEmail());
		
		// THEN I recived the same element
		assertThat(response.convertToEntity().getKey()).isEqualTo(user.getKey());
	}
	
	@Test(expected=Exception.class)
	public void testGetSpecificUserNotFound() throws Exception {
		// WHEN I search for a non existing user
		UserEntity user = faker.entity().user();
		UserBoundary response = this.restTemplate.getForObject(this.baseUrl + "users/login/{userSmartspace}/{userEmail}", UserBoundary.class, user.getUserSmartspace(), user.getUserEmail());
		
		// THEN I recive a NOT_FOUND Exception
	}
	
	// # ---------------- POST: /smartspace/elements/{managerSmartspace}/{managerEmail} ---------------- # 

	@Test
	public void testPostingNewElementWorks() throws Exception {
		// GIVEN that the database contains a manager user
		UserEntity manager = this.userDao.create(faker.entity().user(UserRole.MANAGER));
		
		// WHEN the manager posts a new element
		ElementBoundary element = faker.boundary().element();
		element.setKey(null); 
		
		ElementBoundary rv = this.restTemplate.postForObject(baseUrl + "elements/{managerSmartspace}/{managerEmail}", element, ElementBoundary.class, manager.getUserSmartspace(), manager.getUserEmail());
		
		// THEN the database should contain an element with a new key
		assertThat(this.userDao.readAll()).hasSize(1);
		assertThat(this.elementDao.readAll()).hasSize(1);
		assertThat(rv.convertToEntity().getKey()).isNotNull();
	}
		
	@Test(expected=Exception.class)
	public void testPostingNewElementManagerOnlyPlayer() throws Exception {
		// GIVEN that the database contains a manager user
		UserEntity manager = this.userDao.create(faker.entity().user(UserRole.PLAYER));
		
		// WHEN the manager posts a new element
		ElementBoundary element = faker.boundary().element();
		element.setKey(null); 
		
		ElementBoundary rv = this.restTemplate.postForObject(baseUrl + "elements/{managerSmartspace}/{managerEmail}", element, ElementBoundary.class, manager.getUserSmartspace(), manager.getUserEmail());
		
		// THEN there is an exception
	}
	
	@Test(expected=Exception.class)
	public void testPostingNewElementManagerOnlyAdmin() throws Exception {
		// GIVEN that the database contains a manager user
		UserEntity admin = this.userDao.create(faker.entity().user(UserRole.ADMIN));
		
		// WHEN the manager posts a new element
		ElementBoundary element = faker.boundary().element();
		element.setKey(null); 
		
		ElementBoundary rv = this.restTemplate.postForObject(baseUrl + "elements/{managerSmartspace}/{managerEmail}", element, ElementBoundary.class, admin.getUserSmartspace(), admin.getUserEmail());
		
		// THEN there is an exception
	}
	
		
	// # ---------------- POST: /smartspace/users ---------------- # 
	
	@Test
	public void testRegisterNewUser() throws Exception {
		// GIVEN that the database is empty
		
		// WHEN a new user registers
		NewUserForm newUser = new NewUserForm(faker.entity().user()); // fake valid details
		this.restTemplate.postForObject(baseUrl + "users", newUser, NewUserForm.class);
		
		// THEN the database contains the user
		assertThat(this.userDao.readById(new UserKey(this.appSmartspace, newUser.getEmail()))).isNotEmpty();
	}
	
	// # ---------------- POST: /smartspace/actions ---------------- # 
	
	@Test
	public void testPostNewAction() throws Exception {
		// GIVEN that the database contains a user and an element
		UserEntity player = this.userDao.create(faker.entity().user(UserRole.PLAYER));
		ElementEntity element = this.elementDao.create(faker.entity().element());
		
		// WHEN a new action is posted
		ActionBoundary action = faker.boundary().action(element, player);
		this.restTemplate.postForObject(baseUrl + "actions", action, ActionBoundary.class);
		
		// THEN the database contains 1 action
		assertThat(this.actionDao.readAll()).hasSize(1);
	}
	
	@Test(expected=Exception.class)
	public void testPostNewActionNoUser() throws Exception {
		// GIVEN that the database contains an element
		ElementEntity element = this.elementDao.create(faker.entity().element());
		
		// WHEN a new action is posted with bad user
		ActionBoundary action = faker.boundary().action(element);
		this.restTemplate.postForObject(baseUrl + "actions", action, ActionBoundary.class);
		
		// THEN there is an excpetion
	}
	
	@Test(expected=Exception.class)
	public void testPostNewActionNoElement() throws Exception {
		// GIVEN that the database contains a user and NO element
		UserEntity player = this.userDao.create(faker.entity().user(UserRole.PLAYER));
		ElementEntity element = faker.entity().element(); // no create()

		// WHEN a new action is posted
		ActionBoundary action = faker.boundary().action(element, player);
		this.restTemplate.postForObject(baseUrl + "actions", action, ActionBoundary.class);

		// THEN there is an excpetion
	}
	
	@Test(expected=Exception.class)
	public void testPostNewActionAsManager() throws Exception {
		// GIVEN that the database contains a user and an element
		UserEntity player = this.userDao.create(faker.entity().user(UserRole.MANAGER));
		ElementEntity element = this.elementDao.create(faker.entity().element());

		// WHEN a new action is posted
		ActionBoundary action = faker.boundary().action(element, player);
		this.restTemplate.postForObject(baseUrl + "actions", action, ActionBoundary.class);

		// THEN there is an excpetion
	}
	
	@Test(expected=Exception.class)
	public void testPostNewActionBadActionType() throws Exception {
		// GIVEN that the database contains a user and an element
		UserEntity player = this.userDao.create(faker.entity().user(UserRole.MANAGER));
		ElementEntity element = this.elementDao.create(faker.entity().element());

		// WHEN a new action is posted
		ActionBoundary action = faker.boundary().action(element, player);
		action.setType("BadType");
		this.restTemplate.postForObject(baseUrl + "actions", action, ActionBoundary.class);

		// THEN there is an excpetion
	}
	
	@Test(expected=Exception.class)
	public void testPostNewActionOnExpiredElement() throws Exception {
		// GIVEN that the database contains a user and an element
		UserEntity player = this.userDao.create(faker.entity().user(UserRole.PLAYER));
		ElementEntity element = this.elementDao.create(faker.entity().element(true));

		// WHEN a new action is posted
		ActionBoundary action = faker.boundary().action(element, player);
		this.restTemplate.postForObject(baseUrl + "actions", action, ActionBoundary.class);

		// THEN there is an excpetion
	}
	
	// # ---------------- PUT: /smartspace/elements/{managerSmartspace}/{managerEmail}/{elementSmartspace}/{elementId} ---------------- # 
	
	@Test
	public void testUpdateElement() throws Exception {
		// GIVEN that the database contains a manager and an element
		UserEntity manager = this.userDao.create(faker.entity().user(UserRole.MANAGER));
		ElementEntity element = this.elementDao.create(faker.entity().element());
		
		// WHEN the manager tries to update the element
		ElementEntity updatedBoundary = faker.entity().element();
		
		this.restTemplate.put(baseUrl + "elements/{managerSmartspace}/{managerEmail}/{elementSmartspace}/{elementId}", new ElementBoundary(updatedBoundary), manager.getUserSmartspace(), manager.getUserEmail(), element.getElementSmartspace(), element.getElementId());
		
		// THEN the element updates in the database
		assertThat(this.elementDao.readById(element.getKey()).get()).extracting("location", "name", "type", "expired").containsExactly(updatedBoundary.getLocation(), updatedBoundary.getName(), updatedBoundary.getType(), updatedBoundary.getExpired());
	}
	
	// # ---------------- PUT: /smartspace/users/login/{userSmartspace}/{userEmail} ---------------- #
	
	@Test
	public void testUpdateUser() throws Exception {
		// GIVEN that the database contains a user
		UserEntity user = this.userDao.create(faker.entity().user());
		
		// WHEN I send updated details with PUT request
		UserEntity updatedUser = faker.entity().user();
		this.restTemplate.put(baseUrl + "users/login/{userSmartspace}/{userEmail}", new UserBoundary(updatedUser), user.getUserSmartspace(), user.getUserEmail());
		
		// THEN the element updates in the database
		assertThat(this.userDao.readById(user.getKey()).get()).extracting("avatar", "username", "role").containsExactly(updatedUser.getAvatar(), updatedUser.getUsername(), updatedUser.getRole());
	}
	
	

}
