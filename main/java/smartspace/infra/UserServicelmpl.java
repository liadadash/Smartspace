package smartspace.infra;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import smartspace.aop.AdminOnly;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.UserEntity;
import smartspace.data.UserKey;

@Service
public class UserServicelmpl implements UserService {

	private EnhancedUserDao<UserKey> userDao;
	@Value("${smartspace.name}")
	private String appSmartspace;

	@Autowired
	public UserServicelmpl(EnhancedUserDao<UserKey> userDao) {
		this.userDao = userDao;
	}

	@Override
	@Transactional
	@AdminOnly
	public List<UserEntity> importUsers(String adminSmartspace, String adminEmail, List<UserEntity> entities) {
		return entities.stream().map(this::validate).map(this.userDao::importUser).collect(Collectors.toList());
	}

	@Override
	@AdminOnly
	public List<UserEntity> getUsingPagination(String adminSmartspace, String adminEmail, int size, int page) {
		return this.userDao.readAllWithPaging("key", size, page);
	}
	
	private UserEntity validate(UserEntity user) {
		if (!isValid(user)) {
			throw new RuntimeException("one or more of the given users are invalid");
		}
		
		return user;
	}

	private boolean isValid(UserEntity user) {
		return 	user.getUserSmartspace()!= null && !user.getUserSmartspace().equals(appSmartspace) 
				&& user.getUsername() != null && !user.getUsername().trim().isEmpty() 
				&& user.getAvatar() != null && !user.getAvatar().trim().isEmpty()
				&& user.getRole() != null && user.getKey() != null
				&& user.getUserEmail() != null && !user.getUserEmail().trim().isEmpty()
				&& user.getUserSmartspace() != null && !user.getUserSmartspace().isEmpty()
				&& user.getKey().getUserEmail() != null && !user.getKey().getUserEmail().isEmpty()
				&& user.getKey().getUserSmartspace() != null && !user.getKey().getUserSmartspace().isEmpty();
	}
}
