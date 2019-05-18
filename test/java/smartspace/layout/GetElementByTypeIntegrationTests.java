package smartspace.layout;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;

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

import smartspace.dao.EnhancedElementDao;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;
import smartspace.data.UserEntity;
import smartspace.data.UserKey;
import smartspace.data.UserRole;
import smartspace.data.util.Faker;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = { "spring.profiles.active=default,test" })
public class GetElementByTypeIntegrationTests {

	private String baseUrl;
	private int port;
	private RestTemplate restTemplate;
	private EnhancedElementDao<ElementKey> elementDao;
	private EnhancedUserDao<UserKey> userDao;
	private Faker faker;

	

	@Autowired
	public void setElementDao(EnhancedElementDao<ElementKey> elementDao) {
		this.elementDao = elementDao;
	}

	@Autowired
	public void setUsertDao(EnhancedUserDao<UserKey> userDao) {
		this.userDao = userDao;
	}

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
		this.restTemplate = new RestTemplate();
	}

	@PostConstruct
	public void init() {
		this.baseUrl = "http://localhost:" + port
				+ "/smartspace/elements/{userSmartspace}/{userEmail}/?search=type&value={type}&page={page}&size={size}";
		this.faker = new Faker();
	}

	@After
	public void tearDown() {
		this.elementDao.deleteAll();
		this.userDao.deleteAll();
	}

	@Test
	public void testGetElementByTypeWithManagerAndEmptyDB() throws Exception {
		// GIVEN the database contain manager and not contain elements
		UserEntity manager = faker.entity().user();
		manager.setRole(UserRole.MANAGER);
		this.userDao.create(manager);
		// WHEN I GET element by type
		ElementBoundary[] response = this.restTemplate.getForObject(this.baseUrl, ElementBoundary[].class,
				manager.getUserSmartspace(), manager.getUserEmail(), "test", 0, 10);

		// THEN the database not contains elements
		assertThat(response).isEmpty();

	}

	@Test
	public void testGetElementByTypeWithManagerWithPagination() throws Exception {
		// GIVEN the database contain manager and element
		UserEntity manager = faker.entity().user();
		manager.setRole(UserRole.MANAGER);
		this.userDao.create(manager);
		ElementEntity element = faker.entity().element();
		element.setType("test");
		elementDao.create(element);

		// WHEN I GET element by type
		ElementBoundary[] response = this.restTemplate.getForObject(this.baseUrl, ElementBoundary[].class,
				manager.getUserSmartspace(), manager.getUserEmail(), "test", 0, 10);

		// THEN the database contains a single element
		assertThat(response).hasSize(1);
	}

	@Test(expected = Exception.class)
	public void testGetElementByTypeWithWrongManagerWithPagination() throws Exception {
		// GIVEN the database contain manager and element
		UserEntity manager = faker.entity().user();
		manager.setRole(UserRole.MANAGER);
		this.userDao.create(manager);
		ElementEntity element = faker.entity().element();
		element.setType("test");
		elementDao.create(element);

		// WHEN I GET element by type with wrong manager
		this.restTemplate.getForObject(this.baseUrl, ElementBoundary[].class, manager.getUserSmartspace(),
				"wrongemail@gmail.com", "test", 0, 10);

		// THEN throw exception

	}

	@Test
	public void testGetElementByTypeWithPlayerWithPagination() throws Exception {
		// GIVEN the database contain player and element
		UserEntity player = faker.entity().user();
		player.setRole(UserRole.PLAYER);
		this.userDao.create(player);
		ElementEntity element = faker.entity().element();
		element.setType("test");
		elementDao.create(element);

		// WHEN I GET element by type
		ElementBoundary[] response = this.restTemplate.getForObject(this.baseUrl, ElementBoundary[].class,
				player.getUserSmartspace(), player.getUserEmail(), "test", 0, 10);

		// THEN the database contains a single element
		assertThat(response).hasSize(1);
	}

	@Test(expected = Exception.class)
	public void testGetElementByTypeWithWrongPlayerWithPagination() throws Exception {
		// GIVEN the database contain player and element
		UserEntity player = faker.entity().user();
		player.setRole(UserRole.PLAYER);
		this.userDao.create(player);
		ElementEntity element = faker.entity().element();
		element.setType("test");
		elementDao.create(element);

		// WHEN I GET element by type with wrong manager
		this.restTemplate.getForObject(this.baseUrl, ElementBoundary[].class, player.getUserSmartspace(),
				"wrongemail@gmail.com", "test", 0, 10);

		// THEN throw exception

	}

	@Test
	public void testGetElementNoExpiredByTypeWithPlayerAndPagination() throws Exception {
		// GIVEN the database contain player and 4 elements 
		UserEntity player = faker.entity().user();
		player.setRole(UserRole.PLAYER);
		this.userDao.create(player);

		int size = 2;
		List<ElementEntity> elements = faker.entity().elementList(size);
		elements=elements.stream().peek(element->element.setExpired(false)).peek(element->element.setType("test")).map(elementDao::create).collect(Collectors.toList());
		
		List<ElementEntity> elementsExpired = faker.entity().elementList(size);
		elementsExpired=elementsExpired.stream().peek(element->element.setExpired(true)).peek(element->element.setType("test")).map(elementDao::create).collect(Collectors.toList());
		
		List<ElementBoundary> elementsBoundary = elements.stream().map(ElementBoundary::new)
				.collect(Collectors.toList());
		// WHEN I GET element by type with wrong manager
		ElementBoundary[] response=this.restTemplate.getForObject(this.baseUrl, ElementBoundary[].class, player.getUserSmartspace(), player.getUserEmail(), "test", 0, 10);

		// THEN the database contains 2 elements not expired
		assertThat(response).hasSize(2).usingElementComparatorOnFields("key","elementType","expired").containsExactlyElementsOf(elementsBoundary);

	
	}
	
	@Test
	public void testGetElementExpiredByTypeWithManagerAndPagination() throws Exception {
		// GIVEN the database contain manager and 4 elements 
		UserEntity manager = faker.entity().user();
		manager.setRole(UserRole.MANAGER);
		this.userDao.create(manager);

		int size = 2;
		List<ElementEntity> elements = faker.entity().elementList(size);
		elements=elements.stream().peek(element->element.setExpired(false)).peek(element->element.setType("test")).map(elementDao::create).collect(Collectors.toList());
		
		List<ElementEntity> elementsExpired = faker.entity().elementList(size);
		elementsExpired=elementsExpired.stream().peek(element->element.setExpired(true)).peek(element->element.setType("test")).map(elementDao::create).collect(Collectors.toList());
		
		// WHEN I GET element by type 
		ElementBoundary[] response=this.restTemplate.getForObject(this.baseUrl, ElementBoundary[].class, manager.getUserSmartspace(), manager.getUserEmail(), "test", 0, 10);

		// THEN the database contains 4 elements 
		assertThat(response).hasSize(4);

	}
	
	public void testGetElementExpiredByTypeWithManager() throws Exception {
		// GIVEN the database contain manager and 4 elements 
		UserEntity manager = faker.entity().user();
		manager.setRole(UserRole.MANAGER);
		this.userDao.create(manager);
		
		int size = 2;
		List<ElementEntity> elements = faker.entity().elementList(size);
		elements=elements.stream().peek(element->element.setExpired(false)).peek(element->element.setType("test")).map(elementDao::create).collect(Collectors.toList());
		
		List<ElementEntity> elementsExpired = faker.entity().elementList(size);
		elementsExpired=elementsExpired.stream().peek(element->element.setExpired(false)).peek(element->element.setType("test1")).map(elementDao::create).collect(Collectors.toList());
		
		// WHEN I GET element by type 
		ElementBoundary[] response=this.restTemplate.getForObject(this.baseUrl, ElementBoundary[].class, manager.getUserSmartspace(), manager.getUserEmail(), "test", 0, 10);

		// THEN the database contains 2 elements 
		assertThat(response).hasSize(2);

	}
	
	
	@Test(expected = Exception.class)
	public void testGetElementByTypeWithAdmin() throws Exception {
		// GIVEN the database contain admin and element
		UserEntity admin = faker.entity().user();
		admin.setRole(UserRole.ADMIN);
		this.userDao.create(admin);
		ElementEntity element = faker.entity().element();
		element.setType("test");
		elementDao.create(element);

		// WHEN I GET element by type with admin
		this.restTemplate.getForObject(this.baseUrl, ElementBoundary[].class, admin.getUserSmartspace(),
				admin.getUserEmail(), "test", 0, 10);

		// THEN throw exception

	}


}
