package smartspace.layout;

import static org.assertj.core.api.Assertions.assertThat;

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
import org.springframework.web.client.RestTemplate;

import smartspace.dao.EnhancedUserDao;
import smartspace.data.UserEntity;
import smartspace.data.UserKey;
import smartspace.data.util.Faker;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = { "spring.profiles.active=default, test" })
public class RetrieveUserIntegrationTests {

	@Value("${smartspace.name}")
	private String appSmartspace;

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
		this.baseUrl = "http://localhost:" + port + "smartspace/users/login/{userSmartspace}/{userEmail}";
		this.faker = new Faker();
	}

	@After
	public void tearDown() {
		this.userDao.deleteAll();
	}

	@Test
	public void testGetUserDetails() throws Exception {
		// GIVEN that the database with 3 users
		UserEntity user1 = faker.entity().user();// fake valid details
		this.userDao.create(user1);
		UserEntity user2 = faker.entity().user();// fake valid details
		this.userDao.create(user2);
		UserEntity user3 = faker.entity().user();// fake valid details
		this.userDao.create(user3);

		// WHEN send get request for one of the users
		UserBoundary requestedUser2 = this.restTemplate.getForObject(baseUrl, UserBoundary.class,
				user2.getUserSmartspace(), user2.getUserEmail());

		// THEN we will get from db the user we asked for
		assertThat(
				this.userDao.readById(new UserKey(user2.getUserSmartspace(), user2.getUserEmail())).get().getUsername())
						.isEqualTo(requestedUser2.getUsername());
	}

	@Test(expected = Exception.class)
	public void testUpdateUserWithNullUserName() throws Exception {
		// GIVEN that the database with 3 users
		UserEntity user1 = faker.entity().user();// fake valid details
		this.userDao.create(user1);
		UserEntity user2 = faker.entity().user();// fake valid details
		this.userDao.create(user2);
		UserEntity user3 = faker.entity().user();// fake valid details
		this.userDao.create(user3);

		// WHEN trying to get user that not exist in db

		this.restTemplate.getForObject(baseUrl, UserBoundary.class, user3.getUserSmartspace(),
				user3.getUserEmail() + "!!!");

		// THEN method throw exception case it not player or manager
	}
}
