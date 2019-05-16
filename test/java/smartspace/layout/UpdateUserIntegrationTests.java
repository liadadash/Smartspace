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
public class UpdateUserIntegrationTests {
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
	public void testUpdateUserDetails() throws Exception {
		// GIVEN that the database with one user
		UserEntity user = faker.entity().user();// fake valid details
		this.userDao.create(user);
		String lastUserName = user.getUsername();

		// WHEN a updating userName
		UserBoundary updatedUser = new UserBoundary(user);
		updatedUser.setUsername(lastUserName + " the second");

		this.restTemplate.put(baseUrl, updatedUser, UserBoundary.class, user.getUserSmartspace(), user.getUserEmail());

		// THEN the database contains a user with the updated userName
		assertThat(
				this.userDao.readById(new UserKey(user.getUserSmartspace(), user.getUserEmail())).get().getUsername())
						.isEqualTo(updatedUser.getUsername());
	}

	@Test
	public void testUpdateUserWithNullUserName() throws Exception {
		// GIVEN that the database with one user
		UserEntity user = faker.entity().user();// fake valid details
		this.userDao.create(user);
		String lastUserName = user.getUsername();

		// WHEN a updating userName to be null
		UserBoundary updatedUser = new UserBoundary(user);
		updatedUser.setUsername(null);

		this.restTemplate.put(baseUrl, updatedUser, UserBoundary.class, user.getUserSmartspace(), user.getUserEmail());

		// THEN the database contains a user with SAME userName like before(cannot
		// update to NULL)
		assertThat(
				this.userDao.readById(new UserKey(user.getUserSmartspace(), user.getUserEmail())).get().getUsername())
						.isEqualTo(lastUserName);
	}

	@Test
	public void testUpdatePointsToUser() throws Exception {
		// GIVEN that the database with one user
		UserEntity user = faker.entity().user();// fake valid details
		this.userDao.create(user);

		// WHEN a updating user points
		UserBoundary updatedUser = new UserBoundary(user);
		updatedUser.setPoints(user.getPoints() + 100);

		this.restTemplate.put(baseUrl, updatedUser, UserBoundary.class, user.getUserSmartspace(), user.getUserEmail());

		// THEN the database contains a user with SAME points like before(cannot update
		// points)
		assertThat(this.userDao.readById(new UserKey(user.getUserSmartspace(), user.getUserEmail())).get().getPoints())
				.isEqualTo(user.getPoints());
	}
}
