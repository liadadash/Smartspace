package smartspace.layout;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
import smartspace.infra.UpdateElementService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.profiles.active=default,test"})
public class UpdateElementIntegrationTests {
	private String baseUrl;
	private int port;
	private RestTemplate restTemplate;
	private EnhancedElementDao<ElementKey> elementDao;
	private EnhancedUserDao<UserKey> userDao;
	private UpdateElementService elementService;
	private String smartspace;
	
	@Autowired
	public void setElementService(UpdateElementService elementService) {
		this.elementService = elementService;
	}
	@Autowired
	public void setElementDao(EnhancedElementDao<ElementKey> elementDao , EnhancedUserDao<UserKey> userDao ) {
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
		this.baseUrl ="http://localhost:"+port+"/smartspace/elements/{managerSmartspace}/{managerEmail}/{elementSmartspace}/{elementId}";
		this.smartspace = "2019B.nadav.peleg";
	}
	@After
	public void tearDown() {
		this.elementDao.deleteAll();
		this.userDao.deleteAll();
	}
	
	@Test
	public void testUpdatElementWithManager() throws Exception{
		// GIVEN the data base contain manager and element
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
			
		//WHEN manager update an element

		ElementBoundary update = new ElementBoundary(element);
		update.setExpired(true);
		
		this.restTemplate.put(baseUrl,update,
				manager.getUserSmartspace(),
				manager.getUserEmail(),
				element.getElementSmartspace(),
				element.getKey().getId());
		
		//THEN database contain 1 updated element
		assertThat(this.elementDao
				.readAll())
				.usingElementComparatorOnFields("expired")
				.containsExactly(update.convertToEntity());
		
	}
	
	@Test(expected = Exception.class)
	public void testUpdateElementWithPlayerRole() throws Exception{
		// GIVEN the data base contain player and element
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
			
		//WHEN manager update an element

		ElementBoundary update = new ElementBoundary(element);
		update.setExpired(true);
		
		this.restTemplate.put(baseUrl,update,
				player.getUserSmartspace(),
				player.getUserEmail(),
				element.getElementSmartspace(),
				element.getKey().getId());
		
		//THEN should throw error
		
	}
	
	@Test(expected = Exception.class)
	public void testUpdateElementWithAdminRole() throws Exception{
		// GIVEN the data base contain player and element
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
		
		UserEntity admin = this.userDao.create(new UserEntity("mangaer@test.com",
				this.smartspace,
				"manager", 
				null,
				UserRole.ADMIN,
				0));
			
		//WHEN manager update an element

		ElementBoundary update = new ElementBoundary(element);
		update.setExpired(true);
		
		this.restTemplate.put(baseUrl,update,
				admin.getUserSmartspace(),
				admin.getUserEmail(),
				element.getElementSmartspace(),
				element.getKey().getId());
		
		//THEN should throw error
		
	}

	@Test
	public void testUpdateElementWhenDBContainSomeElements() throws Exception{
		//GIVEN the database contains some element
		int size=10;
		int index=4;
		List<ElementEntity> elements = IntStream.range(1, size+1).mapToObj(
				i->new ElementEntity("test"+i, "",
						new Location(),
						new Date(),
						"test"+i+"@test.com",
						this.smartspace,false, new HashMap<String, Object>())).collect(Collectors.toList());		
		
		elements = elements.stream().map(this.elementDao::create).collect(Collectors.toList());
		
		UserEntity manager = new UserEntity("manager@test.com",
				smartspace,
				"mangaer",
				null, UserRole.MANAGER,0);
		
		manager = this.userDao.create(manager);
		
		//WHEN i update specific element
		ElementEntity  chosenElement = elements.get(index);
		chosenElement.setExpired(true);

		ElementBoundary update = new ElementBoundary(chosenElement);
		
		this.restTemplate.put(baseUrl, update, 
				manager.getUserSmartspace(),
				manager.getUserEmail(),
				chosenElement.getElementSmartspace(),
				chosenElement.getKey().getId());
		
		//THEN only the specific element update
		assertThat(this.elementDao.readById(chosenElement.getKey())
				.orElseThrow(()->new RuntimeException("no such element")))
				.isEqualToComparingOnlyGivenFields(update.convertToEntity(), "expired");
		
		
		this.elementDao.delete(chosenElement);
		elements.remove(chosenElement);
		
		assertThat(this.elementDao.readAll())
		.usingElementComparatorOnFields("expired")
		.containsAnyElementsOf(elements);
	}

	@Test(expected = Exception.class)
	public void testUpdateSpecificElementWithNullValue() throws Exception{
	
		// GIVEN the data base contain manager and element
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
			
		//WHEN manager update an element

		ElementBoundary update = null;
		
		this.restTemplate.put(baseUrl,update,
				manager.getUserSmartspace(),
				manager.getUserEmail(),
				element.getElementSmartspace(),
				element.getKey().getId());
		
		//THEN should throw exception
		
	
	}

	@Test
	public void testUpdatElementWithManagerUsingService() throws Exception{
		// GIVEN the data base contain manager and element
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
			
		//WHEN manager update an element

		ElementEntity update = new ElementEntity(
					"Test",
					"test",
					new Location(),
					new Date(),
					"test@test.com",
					this.smartspace,
					true,
					new HashMap<String, Object>());
		
		update.setKey(element.getKey());
		
		this.elementService.updateElement(
				manager.getUserSmartspace(),
				manager.getUserEmail(),
				update);
		
		//THEN database contain 1 updated element
		assertThat(this.elementDao
				.readAll())
				.usingElementComparatorOnFields("expired")
				.containsExactly(update);
		
	}
}
