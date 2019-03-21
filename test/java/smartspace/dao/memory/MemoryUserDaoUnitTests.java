package smartspace.dao.memory;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import smartspace.data.UserEntity;
import smartspace.data.UserKey;
import smartspace.data.UserRole;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties= {"spring.profiles.active=default"})
public class MemoryUserDaoUnitTests {
	
	@Test
	public void testUserEntityCreateMethod() throws Exception {
		// GIVEN a Dao is available
		MemoryUserDao dao = new MemoryUserDao();
		
		// WHEN we create a new user entity
		// AND we invoke create on the dao
		UserEntity user = new UserEntity("john134@gmail.com", "2019B.nadav.peleg", "johncool", "urltopicture.jpg", UserRole.PLAYER, 0);
		UserEntity rvUser = dao.create(user);
	
		// THEN the user was added to the dao
		// AND the user has a key which isn't null
		// AND the key contains user's email and user's smartspace
		
		assertThat(dao.getUsers()).usingElementComparatorOnFields("userEmail, username").contains(user);
		assertThat(rvUser.getKey()).isNotNull();
		assertThat(rvUser.getKey().getUserEmail()).isEqualTo(user.getUserEmail());
		assertThat(rvUser.getKey().getUserSmartspace()).isEqualTo(user.getUserSmartspace());
	}
	
	@Test
	public void testUserEntityReadAllMethod() throws Exception {
		// GIVEN a Dao is available
		MemoryUserDao dao = new MemoryUserDao();
		
		// WHEN we create a new user entities
		// AND we invoke create on the dao
		UserEntity user1 = new UserEntity("john134@gmail.com", "2019B.nadav.peleg", "johncool", "urltopicture1.jpg", UserRole.PLAYER, 0);
		UserEntity user2 = new UserEntity("david134@gmail.com", "2019B.nadav.peleg", "david673", "urltopicture2.jpg", UserRole.PLAYER, 10);
		UserEntity user3 = new UserEntity("omer454@gmail.com", "2019B.nadav.peleg", "omer2019", "urltopicture3.jpg", UserRole.PLAYER, 20);
		UserEntity user4 = new UserEntity("lori64@gmail.com", "2019B.nadav.peleg", "goldfish", "urltopicture4.jpg", UserRole.PLAYER, 30);
		
		UserEntity rvUser1 = dao.create(user1);
		UserEntity rvUser2 = dao.create(user2);
		UserEntity rvUser3 = dao.create(user3);
		UserEntity rvUser4 = dao.create(user4);
		
		// THEN all users were  added to the dao
		assertThat(dao.readAll()).contains(user1).contains(user2).contains(user3).contains(user4);
	}
	
	@Test
	public void testUserEntityReadByIdMethod() throws Exception {
		// GIVEN a Dao is available
		MemoryUserDao dao = new MemoryUserDao();
		
		// WHEN we use getById for exisiting user
		UserEntity user = new UserEntity("john134@gmail.com", "2019B.nadav.peleg", "johncool", "urltopicture.jpg", UserRole.PLAYER, 0);
		user.setKey(new UserKey(user.getUserSmartspace(), user.getUserEmail()));
		
		List<UserEntity> users = Collections.synchronizedList(new ArrayList<>());
		users.add(user);
		dao.setUsers(users);
		
		Optional<UserEntity> searchResult = dao.readById(user.getKey());
	
		// THEN the user was found
		assertThat(searchResult.get()).isNotNull().isEqualTo(user);

	}
	
	@Test
	public void testUserEntityUpdateMethod() throws Exception {
		// GIVEN a Dao is available
		MemoryUserDao dao = new MemoryUserDao();
		
		// WHEN we use update method
		UserEntity user = new UserEntity("john134@gmail.com", "2019B.nadav.peleg", "johncool", "urltopicture.jpg", UserRole.PLAYER, 0);
		user.setKey(new UserKey(user.getUserSmartspace(), user.getUserEmail()));
				
		List<UserEntity> users = Collections.synchronizedList(new ArrayList<>());
		users.add(user);
		dao.setUsers(users);
		
		// 	--	update some values --
		UserEntity updatedUser = new UserEntity();
		
		String newUsername = "john_cool53";
		String newAvatar = "http://www.dogbreedslist.info/uploads/allimg/dog-pictures/Rottweiler-2.jpg";
		long newPoints = 50;
		
		updatedUser.setUsername(newUsername);
		updatedUser.setAvatar(newAvatar);
		updatedUser.setPoints(newPoints);
	
		updatedUser.setKey(user.getKey());
		dao.update(updatedUser);
	
		// THEN all fields of user were updated 
		assertThat(user).isNotNull().extracting("username", "avatar", "points").containsExactly(newUsername, newAvatar, newPoints);
	}
	

}