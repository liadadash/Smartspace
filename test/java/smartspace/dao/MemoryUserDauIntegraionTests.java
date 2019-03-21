package smartspace.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import smartspace.data.UserEntity;
import smartspace.data.UserKey;
import smartspace.data.UserRole;
import smartspace.data.util.EntityFactory;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = { "spring.profiles.active=default" })

//aviel
public class MemoryUserDauIntegraionTests {

	private UserDao<UserKey> dao;
	private EntityFactory factory;

	@Autowired
	public void setDao(UserDao<UserKey> dao) {
		this.dao = dao;
	}

	@Autowired
	public void setFactory(EntityFactory factory) {
		this.factory = factory;
	}

	@Before
	public void setup() {
		dao.deleteAll();
	}

	@After
	public void teardown() {
		dao.deleteAll();
	}

	@Test(expected = Exception.class)
	public void testCreateWithNullUser() throws Exception {
		// GIVEN nothing

		// WHEN i create Null message
		this.dao.create(null);

		// Then create method throws exception
	}

	@Test
	public void testCreateReadByIdWithValidUser() throws Exception {
		// GIVEN dao is initialized and clean

		// WHEN I create a user
		String smartspace = " testSmartspace";
		String email = "test@gmail.com";

		UserEntity user = this.factory.createNewUser(email, smartspace, "test1", null, UserRole.PLAYER, 0);
		// AND we get the user by key

		UserEntity userInDB = this.dao.create(user);

		UserEntity userFromDB = this.dao.readById(userInDB.getKey())
				.orElseThrow(() -> new RuntimeException("could not find user by key"));

		// THEN the same user is returned
		assertThat(userFromDB).isNotNull().extracting("userSmartspace", "userEmail", "role").containsExactly(smartspace,
				email, UserRole.PLAYER);
	}

	@Test
	public void testCreateUpdateReadByIdDeleteAllReadAll() throws Exception {
		// GIVEN nothing

		// WHEN I create a new user
		String smartspace = " testSmartspace";
		String email = "test@gmail.com";

		UserEntity user = this.factory.createNewUser(email, smartspace, "test1", null, UserRole.PLAYER, 0);
		user = this.dao.create(user);

		// AND update user
		UserEntity update = new UserEntity();
		update.setRole(UserRole.MANAGER);
		update.setPoints(1000);
		update.setKey(user.getKey());
		this.dao.update(update);
		// AND get user by key
		this.dao.readById(user.getKey()).orElseThrow(() -> new RuntimeException("not user after update"));
		// AND Delete all users
		this.dao.deleteAll();
		// AND read all
		List<UserEntity> list = this.dao.readAll();

		// THEN the created user received an id != null
		// AND the dao contains nothing

		assertThat(user.getKey()).isNotNull();
		assertThat(list).isEmpty();

	}

}
