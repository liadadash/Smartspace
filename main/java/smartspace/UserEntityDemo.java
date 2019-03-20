package smartspace;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import smartspace.dao.UserDao;
import smartspace.data.UserEntity;
import smartspace.data.UserKey;
import smartspace.data.UserRole;
import smartspace.data.util.EntityFactoryImpl;

@Component
public class UserEntityDemo implements CommandLineRunner {
	private EntityFactoryImpl factory;
	private UserDao<UserKey> dao;

	public UserEntityDemo() {
	}

	@Autowired
	public UserEntityDemo(EntityFactoryImpl factory, UserDao<UserKey> dao) {
		this.factory = factory;
		this.dao = dao;
	}

	@Override
	public void run(String... args) throws Exception {

		String smartspace = "ShoppingList";
		System.err.println("\n---------- UserEntityDemo ----------");

		UserEntity user1 = this.factory.createNewUser("john123@gmail.com", smartspace, "cooljohn53",
				"https://pbs.twimg.com/profile_images/1006019117101432834/5lundpuo_400x400.jpg", UserRole.PLAYER, 0);
		user1.setUserSmartspace(smartspace);

		System.err.println("user before saving to database: " + user1);

		user1 = this.dao.create(user1);
		System.err.println("user after saving to database: " + user1);

		UserEntity updatedUser = new UserEntity();

		// update some values
		updatedUser.setUsername("john_cool53");
		updatedUser.setAvatar("http://www.dogbreedslist.info/uploads/allimg/dog-pictures/Rottweiler-2.jpg");
		updatedUser.setPoints(50);

		// set search key
		updatedUser.setUserEmail(user1.getUserEmail());
		updatedUser.setUserSmartspace(user1.getUserSmartspace());

		// update in database
		this.dao.update(updatedUser);

		// load from database by id
		Optional<UserEntity> userLoad = this.dao.readById(user1.getKey());
		if (userLoad.isPresent()) {
			user1 = userLoad.get();
		} else {
			throw new RuntimeException("user was lost after update");
		}

		System.err.println("user after update: " + user1);

		// delete all entities
		this.dao.deleteAll();
		if (this.dao.readAll().isEmpty()) {
			System.err.println("\nall users deleted successfully");
		} else {
			throw new RuntimeException("\nsome users were not deleted");
		}

	}

}
