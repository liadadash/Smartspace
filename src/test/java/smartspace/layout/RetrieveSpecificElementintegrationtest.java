package smartspace.layout;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.HashMap;
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
import smartspace.data.Location;
import smartspace.data.UserEntity;
import smartspace.data.UserKey;
import smartspace.data.UserRole;
import smartspace.infra.GetSpecificElementService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.profiles.active=default,test"})
public class RetrieveSpecificElementintegrationtest {

	private String baseUrl;
	private int port;
	private RestTemplate restTemplate;
	private EnhancedElementDao<ElementKey> elementDao;
	private EnhancedUserDao<UserKey> userDao;
	private GetSpecificElementService elementService;
	private String smartspace;

	@Autowired
	public void setElementService(GetSpecificElementService elementService) {
		this.elementService = elementService;
	}
	@Autowired
	public void setElementDao(EnhancedElementDao<ElementKey> elementDao , EnhancedUserDao<UserKey> userDao) {
		this.elementDao = elementDao;
		this.userDao = userDao;
	}
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
		this.restTemplate = new RestTemplate();
	}
	@PostConstruct
	public void init() {
		this.baseUrl = "http://localhost:"+port+"/smartspace/elements/{userSmartspace}/{userEmail}/{elementSmartspace}/{elementId}";
		this.smartspace = "2019B.nadav.peleg";
	}
	@After
	public void tearDown() {
		this.elementDao.deleteAll();
		this.userDao.deleteAll();
	}

	@Test
	public void getSpecificElementThatExistInDBAsManager() throws Exception{
		//GIVEN the database contains one element and manger
		ElementEntity element = this.elementDao.create(
				new ElementEntity(
					"Test",
					"test",
					new Location(),
					new Date(),
					"test@test.com",
					this.smartspace,
					true,
					new HashMap<String, Object>()));
		
		UserEntity manager = this.userDao.create(new UserEntity("mangaer@test.com",
				this.smartspace,
				"manager", 
				null,
				UserRole.MANAGER,
				0));
		//WHEN manager want to get the specific element
		
		ElementBoundary rv = this.restTemplate.getForObject
		(baseUrl, ElementBoundary.class,
				manager.getUserSmartspace(),
				manager.getUserEmail(),
				element.getElementSmartspace(),
				element.getKey().getId());
		
		//THEN method return the element
		assertThat(this.elementDao.readById(element.getKey())
				.orElseThrow( ()-> new RuntimeException("not in db"))
				.toString().equals(rv.convertToEntity().toString())
				);
	}
	@Test
	public void getSpecificElementThatExistWithExpierdFalseInDBAsManager() throws Exception{
		//GIVEN the database contains one element with expierd false and manger
		ElementEntity element = this.elementDao.create(
				new ElementEntity(
					"Test",
					"test",
					new Location(),
					new Date(),
					"test@test.com",
					this.smartspace,
					false,
					new HashMap<String, Object>()));
		
		UserEntity manager = this.userDao.create(new UserEntity("mangaer@test.com",
				this.smartspace,
				"manager", 
				null,
				UserRole.MANAGER,
				0));
		//WHEN manager want to get the specific element and keep it on dao
		
		ElementBoundary rv = this.restTemplate.getForObject
		(baseUrl, ElementBoundary.class,
				manager.getUserSmartspace(),
				manager.getUserEmail(),
				element.getElementSmartspace(),
				element.getKey().getId());
		
		//THEN method return the element
		assertThat(this.elementDao.readById(element.getKey())
				.orElseThrow( ()-> new RuntimeException("not in db"))
				.toString().equals(rv.convertToEntity().toString())
				);
		
		assertThat(this.elementDao.readAll()).hasSize(1);
	}
	
	@Test
	public void getSpecificElementThatExistWithExpierdFlaseInDBAsPlayer() throws Exception{
		//GIVEN the database contains one element and manger
		ElementEntity element = this.elementDao.create(
				new ElementEntity(
					"Test",
					"test",
					new Location(),
					new Date(),
					"test@test.com",
					this.smartspace,
					false,
					new HashMap<String, Object>()));
		
		UserEntity player = this.userDao.create(new UserEntity("player@test.com",
				this.smartspace,
				"player", 
				null,
				UserRole.PLAYER,
				0));
		//WHEN player want to get the specific element
		
		this.restTemplate.getForObject
		(baseUrl, ElementBoundary.class,
				player.getUserSmartspace(),
				player.getUserEmail(),
				element.getElementSmartspace(),
				element.getKey().getId());
		
//		//THEN method return the element
//		assertThat(this.elementDao.readById(element.getKey())
//				.orElseThrow( ()-> new RuntimeException("not in db"))
//				.toString().equals(rv.convertToEntity().toString())
//				);
	}
	@Test
	public void getSpecificElementThatExistWithExpierdTrueInDBAsPlayer() throws Exception{
		//GIVEN the database contains one element and manger
		ElementEntity element = this.elementDao.create(
				new ElementEntity(
					"Test",
					"test",
					new Location(),
					new Date(),
					"test@test.com",
					this.smartspace,
					false,
					new HashMap<String, Object>()));
		
		UserEntity player = this.userDao.create(new UserEntity("mangaer@test.com",
				this.smartspace,
				"manager", 
				null,
				UserRole.PLAYER,
				0));
		//WHEN player want to get the specific element
		
		this.restTemplate.getForObject
		(baseUrl, ElementBoundary.class,
				player.getUserSmartspace(),
				player.getUserEmail(),
				element.getElementSmartspace(),
				element.getKey().getId());
		
		//THEN method throw exception case the expierd is false
	}

	@Test(expected = Exception.class)
	public void getSpecificElementThatExistWithExpierdTrueInDBAsAdmin() throws Exception{
		//GIVEN the database contains one element and admin
		ElementEntity element = this.elementDao.create(
				new ElementEntity(
					"Test",
					"test",
					new Location(),
					new Date(),
					"test@test.com",
					this.smartspace,
					true,
					new HashMap<String, Object>()));
		
		UserEntity admin = this.userDao.create(new UserEntity("admin@test.com",
				this.smartspace,
				"admin", 
				null,
				UserRole.ADMIN,
				0));
		//WHEN admin want to get the specific element
		
		this.restTemplate.getForObject
		(baseUrl, ElementBoundary.class,
				admin.getUserSmartspace(),
				admin.getUserEmail(),
				element.getElementSmartspace(),
				element.getKey().getId());
		
		//THEN method throw exception case it not player or manager
	}
	@Test(expected = Exception.class)
	public void getSpecificElementThatExistWithExpierdFalseInDBAsAdmin() throws Exception{
		//GIVEN the database contains one element and admin
		ElementEntity element = this.elementDao.create(
				new ElementEntity(
					"Test",
					"test",
					new Location(),
					new Date(),
					"test@test.com",
					this.smartspace,
					false,
					new HashMap<String, Object>()));
		
		UserEntity admin = this.userDao.create(new UserEntity("admin@test.com",
				this.smartspace,
				"admin", 
				null,
				UserRole.ADMIN,
				0));
		//WHEN admin want to get the specific element
		
		this.restTemplate.getForObject
		(baseUrl, ElementBoundary.class,
				admin.getUserSmartspace(),
				admin.getUserEmail(),
				element.getElementSmartspace(),
				element.getKey().getId());
		
		//THEN method throw exception case is not player or manager
	}

	@Test(expected = Exception.class)
	public void getSpecificElementThatNotExist() throws Exception{
		//GIVEN the database contains one element and manger
		this.elementDao.create(
				new ElementEntity(
					"Test",
					"test",
					new Location(),
					new Date(),
					"test@test.com",
					this.smartspace,
					true,
					new HashMap<String, Object>()));
		
		UserEntity manager = this.userDao.create(new UserEntity("mangaer@test.com",
				this.smartspace,
				"manager", 
				null,
				UserRole.MANAGER,
				0));
		//WHEN manager want to get the specific element that not in DB

		
		
		this.restTemplate.getForObject
		(baseUrl, ElementBoundary.class,
				manager.getUserSmartspace(),
				manager.getUserEmail(),
				this.smartspace,
				this.elementDao.readAll().get(0).getKey().getId()-1);
		
		
		//THEN throws Exception
	}
	@Test(expected = Exception.class)
	public void getSpecificElementOfNull() throws Exception{
		//GIVEN the database contains one element and manger
		this.elementDao.create(
				new ElementEntity(
					"Test",
					"test",
					new Location(),
					new Date(),
					"test@test.com",
					this.smartspace,
					true,
					new HashMap<String, Object>()));
		
		UserEntity manager = this.userDao.create(new UserEntity("mangaer@test.com",
				this.smartspace,
				"manager", 
				null,
				UserRole.MANAGER,
				0));
		//WHEN manager want to get the specific element that not in DB

		
		
		this.restTemplate.getForObject
		(baseUrl, ElementBoundary.class,
				manager.getUserSmartspace(),
				manager.getUserEmail(),
				null,
				null);
		
		
		//THEN throws Exception
	}

	public void getSpecificElementWithElementService() throws Exception{
		//GIVEN the database contains one element and manger
		ElementEntity element = this.elementDao.create(
				new ElementEntity(
					"Test",
					"test",
					new Location(),
					new Date(),
					"test@test.com",
					this.smartspace,
					true,
					new HashMap<String, Object>()));
		
		UserEntity manager = this.userDao.create(new UserEntity("mangaer@test.com",
				this.smartspace,
				"manager", 
				null,
				UserRole.MANAGER,
				0));
		//WHEN manager want to get the specific element
		
		ElementEntity rv = this.elementService.getElement(
				manager.getRole(),
				manager.getUserSmartspace(),
				manager.getUserEmail(),
				element.getElementSmartspace(),
				""+element.getKey().getId());
		
		//THEN method return the element
		assertThat(this.elementDao.readById(element.getKey())
				.orElseThrow( ()-> new RuntimeException("not in db"))
				.toString().equals(rv.toString())
				);
	}
}
