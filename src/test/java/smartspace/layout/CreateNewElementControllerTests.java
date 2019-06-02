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
import smartspace.infra.ElementServiceForManagerOnly;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = { "spring.profiles.active=default,test" })
public class CreateNewElementControllerTests {

	private String baseUrl;
	private int port;
	private RestTemplate restTemplate;
	private EnhancedElementDao<ElementKey> elementDao;
	private EnhancedUserDao<UserKey> userDao;
	private ElementServiceForManagerOnly elementService;
	private Faker faker;

	@Autowired
	public void setElementService(ElementServiceForManagerOnly elementService) {
		this.elementService = elementService;
	}

	@Autowired
	public void setElementDao(EnhancedElementDao<ElementKey> elementDao) {
		this.elementDao = elementDao;
	}

	@Autowired
	public void setUserDao(EnhancedUserDao<UserKey> userDao) {
		this.userDao = userDao;
	}

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
		this.restTemplate = new RestTemplate();
	}

	@PostConstruct
	public void init() {
		this.baseUrl = "http://localhost:" + port + "/smartspace/elements/{managerSmartspace}/{managerEmail}";
		this.faker = new Faker();
	}

	@After
	public void tearDown() {
		this.elementDao.deleteAll();
		this.userDao.deleteAll();
	}

	@Test
	public void testPostNewElement() throws Exception {
		// GIVEN the database contain one MANAGER
		UserEntity manager = faker.entity().user();
		manager.setRole(UserRole.MANAGER);
		this.userDao.create(manager);

		// WHEN I POST new element
		ElementBoundary newElement = faker.boundary().element();
		this.restTemplate.postForObject(this.baseUrl, newElement, ElementBoundary.class, manager.getUserSmartspace(),
				manager.getUserEmail());

		// THEN the database contains a single element
		assertThat(this.elementDao.readAll()).hasSize(1);
	}

	@Test
	public void testPostManyNewElements() throws Exception {
		// GIVEN the database contain one MANAGER
		UserEntity manager = faker.entity().user();
		manager.setRole(UserRole.MANAGER);
		this.userDao.create(manager);

		// WHEN I POST new elements
		int size = faker.generateNumber(1, 100);
		List<ElementEntity> elemntsEntity = faker.entity().elementList(size);
		List<ElementEntity> elements = elemntsEntity.stream().map(element -> this.elementService
				.createNewElement(manager.getUserSmartspace(), manager.getUserEmail(), element))
				.collect(Collectors.toList());

		elemntsEntity.stream().map(ElementBoundary::new).map(element -> this.restTemplate.postForObject(this.baseUrl,
				element, ElementBoundary.class, manager.getUserSmartspace(), manager.getUserEmail()));

		// THEN the database contains size elements
		assertThat(elements).hasSize(size);
		assertThat(this.elementDao.readAll()).hasSize(size);
	}

	@Test(expected = Exception.class)
	public void testPostNewElementWithWrongManagerEmail() throws Exception {
		// GIVEN the database contain MANAGER
		UserEntity manager = faker.entity().user();
		manager.setRole(UserRole.MANAGER);
		manager.setUserEmail("test@gmail.com");
		this.userDao.create(manager);

		// WHEN I POST new element with wrong manager email
		ElementBoundary[] newElement = faker.boundary().elementArray(1);

		this.restTemplate.postForObject(this.baseUrl, newElement, ElementBoundary[].class, manager.getUserSmartspace(),
				"err@gmail.com");

		// THEN the test end with exception
	}
	@Test(expected = Exception.class)
	public void testPostNewElementWithWrongManagerSmartspace() throws Exception {
		// GIVEN the database contain MANAGER
		UserEntity manager = faker.entity().user();
		manager.setRole(UserRole.MANAGER);
		manager.setUserSmartspace("testSmartspace");;
		this.userDao.create(manager);

		// WHEN I POST new element with wrong manager smartspace
		ElementBoundary[] newElement = faker.boundary().elementArray(1);

		this.restTemplate.postForObject(this.baseUrl, newElement, ElementBoundary[].class,"wrongSmartspace",
				manager.getUserEmail());

		// THEN the test end with exception
	}

}
