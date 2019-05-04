package smartspace.dao;

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

import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;
import smartspace.data.UserEntity;
import smartspace.data.UserKey;
import smartspace.data.UserRole;
import smartspace.data.util.Faker;
import smartspace.infra.ElementService;
import smartspace.layout.ActionBoundary;
import smartspace.layout.ElementBoundary;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = { "spring.profiles.active=default,test" })
public class ElementControllerIntegrationTests {
	private String baseUrl;
	private int port;
	private RestTemplate restTemplate;
	private EnhancedElementDao<ElementKey> elementDao;
	private EnhancedUserDao<UserKey> userDao;
	private ElementService elementService;
	private Faker faker;
	

	@Autowired
	public void setElementService(ElementService elementService) {
		this.elementService = elementService;
	}

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
		this.baseUrl = "http://localhost:" + port + "/smartspace/admin/elements/{adminSmartspace}/{adminEmail}";
		this.faker=new Faker();
	}

	@After
	public void tearDown() {
		this.elementDao.deleteAll();
		this.userDao.deleteAll();
	}
	
	@Test
	public void testPostNewElement() throws Exception{
		// GIVEN the database contain admin
		UserEntity newAdmin=faker.entity().user();
		newAdmin.setRole(UserRole.ADMIN);
		this.userDao.create(newAdmin);
		
		// WHEN I POST new element
		ElementBoundary[] newElement=faker.boundary().elementArray(1);
		this.restTemplate
			.postForObject(this.baseUrl, newElement, ElementBoundary[].class, newAdmin.getUserSmartspace(),newAdmin.getUserEmail());
		
		// THEN the database contains a single element
		assertThat(this.elementDao
			.readAll())
			.hasSize(1);
	}
	
	@Test(expected=Exception.class)
	public void testPostNewElementWithBadAdmin() throws Exception{
		// GIVEN the database contain admin
		UserEntity newAdmin=faker.entity().user();
		newAdmin.setRole(UserRole.ADMIN);
		newAdmin.setUserEmail("test@gmail.com");
		this.userDao.create(newAdmin);
		
		// WHEN I POST new element with bad admin
		ElementBoundary[] newElement=faker.boundary().elementArray(1);
		
		this.restTemplate
			.postForObject(
					this.baseUrl, 
					newElement, 
					ElementBoundary[].class, 
					newAdmin.getUserSmartspace(),"err@gmail.com");
		
		// THEN the test end with exception
	}
	
	@Test
	public void testGetAllElementsUsingPagination() throws Exception {
		// GIVEN the database contains 2 elements and one admin
		UserEntity newAdmin = faker.entity().user();
		newAdmin.setRole(UserRole.ADMIN);
		this.userDao.create(newAdmin);
		int size = 2;
		List<ElementEntity> elements = faker.entity().elementList(size);
		elements=elements.stream().map(elementDao::create).collect(Collectors.toList());

		// WHEN I GET elements of size 10 and page 0
		ElementBoundary[] response = this.restTemplate.getForObject(this.baseUrl + "?size={size}&page={page}",
				ElementBoundary[].class, newAdmin.getUserSmartspace(), newAdmin.getUserEmail(), 10, 0);

		// THEN I receive 2 elements
		assertThat(response).hasSize(size );
	}
	
	@Test
	public void testGetAllElementsUsingPaginationAndValidateContent() throws Exception {

		// GIVEN the database contains 2 elements and 1 admin
		
		//------------------------------------create admin and add to db---------------------------
		UserEntity newAdmin = faker.entity().user();
		newAdmin.setRole(UserRole.ADMIN);
		this.userDao.create(newAdmin);
		//-------------------------------------create 2 elements-------------------------------------
		int size = 2;
		List<ElementEntity> elemetnsEntity = faker.entity().elementList(size).stream().map(elementDao::create)
				.collect(Collectors.toList());
		
		List<ElementBoundary> elementsBoundary = elemetnsEntity.stream().map(ElementBoundary::new)
				.collect(Collectors.toList());
		
		// WHEN I GET elements of size 10 and page 0
		ElementBoundary[] response = this.restTemplate.getForObject(this.baseUrl + "?size={size}&page={page}",
				ElementBoundary[].class, newAdmin.getUserSmartspace(), newAdmin.getUserEmail(), 10, 0);

		// THEN I receive the exact 2 elements written to the database
		assertThat(response).usingElementComparatorOnFields("key").containsExactlyElementsOf(elementsBoundary);
	}
	
	@Test
	public void testGetAllElemetsUsingPaginationAndValidateContentWithAllAttributeValidation() throws Exception {

		// GIVEN the database contains 2 elements and 1 admin
		
		//------------------------------------create admin ---------------------------
		UserEntity newAdmin = faker.entity().user();
		newAdmin.setRole(UserRole.ADMIN);
		this.userDao.create(newAdmin);
				
		//-------------------------------------create 2 elements-------------------------------------
		int size = 2;
		List<ElementEntity> elemntsEntity = faker.entity().elementList(size);
		List<ElementBoundary> elementsBoundary=
				this.elementService.importElements(elemntsEntity, newAdmin.getUserSmartspace(), newAdmin.getUserEmail())
				.stream().map(ElementBoundary::new).collect(Collectors.toList());
		
		// WHEN I GET elements of size 10 and page 0
		ElementBoundary[] response = this.restTemplate.getForObject(this.baseUrl + "?size={size}&page={page}",
				ElementBoundary[].class, newAdmin.getUserSmartspace(), newAdmin.getUserEmail(), 10, 0);

		// THEN I receive the exact elements written to the database
		assertThat(response).usingElementComparatorOnFields("key","elementType","name","expired","created","creator","latlng","elementProperties")
		.containsExactlyElementsOf(elementsBoundary);
	}
	

	@Test
	public void testGetAllElementsUsingPaginationOfSecondPage() throws Exception{
		
		// GIVEN then database contains 11 elements and 1 admin
		
		//------------------------------------create admin and add to db---------------------------
		UserEntity newAdmin = faker.entity().user();
		newAdmin.setRole(UserRole.ADMIN);
		this.userDao.create(newAdmin);
	
		//-------------------------------------create 11 elements-------------------------------------
		int size = 11;
		List<ElementEntity> elementsEntity = faker.entity().elementList(size).stream().map(elementDao::create)
				.collect(Collectors.toList());
		
		//------------------------------------find the last element---------------------------
		ElementBoundary lastElement = elementsEntity.stream()
				.skip(10).limit(1)
				.map(ElementBoundary::new).findFirst()
				.orElseThrow(()->new RuntimeException("no messages after skipping"));

		// WHEN I GET elements of size 10 and page 1
		ElementBoundary[] result = this.restTemplate
			.getForObject(
					this.baseUrl + "?size={size}&page={page}", 
					ElementBoundary[].class, 
					newAdmin.getUserSmartspace(),newAdmin.getUserEmail(),10, 1);
		
		// THEN the result contains a single element (last element)
		assertThat(result)
			.usingElementComparator((b1,b2)->b1.toString().compareTo(b2.toString()))
			.containsExactly(lastElement);
	}
	
	@Test
	public void testGetAllElementsUsingPaginationOfSecondNonExistingPage() throws Exception{
		
		// GIVEN the database contains 10 elements and 1 admin
		
		//------------------------------------create admin and add to db---------------------------
		UserEntity newAdmin = faker.entity().user();
		newAdmin.setRole(UserRole.ADMIN);
		this.userDao.create(newAdmin);
		//-------------------------------------create 10 elements-------------------------------------
		int size = 10;
	    faker.entity().elementList(size).stream().map(elementDao::create)
				.collect(Collectors.toList());
		
		// WHEN I GET elements of size 10 and page 1
		String[] result = 
		  this.restTemplate
			.getForObject(
					this.baseUrl + "?size={size}&page={page}", 
					String[].class, 
					newAdmin.getUserSmartspace(),newAdmin.getUserEmail(),10, 1);
		
		// THEN the result is empty
		assertThat(result)
			.isEmpty();
		
	}
	
	@Test
	public void testPostInvalidElementsWithValidElements() throws Exception {
		// GIVEN the database contains an admin user
		UserEntity admin = this.userDao.create(faker.entity().user(UserRole.ADMIN));
		
		// WHEN I post valid elements together with invalid elements
		ElementBoundary[] elements = faker.boundary().elementArray(5);
		elements[3].setKey(null);
		elements[3].setName(null);
		
		// THEN there is an exception and the database should be empty (@Transactional behavior working as intended)
		try {
			this.restTemplate.postForObject(this.baseUrl, elements, ElementBoundary[].class, admin.getUserSmartspace(), admin.getUserEmail());
			throw new RuntimeException("some elements are invalid but there was no exception"); // will only get to this line if there was no exception
		} catch (Exception e) {
			assertThat(this.elementDao.readAll()).isEmpty();
		}
	}
	
}
