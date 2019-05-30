package smartspace;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
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
	private MongoTemplate mongoTemplate;

	private String appSmartspace;

	public StartupAddUsers() {
	}

	@Autowired
	public StartupAddUsers(UserDao<UserKey> dao, ElementDao<ElementKey> elementDao, MongoTemplate mongoTemplate) {
		this.dao = dao;
		this.elementDao = elementDao;
		this.mongoTemplate = mongoTemplate;
	}

	@Value("${smartspace.name}")
	public void setAppSmartspace(String appSmartspace) {
		this.appSmartspace = appSmartspace;
	}

	@Override
	public void run(String... args) throws Exception {
		String[] collections = { "USERS", "ELEMENTS", "ACTIONS", "database_sequences" };
		// IMPORTANT! MongoDB transactions can't create collections!!! couldn't find any other way at the moment.
		// the problem is when a transaction tried to create something into a collection that doesn't exist
		// transactions can't create non existing collections so it fails.
		
		for (String collectionName : collections) {
			if (!mongoTemplate.getCollectionNames().contains(collectionName)) {
				mongoTemplate.createCollection(collectionName);
			}
		}

		// add users
		String[] emails = { "bscpkd@gmail.com", "nofaruliel@gmail.com", "liadkh95@gmail.com", "Aviel2845@gmail.com",
				"Amitpelerman@gmail.com" };
		String[] avatars = { "https://trello-avatars.s3.amazonaws.com/df93c8f73ee15659408e3ec6a2e3d277/original.png",
				"https://trello-avatars.s3.amazonaws.com/e359bf1b2b2943167e53665054f43a9a/original.png",
				"https://trello-avatars.s3.amazonaws.com/17857e176a3936a15e072f02bbf995ac/original.png",
				"https://trello-avatars.s3.amazonaws.com/c970bdec1f2f00417b7975b1b7a2f21b/original.png",
				"https://trello-avatars.s3.amazonaws.com/f6eeed5381304734fa78d9366232aea7/original.png" };

		try {
			for (int i = 0; i < emails.length; i++) {
				dao.create(new UserEntity(emails[i], appSmartspace, emails[i], avatars[i], UserRole.ADMIN, 50));
			}
			dao.create(new UserEntity("player@gmail.com", appSmartspace, "player",
					"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT2jFuytvPYnjo7SSMVT07soJOHX4B6rn1-F-EETluQD9SS_s0R",
					UserRole.PLAYER, 50));
			dao.create(new UserEntity("manager@gmail.com", appSmartspace, "manager",
					"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRG2vPtKI81KhQFOiB4gzaCL6oGMoEEcEllj8U3dFbjRzRV0m3l",
					UserRole.MANAGER, 50));
			dao.create(new UserEntity("admin@gmail.com", appSmartspace, "admin",
					"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRbbZqYr4sdvAnK9Yx-fWKsHmcpHS-ilyfSdhYgq28nTF-2KEJgJw",
					UserRole.ADMIN, 50));

			// delayed print to show after trace logs
			for (String email : emails) {
				System.err.println("added ADMIN with email: " + email);
			}

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		System.err.println("added PLAYER with email: player@gmail.com");
		System.err.println("added MANAGER with email: manager@gmail.com");
		System.err.println("added ADMIN with email: admin@gmail.com");

		// enable for adding elements on startup
//		 Faker faker = new Faker();F
//		 List<ElementEntity> elements = faker.entity().elementList(10).stream().map(this.elementDao::create).collect(Collectors.toList());
//		 List<ElementEntity> elements2 = faker.entity().elementList(10).stream().peek(en->en.setExpired(true)).map(this.elementDao::create).collect(Collectors.toList());

	}

}
