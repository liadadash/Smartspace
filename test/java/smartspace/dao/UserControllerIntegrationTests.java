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

import smartspace.data.UserEntity;
import smartspace.data.UserKey;
import smartspace.data.UserRole;
import smartspace.data.util.Faker;
import smartspace.infra.UserService;
import smartspace.layout.ElementBoundary;
import smartspace.layout.UserBoundary;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = { "spring.profiles.active=default,test" })
public class UserControllerIntegrationTests {
	private String baseUrl;
	private int port;
	private RestTemplate restTemplate;
	private EnhancedUserDao<UserKey> userDao;
	private UserService userService;
	private Faker faker;

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
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
		this.baseUrl = "http://localhost:" + port + "/smartspace/admin/users/{adminSmartspace}/{adminEmail}";
		this.faker = new Faker();
	}

	@After
	public void tearDown() {
		this.userDao.deleteAll();
	}

	@Test
	public void testPostNewUser() throws Exception{
		
		// GIVEN the database contain admin
		UserEntity newAdmin=faker.entity().user();
		newAdmin.setRole(UserRole.ADMIN);
		this.userDao.create(newAdmin);
		
		// WHEN I POST new user
		UserBoundary[] newUser= faker.boundary().userArray(1);
		this.restTemplate.
		postForObject(baseUrl, newUser, UserBoundary[].class,newAdmin.getUserSmartspace(),newAdmin.getUserEmail());
		
		// THEN the database contains a single message
		assertThat(this.userDao
			.readAll())
			.hasSize(2);
	}
	
	
	@Test(expected=Exception.class)
	public void testPostNewUserWithBadAdmin() throws Exception{
		// GIVEN the database contain admin
		
		UserEntity newAdmin=faker.entity().user();
		newAdmin.setRole(UserRole.ADMIN);
		newAdmin.setUserEmail("test@gmail.com");
		this.userDao.create(newAdmin);
		
		// WHEN I POST new user with bad admin
		
		UserBoundary[] newUser= faker.boundary().userArray(1);
		this.restTemplate.
		postForObject(baseUrl, newUser, UserBoundary[].class,newAdmin.getUserSmartspace(),"error@gmail.com");
		
		// THEN the test end with exception
	}

	@Test
	public void testGetAllUsersUsingPagination() throws Exception {
		
		// GIVEN the database contains 2 users and one admin
		
		UserEntity newAdmin = faker.entity().user();
		newAdmin.setRole(UserRole.ADMIN);
		this.userDao.create(newAdmin);
		int size = 2;
		List<UserEntity> users = faker.entity().userList(size);
		users = users.stream().map(userDao::create).collect(Collectors.toList());

		// WHEN I GET users of size 10 and page 0
		UserBoundary[] response = this.restTemplate.getForObject(this.baseUrl + "?size={size}&page={page}",
				UserBoundary[].class, newAdmin.getUserSmartspace(), newAdmin.getUserEmail(), 10, 0);

		// THEN I receive 3 users
		assertThat(response).hasSize(size + 1);
	}

	@Test
	public void testGetAllUsersUsingPaginationAndValidateContent() throws Exception {

		// GIVEN the database contains 2 users and 1 admin
		int size = 2;
		//-------------------------------------create 2 users-------------------------------------
		List<UserEntity> usersEntity = faker.entity().userList(size).stream().map(userDao::create)
				.collect(Collectors.toList());
		//------------------------------------create admin and add to db---------------------------
		UserEntity newAdmin = faker.entity().user();
		newAdmin.setRole(UserRole.ADMIN);
		this.userDao.create(newAdmin);
		usersEntity.add(newAdmin);
		
		List<UserBoundary> usersBoundary = usersEntity.stream().map(UserBoundary::new)
				.collect(Collectors.toList());
		
		// WHEN I GET users of size 10 and page 0
		UserBoundary[] response = this.restTemplate.getForObject(this.baseUrl + "?size={size}&page={page}",
				UserBoundary[].class, newAdmin.getUserSmartspace(), newAdmin.getUserEmail(), 10, 0);

		// THEN I receive the exact 3 users written to the database
		assertThat(response).usingElementComparatorOnFields("key").containsExactlyElementsOf(usersBoundary);
	}

	@Test
	public void testGetAllUsersUsingPaginationAndValidateContentWithAllAttributeValidation() throws Exception {
		
		// GIVEN the database contains 2 users and 1 admin
		
		//------------------------------------create admin ---------------------------
		UserEntity newAdmin = faker.entity().user();
		newAdmin.setRole(UserRole.ADMIN);
		newAdmin.setUserEmail("nofarrrrr@gmail.com");
		this.userDao.create(newAdmin);
		//-------------------------------------create 2 users-------------------------------------
		int size = 2;
		List<UserEntity> usersEntity = faker.entity().userList(size);
			
		List<UserBoundary> usersBoundary=
				this.userService.importUsers(usersEntity,newAdmin.getUserSmartspace(),newAdmin.getUserEmail())
				.stream().map(UserBoundary::new).collect(Collectors.toList());
		usersBoundary.add(0, new UserBoundary(newAdmin));
		
		// WHEN I GET users of size 10 and page 0
		UserBoundary[] response = this.restTemplate.getForObject(this.baseUrl + "?size={size}&page={page}",
				UserBoundary[].class, newAdmin.getUserSmartspace(), newAdmin.getUserEmail(), 10, 0);
		
		
		
		
		// THEN I receive the exact users written to the database
		assertThat(response).usingElementComparatorOnFields("key","role","username","avatar","points")
		.containsExactlyElementsOf(usersBoundary);
	}

	@Test
	public void testGetAllUsersUsingPaginationOfSecondPage() throws Exception{
		
		// GIVEN then database contains 10 users and 1 admin
		
		//-------------------------------------create 10 users-------------------------------------
		int size = 10;
		List<UserEntity> usersEntity = faker.entity().userList(size).stream().map(userDao::create)
				.collect(Collectors.toList());
		
		//------------------------------------create admin and add to db---------------------------
		UserEntity newAdmin = faker.entity().user();
		newAdmin.setRole(UserRole.ADMIN);
		this.userDao.create(newAdmin);
		usersEntity.add(newAdmin);
		//------------------------------------find the last user---------------------------
		UserBoundary lastUser = usersEntity.stream()
				.skip(10).limit(1)
				.map(UserBoundary::new).findFirst()
				.orElseThrow(()->new RuntimeException("no messages after skipping"));

		// WHEN I GET users of size 10 and page 1
		UserBoundary[] result = this.restTemplate
			.getForObject(
					this.baseUrl + "?size={size}&page={page}", 
					UserBoundary[].class, 
					newAdmin.getUserSmartspace(),newAdmin.getUserEmail(),10, 1);
		
		// THEN the result contains a single user (last user)
		assertThat(result)
			.usingElementComparator((b1,b2)->b1.toString().compareTo(b2.toString()))
			.containsExactly(lastUser);
	}

	@Test
	public void testGetAllUersUsingPaginationOfSecondNonExistingPage() throws Exception{
		
		// GIVEN the database contains 9 users and 1 admin
		
		//-------------------------------------create 9 users-------------------------------------
		int size = 9;
		List<UserEntity> usersEntity = faker.entity().userList(size).stream().map(userDao::create)
				.collect(Collectors.toList());
		
		//------------------------------------create admin and add to db---------------------------
		UserEntity newAdmin = faker.entity().user();
		newAdmin.setRole(UserRole.ADMIN);
		this.userDao.create(newAdmin);
		usersEntity.add(newAdmin);
		
		// WHEN I GET users of size 10 and page 1
		String[] result = 
		  this.restTemplate
			.getForObject(
					this.baseUrl + "?size={size}&page={pp}", 
					String[].class, 
					newAdmin.getUserSmartspace(),newAdmin.getUserEmail(),10, 1);
		
		// THEN the result is empty
		assertThat(result)
			.isEmpty();
		
	}
	
	@Test
	public void testPostInvalidUsersWithValidUsers() throws Exception {
		// GIVEN the database contains an admin user
		UserEntity admin = this.userDao.create(faker.entity().user(UserRole.ADMIN));
		
		// WHEN I post valid elements together with invalid elements
		UserBoundary[] users = faker.boundary().userArray(5);
		users[3].setUsername(null);
		users[3].setAvatar(null);
		
		// THEN there is an exception and the database should only contain the admin user (@Transactional behavior working as intended)
		try {
			this.restTemplate.postForObject(this.baseUrl, users, UserBoundary[].class, admin.getUserSmartspace(), admin.getUserEmail());
			throw new RuntimeException("some users are invalid but there was no exception"); // will only get to this line if there was no exception
		} catch (Exception e) {
			assertThat(this.userDao.readAll()).usingElementComparatorOnFields("key").containsExactly(admin);
		}
	}
	
	@Test
	public void testAdminCheckIsCaseInsensitive() throws Exception {
		// GIVEN the database contains an admin user
		UserEntity admin = this.userDao.create(faker.entity().user(new UserKey("smartspace", "Test@gmail.com"), UserRole.ADMIN));
		
		// WHEN I post 3 users using different spellings
		this.restTemplate.postForObject(this.baseUrl, faker.boundary().userArray(1), UserBoundary[].class, admin.getUserSmartspace(), "test@gmail.com");
		this.restTemplate.postForObject(this.baseUrl, faker.boundary().userArray(1), UserBoundary[].class, admin.getUserSmartspace(), "TEST@gmail.com");
		this.restTemplate.postForObject(this.baseUrl, faker.boundary().userArray(1), UserBoundary[].class, admin.getUserSmartspace(), "tESt@gMaIl.cOm");
		
		// THEN there should be 3 users in the database + 1 admins
		assertThat(this.userDao.readAll()).hasSize(4);
	}

}
