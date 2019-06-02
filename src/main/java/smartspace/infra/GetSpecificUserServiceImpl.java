package smartspace.infra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import smartspace.dao.EnhancedUserDao;
import smartspace.data.UserEntity;
import smartspace.data.UserKey;

@Service
// @LoggerService
public class GetSpecificUserServiceImpl implements GetSpecificUserService {

	private EnhancedUserDao<UserKey> userDao;

	@Autowired
	public GetSpecificUserServiceImpl(EnhancedUserDao<UserKey> UserDao) {
		this.userDao = UserDao;
	}

	@Override
	public UserEntity getUser(String userSmartspace, String userEmail) {
		// TODO Auto-generated method stub
		return this.userDao.readById(new UserKey(userSmartspace, userEmail))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
	}

}