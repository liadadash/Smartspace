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

	// User can update only: Avatar, Username,Role
	@Override
	public void updateUser(String userEmail, String userSmartspace, UserEntity userEntity) {
		if (userSmartspace != userEntity.getUserSmartspace() || userEmail != userEntity.getUserEmail()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"User cannot update his userSmartspace or his email");
		}
		this.userDao.update(userEntity); // if the userEntity not in DB userDao.update() throw runTimeExepcion
	}
}
