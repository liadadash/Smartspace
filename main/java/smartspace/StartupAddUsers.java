package smartspace;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import smartspace.dao.ElementDao;
import smartspace.dao.UserDao;
import smartspace.data.ElementEntity;
import smartspace.data.ElementKey;
import smartspace.data.UserEntity;
import smartspace.data.UserKey;
import smartspace.data.UserRole;
import smartspace.data.util.Faker;

@Component
@Profile("!test")
public class StartupAddUsers implements CommandLineRunner {
	private UserDao<UserKey> dao;
	private ElementDao<ElementKey> elementDao;

	private String appSmartspace;

	public StartupAddUsers() {
	}

	@Autowired
	public StartupAddUsers(UserDao<UserKey> dao, ElementDao<ElementKey> elementDao) {
		this.dao = dao;
		this.elementDao = elementDao;
	}
	
	@Value("${smartspace.name}") 
	public void setAppSmartspace(String appSmartspace) {
		this.appSmartspace = appSmartspace;
	}

	@Override
	public void run(String... args) throws Exception {
		String[] emails = { "bscpkd@gmail.com", "nofaruliel@gmail.com", "liadkh95@gmail.com", "Aviel2845@gmail.com",
				"Amitpelerman@gmail.com" };

		for (String email : emails) {
			dao.create(new UserEntity(email, appSmartspace, email, "image.png", UserRole.ADMIN, 50));
		}

		dao.create(new UserEntity("player@gmail.com", appSmartspace, "player", "image.png", UserRole.PLAYER, 50));
		dao.create(new UserEntity("manager@gmail.com", appSmartspace, "player", "image.png", UserRole.MANAGER, 50));
		dao.create(new UserEntity("admin@gmail.com", appSmartspace, "player", "image.png", UserRole.ADMIN, 50));
		
		// delayed print to show after trace logs
		for (String email : emails) {
			System.err.println("added ADMIN with email: " + email);
		}
		
		System.err.println("added PLAYER with email: player@gmail.com");
		System.err.println("added MANAGER with email: manager@gmail.com");
		System.err.println("added ADMIN with email: admin@gmail.com");
		
		// enable for adding elements on startup
		// Faker faker = new Faker();
		// List<ElementEntity> elements = faker.entity().elementList(10).stream().map(this.elementDao::create).collect(Collectors.toList());
		// List<ElementEntity> elements2 = faker.entity().elementList(10).stream().peek(en->en.setExpired(true)).map(this.elementDao::create).collect(Collectors.toList());
		
	}

}
