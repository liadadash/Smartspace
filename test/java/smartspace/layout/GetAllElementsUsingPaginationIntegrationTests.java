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
import smartspace.infra.ElementServiceForManagerOrPlayer;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.profiles.active=default,test"})
public class GetAllElementsUsingPaginationIntegrationTests {

	private String baseUrl;
	private int port;
	private RestTemplate restTemplate;
	private EnhancedElementDao<ElementKey> elementDao;
	private EnhancedUserDao<UserKey> userDao;
	private ElementServiceForManagerOrPlayer elementService;
	private String smartspace;

	
	@Autowired
	public void setElementService(ElementServiceForManagerOrPlayer elementService) {
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
		this.baseUrl = "http://localhost:"+port+"/smartspace/elements/{userSmartspace}/{userEmail}";
		this.smartspace = "2019B.nadav.peleg";
	}
	@After
	public void tearDown() {
		this.elementDao.deleteAll();
		this.userDao.deleteAll();
	}
	
	@Test
	public void getAllElementsUsingPaginationWithManger() throws Exception{
		//GIVEN DB contains 20 elements 10 expired true 10 expired false
		
		int size=10;
		
		List<ElementEntity> notExpiredElements = IntStream.range(1, size+1)
				.mapToObj( i -> this.elementDao.create(new ElementEntity(
						"test"+i,
						"",
						new Location(), 
						new Date(), 
						"test"+i+"@test.com",
						this.smartspace,
						false, 
						new HashMap<String, Object>()))).collect(Collectors.toList());
		
		List<ElementEntity> ExpiredElements = IntStream.range(1, size+1)
				.mapToObj( i -> this.elementDao.create(new ElementEntity(
						"test"+i,
						"",
						new Location(), 
						new Date(), 
						"test"+i+"@test.com",
						this.smartspace,
						true, 
						new HashMap<String, Object>()))).collect(Collectors.toList());
		
		UserEntity manager = this.userDao.create(new UserEntity(
				"manager@test.com",
				this.smartspace,
				"manager",
				null,
				UserRole.MANAGER,
				0));
		
		//WHEN manager want to get all elements with pagination
		
		ElementBoundary[] rv = this.restTemplate.getForObject
		(baseUrl+"?size={size}&page={page}", ElementBoundary[].class,
				manager.getUserSmartspace(),
				manager.getUserEmail(),size*2,0);
						

		//THEN return a 20 element with pagination
		assertThat(rv).hasSize(size*2);	
	}

	@Test
	public void getAllElementsUsingPaginationWithPlayer() throws Exception{
		//GIVEN DB contains 20 elements 10 expired true 10 expired false
		
		int size=10;
		
		List<ElementEntity> notExpiredElements = IntStream.range(1, size+1)
				.mapToObj( i -> this.elementDao.create(new ElementEntity(
						"test"+i,
						"",
						new Location(), 
						new Date(), 
						"test"+i+"@test.com",
						this.smartspace,
						false, 
						new HashMap<String, Object>()))).collect(Collectors.toList());
		
		List<ElementEntity> ExpiredElementsEntity = IntStream.range(1, size+1)
				.mapToObj( i -> this.elementDao.create(new ElementEntity(
						"test"+i,
						"",
						new Location(), 
						new Date(), 
						"test"+i+"@test.com",
						this.smartspace,
						true, 
						new HashMap<String, Object>()))).collect(Collectors.toList());
		
		List<ElementBoundary> ExpiredElementsBoundry = 
				ExpiredElementsEntity.stream().map(ElementBoundary::new).collect(Collectors.toList());
				
		UserEntity player = this.userDao.create(new UserEntity(
				"player@test.com",
				this.smartspace,
				"player",
				null,
				UserRole.PLAYER,
				0));
		
		//WHEN player want to get all elements with pagination
		
		ElementBoundary[] rv = this.restTemplate.getForObject
		(baseUrl+"?size={size}&page={page}", ElementBoundary[].class,
				player.getUserSmartspace(),
				player.getUserEmail(),size*2,0);
						
		

		//THEN return a 10 element with pagination and there expierd is false
		assertThat(rv).hasSize(size)
		.usingElementComparatorIgnoringFields("key","elementType","name","expired","created","creator","latlng","elementProperties")
		.containsExactlyElementsOf(ExpiredElementsBoundry );
	}

	@Test
	public void getAllElementsUsingPaginationWithPlayerRoleAndExpierdElements() throws Exception{
		//GIVEN DB contains 10 elements expired true and player
		
		int size=10;
		
		List<ElementEntity> ExpiredElementsEntity = IntStream.range(1, size+1)
				.mapToObj( i -> this.elementDao.create(new ElementEntity(
						"test"+i,
						"",
						new Location(), 
						new Date(), 
						"test"+i+"@test.com",
						this.smartspace,
						true, 
						new HashMap<String, Object>()))).collect(Collectors.toList());
		
		List<ElementBoundary> ExpiredElementsBoundry = 
				ExpiredElementsEntity.stream().map(ElementBoundary::new).collect(Collectors.toList());
				
		UserEntity player = this.userDao.create(new UserEntity(
				"player@test.com",
				this.smartspace,
				"player",
				null,
				UserRole.PLAYER,
				0));
		
		//WHEN player want to get all elements with pagination
		
		ElementBoundary[] rv = this.restTemplate.getForObject
		(baseUrl+"?size={size}&page={page}", ElementBoundary[].class,
				player.getUserSmartspace(),
				player.getUserEmail(),size*2,0);
						
		

		//THEN return a 0 element with pagination
		assertThat(rv).hasSize(0);
	}

	@Test(expected = Exception.class)
	public void getAllElementsUsingPaginationWithAdmin() throws Exception{
		//GIVEN DB contains 20 elements 10 expired true 10 expired false
		
		int size=10;
		
		List<ElementEntity> notExpiredElements = IntStream.range(1, size+1)
				.mapToObj( i -> this.elementDao.create(new ElementEntity(
						"test"+i,
						"",
						new Location(), 
						new Date(), 
						"test"+i+"@test.com",
						this.smartspace,
						false, 
						new HashMap<String, Object>()))).collect(Collectors.toList());
		
		List<ElementEntity> ExpiredElementsEntity = IntStream.range(1, size+1)
				.mapToObj( i -> this.elementDao.create(new ElementEntity(
						"test"+i,
						"",
						new Location(), 
						new Date(), 
						"test"+i+"@test.com",
						this.smartspace,
						true, 
						new HashMap<String, Object>()))).collect(Collectors.toList());
		
		List<ElementBoundary> ExpiredElementsBoundry = 
				ExpiredElementsEntity.stream().map(ElementBoundary::new).collect(Collectors.toList());
				
		UserEntity admin = this.userDao.create(new UserEntity(
				"admin@test.com",
				this.smartspace,
				"admin",
				null,
				UserRole.ADMIN,
				0));
		
		//WHEN admin want to get all elements with pagination
		
		this.restTemplate.getForObject
		(baseUrl+"?size={size}&page={page}", ElementBoundary[].class,
				admin.getUserSmartspace(),
				admin.getUserEmail(),size*2,0);
						
		

		//THEN method throw exception
	}

	public void getAllElementsUsingPaginationWithPlayerAndElementService() throws Exception{
		//GIVEN DB contains 20 elements 10 expired true 10 expired false
		
				int size=10;
				
				List<ElementEntity> notExpiredElements = IntStream.range(1, size+1)
						.mapToObj( i -> this.elementDao.create(new ElementEntity(
								"test"+i,
								"",
								new Location(), 
								new Date(), 
								"test"+i+"@test.com",
								this.smartspace,
								false, 
								new HashMap<String, Object>()))).collect(Collectors.toList());
				
				List<ElementEntity> ExpiredElements = IntStream.range(1, size+1)
						.mapToObj( i -> this.elementDao.create(new ElementEntity(
								"test"+i,
								"",
								new Location(), 
								new Date(), 
								"test"+i+"@test.com",
								this.smartspace,
								true, 
								new HashMap<String, Object>()))).collect(Collectors.toList());
				
				UserEntity manager = this.userDao.create(new UserEntity(
						"manager@test.com",
						this.smartspace,
						"manager",
						null,
						UserRole.MANAGER,
						0));
				
				//WHEN manager want to get all elements with pagination
				
				List<ElementEntity> rv = this.elementService.getElementsUsingPagination(manager.getRole(),
						manager.getUserSmartspace(),
						manager.getUserEmail(),
						20,0);
								

				//THEN return a 20 element with pagination
				assertThat(rv).hasSize(size*2);	
	}

	@Test
	public void getAllElementsUsingPaginationWithMangerWithNoElementInDB() throws Exception{
		//GIVEN DB contains just manager
		
		int size=10;

		UserEntity manager = this.userDao.create(new UserEntity(
				"manager@test.com",
				this.smartspace,
				"manager",
				null,
				UserRole.MANAGER,
				0));
		
		//WHEN manager want to get all elements with pagination
		
		ElementBoundary[] rv = this.restTemplate.getForObject
		(baseUrl+"?size={size}&page={page}", ElementBoundary[].class,
				manager.getUserSmartspace(),
				manager.getUserEmail(),size*2,0);
						

		//THEN return a 0 element with pagination
		assertThat(rv).hasSize(0);	
	}


}
