package smartspace.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

//nofar
@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties= {"spring.profiles.active=default, test"})
public class MoreUserDaoIntegrationTests {
	
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

	@Test
	public void testSaveManyUsersWithUniqueKeys() throws Exception{
		//GIVEN the database is clean 
		
		// WHEN I create some users to the database
		int size = 50;
		Set<UserKey> ids = 
				IntStream.range(1, size+1).mapToObj(i->this.factory.createNewUser("test"+i+"@gmail.com", "testSmartspace", "name"+i, "avatar",UserRole.PLAYER,0)).
				map(this.dao::create).map(UserEntity::getKey).collect(Collectors.toSet());
		// THEN no id is repeated
		assertThat(ids).hasSize(size);
	}
	@Test(expected=Exception.class)
	public void testCreateTwoUsersWithSameKey() throws Exception{
		//GIVEN the database is clean 
		
		// WHEN I create same users to the database
		int size = 2;
		Set<UserKey> ids = 
				IntStream.range(1, size+1).mapToObj(i->this.factory.createNewUser("test@gmail.com", "testSmartspace", "name", "avatar",UserRole.PLAYER,0)).
				map(this.dao::create).map(UserEntity::getKey).collect(Collectors.toSet());
		// THEN id is repeated
		assertThat(ids).hasSize(size);
	}
	@Test
	public void testReadUserNotExistInDB() throws Exception{
		//GIVEN the database is clean 
		
		// WHEN I create user 
		// AND i read by id user that not exist in database
		UserEntity user = this.factory.createNewUser("test@gmail.com", null, "test1", null, UserRole.PLAYER, 0);
		Optional<UserEntity> rvUser=this.dao.readById(user.getKey());
		// THEN throw exception
		assertThat(rvUser).isEmpty();
	}

}
