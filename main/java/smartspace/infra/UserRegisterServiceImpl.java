package smartspace.infra;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import smartspace.dao.EnhancedUserDao;
import smartspace.data.UserEntity;
import smartspace.data.UserKey;

@Service
public class UserRegisterServiceImpl implements UserRegisterService {

	private EnhancedUserDao<UserKey> userDao;
	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

	@Autowired
	public UserRegisterServiceImpl(EnhancedUserDao<UserKey> userDao) {
		this.userDao = userDao;
	}

	@Override
	public UserEntity registerNewUser(UserEntity newUser) {
		return this.userDao.create(validate(newUser)); // create() will throw exception if user already exists
	}

	private UserEntity validate(UserEntity user) {
		if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username must not be empty");
		}
		if (user.getAvatar() == null || user.getAvatar().trim().isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Avatar must not be empty");
		}
		if (user.getUserEmail() == null || user.getUserEmail().trim().isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email must not be empty");
		}
		if (!VALID_EMAIL_ADDRESS_REGEX.matcher(user.getUserEmail()).find()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email"); 
		}
		if (user.getRole() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Must have a valid user role");
		}

		return user;
	}
}
