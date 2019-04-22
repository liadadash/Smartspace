package smartspace.dao;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.After;
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
public class MoreUserDaoIntegrstionTests2 {

	private EnhancedUserDao<UserKey> userDao;
	private EntityFactory factory;

	@Autowired
	public void setUserDao(EnhancedUserDao<UserKey> userDao) {
		this.userDao = userDao;
	}

	@Autowired
	public void setFactory(EntityFactory factory) {
		this.factory = factory;
	}

	@After
	public void teardown() {
		this.userDao.deleteAll();
	}

	@Test
	public void testReadAllWithPaginationOfEmptyDB() throws Exception {
		// GIVEN the database is empty

		// WHEN I read 10 users after skipping first 10 users
		List<UserEntity> result = this.userDao.readAllWithPaging(10, 1);
		// THEN I receive no results
		assertThat(result).hasSize(0).isEmpty();
	}

	@Test
	public void testReadAllWithPagination() throws Exception {
		// GIVEN the database contains 50 users
		createStreamUserToDB(0,50);
		// WHEN I read 10 users after skipping first 10 users
		List<UserEntity> result = this.userDao.readAllWithPaging(10, 1);

		// THEN I receive 10 results
		assertThat(result).hasSize(10);
	}

	@Test
	public void testReadAllWithPaginationOfSmallerDB() throws Exception {
		// GIVEN the database contains 12 users
		createStreamUserToDB(0,12);
		// WHEN I read 10 users after skipping first 10 users
		List<UserEntity> result = this.userDao.readAllWithPaging(10, 1);
		// THEN I receive 2 results
		assertThat(result).hasSize(2);
	}
	
	@Test
	public void testReadAllWithPaginationWithInvalidPage() throws Exception {
		// GIVEN the database contains 12 users
		createStreamUserToDB(0,12);
		// WHEN I read 10 users after skipping first 20 users
		List<UserEntity> result = this.userDao.readAllWithPaging(10, 2);
		// THEN I receive no results
		assertThat(result).hasSize(0).isEmpty();
	}
	@Test(expected=Exception.class)
	public void testReadAllWithPaginationWithInvalidSize() throws Exception {
		// GIVEN the database contains 12 users
		createStreamUserToDB(0,12);
		// WHEN I read -10 users after skipping first 20 users
		List<UserEntity> result = this.userDao.readAllWithPaging(-10, 2);
		// THEN I throw exception
		assertThat(result);
	}

	public void createStreamUserToDB(int start,int end) {
		IntStream.range(start, end) // Stream Integer
		.mapToObj(i -> this.factory.createNewUser("test" + i + "@gmail.com", "testSmartspace", "user" + i,
				"avatarTest", UserRole.PLAYER, 0))
		.forEach(this.userDao::create);

	}
}
