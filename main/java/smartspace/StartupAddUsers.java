package smartspace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import smartspace.dao.UserDao;

import smartspace.data.UserEntity;
import smartspace.data.UserKey;
import smartspace.data.UserRole;

@Component
@Profile("!test")
public class StartupAddUsers implements CommandLineRunner {
	private UserDao<UserKey> dao;

	@Value("${smartspace.name}")
	private String appSmartspace;

	public StartupAddUsers() {
	}

	@Autowired
	public StartupAddUsers(UserDao<UserKey> dao) {
		this.dao = dao;
	}

	@Override
	public void run(String... args) throws Exception {
		String[] emails = { "bscpkd@gmail.com", "nofaruliel@gmail.com", "liadkh95@gmail.com", "Aviel2845@gmail.com",
				"Amitpelerman@gmail.com" };

		for (String email : emails) {
			dao.create(new UserEntity(email, appSmartspace, email, "image.png", UserRole.ADMIN, 50));
		}

		// delayed print to show after trace logs
		for (String email : emails) {
			System.err.println("added admin with email: " + email);
		}
	}

}
