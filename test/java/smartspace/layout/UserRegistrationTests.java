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

import smartspace.dao.EnhancedUserDao;
import smartspace.data.UserEntity;
import smartspace.data.UserKey;
import smartspace.data.UserRole;
import smartspace.data.util.Faker;
import smartspace.layout.UserBoundary;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = { "spring.profiles.active=default, test" })
public class UserRegistrationTests {
	private String baseUrl;
	private int port;
	private RestTemplate restTemplate;
	private EnhancedUserDao<UserKey> userDao;
	private Faker faker;

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
		this.baseUrl = "http://localhost:" + port + "/smartspace/users";
		this.faker = new Faker();
	}

	@After
	public void tearDown() {
		this.userDao.deleteAll();
	}

	@Test
	public void testRegisterNewUser() throws Exception {
		// GIVEN that the database is empty
		
		// WHEN a new user registers
		UserFormBoundary newUser = new UserFormBoundary(faker.entity().user()); // fake valid details
		this.restTemplate.postForObject(baseUrl, newUser, UserFormBoundary.class);
		
		// THEN the database contains a single user
		assertThat(this.userDao.readAll()).hasSize(1);
	}
	
	@Test
	public void testRegisterManyUsers() throws Exception {
		// GIVEN that the database is empty
		
		// WHEN x users register
		int usersAmount = faker.generateNumber(1, 100);		
		List<UserFormBoundary> users2 = faker.entity().userList(usersAmount).stream()
				.map(UserFormBoundary::new)
				.map(user->this.restTemplate.postForObject(baseUrl, user, UserFormBoundary.class))
				.collect(Collectors.toList());
		
		// THEN the database contains all added users
		assertThat(users2).hasSize(usersAmount);
		assertThat(this.userDao.readAll()).hasSize(usersAmount);
	}
	
	@Test
	public void testRegisterAllRoles() throws Exception {
		// GIVEN that the database is empty
		
		// WHEN I add 3 users with different roles
		UserFormBoundary newUser = new UserFormBoundary(faker.entity().user(UserRole.PLAYER)); // fake valid details
		this.restTemplate.postForObject(baseUrl, newUser, UserFormBoundary.class);
		
		newUser = new UserFormBoundary(faker.entity().user(UserRole.MANAGER)); // fake valid details
		this.restTemplate.postForObject(baseUrl, newUser, UserFormBoundary.class);
		
		newUser = new UserFormBoundary(faker.entity().user(UserRole.ADMIN)); // fake valid details
		this.restTemplate.postForObject(baseUrl, newUser, UserFormBoundary.class);
		
		// THEN the database contains 3 users
		assertThat(this.userDao.readAll()).hasSize(3);
	}
	
	@Test(expected=Exception.class)
	public void testWrongBoundary() throws Exception {
		// WHEN I try to register a userBoundary object
		UserBoundary newUser = new UserBoundary(faker.entity().user(UserRole.PLAYER)); // fake valid details
		this.restTemplate.postForObject(baseUrl, newUser, UserBoundary.class);
		
		// THEN there is an exception
	}
	
	@Test(expected=Exception.class)
	public void testRegisterEmailAlreadyTaken() throws Exception {
		// GIVEN that the database has a user with some email
		UserEntity someUser = faker.entity().user();
		this.userDao.create(someUser);
		
		// WHEN a new user registers with the same email
		UserFormBoundary newUser = new UserFormBoundary(faker.entity().user());
		newUser.setEmail(someUser.getUserEmail());
		
		this.restTemplate.postForObject(baseUrl, newUser, UserFormBoundary.class);
		
		// THEN there is an exception
	}
	
	@Test(expected=Exception.class)
	public void testUsernameValidation() throws Exception {
		// WHEN a new user registers with empty username
		UserFormBoundary newUser = new UserFormBoundary(faker.entity().user());
		newUser.setUsername(null);
		
		this.restTemplate.postForObject(baseUrl, newUser, UserFormBoundary.class);
		// THEN there is an exception
	}
	
	@Test(expected=Exception.class)
	public void testUsernameValidation2() throws Exception {
		// WHEN a new user registers with empty username
		UserFormBoundary newUser = new UserFormBoundary(faker.entity().user());
		newUser.setUsername("     ");
		
		this.restTemplate.postForObject(baseUrl, newUser, UserFormBoundary.class);
		// THEN there is an exception
	}
	
	@Test(expected=Exception.class)
	public void testAvatarValidation() throws Exception {
		// WHEN a new user registers with bad avatar
		UserFormBoundary newUser = new UserFormBoundary(faker.entity().user());
		newUser.setAvatar("");
		
		this.restTemplate.postForObject(baseUrl, newUser, UserFormBoundary.class);
		// THEN there is an exception
	}
	
	@Test(expected=Exception.class)
	public void testEmailValidation1() throws Exception {
		// WHEN a new user registers with bad email
		UserFormBoundary newUser = new UserFormBoundary(faker.entity().user());
		newUser.setEmail("john7");
		
		this.restTemplate.postForObject(baseUrl, newUser, UserFormBoundary.class);
		// THEN there is an exception
	}
	
	@Test(expected=Exception.class)
	public void testEmailValidation2() throws Exception {
		// WHEN a new user registers with bad email
		UserFormBoundary newUser = new UserFormBoundary(faker.entity().user());
		newUser.setEmail("john34@gmail");
		
		this.restTemplate.postForObject(baseUrl, newUser, UserFormBoundary.class);
		// THEN there is an exception
	}
	
	@Test(expected=Exception.class)
	public void testEmailValidation3() throws Exception {
		// WHEN a new user registers with bad email
		UserFormBoundary newUser = new UserFormBoundary(faker.entity().user());
		newUser.setEmail("johngmail.com");
		
		this.restTemplate.postForObject(baseUrl, newUser, UserFormBoundary.class);
		// THEN there is an exception
	}

}
