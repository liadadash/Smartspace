package smartspace.infra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import smartspace.dao.EnhancedUserDao;
import smartspace.data.UserEntity;
import smartspace.data.UserKey;

@Service
public class UpdateUserServiceImpl implements UpdateUserService {

	private EnhancedUserDao<UserKey> userDao;

	@Autowired
	public UpdateUserServiceImpl(EnhancedUserDao<UserKey> userDao) {
		this.userDao = userDao;
	}

	// User can update only: Avatar, Username, Role
	@Override
	public void updateUser(String userEmail, String userSmartspace, UserEntity userEntity) {
		this.userDao.update(validate(userEntity));
	}

	private UserEntity validate(UserEntity user) {
		if (user.getUsername() != null && user.getUsername().trim().isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username must not be empty");
		}
		if (user.getAvatar() != null && user.getAvatar().trim().isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Avatar must not be empty");
		}
		return user;
	}

}
