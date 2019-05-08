package smartspace.infra;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sun.istack.internal.NotNull;

import smartspace.aop.AdminOnly;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.UserEntity;
import smartspace.data.UserKey;

@Service
public class UserServicelmpl implements UserService {

	Log logger = LogFactory.getLog(UserServicelmpl.class);

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
	public List<UserEntity> importUsers(String adminSmartspace, String adminEmail, @NotNull List<UserEntity> entities) {
		logger.info("Try to import " + entities.size() + " users by: " + adminSmartspace + "#" + adminEmail);
		return entities.stream().map(this::validate).map(this.userDao::importUser).collect(Collectors.toList());
	}

	@Override
	@AdminOnly
	public List<UserEntity> getUsingPagination(String adminSmartspace, String adminEmail, int size, int page) {
		logger.info("Get user using pagination by: " + adminSmartspace + "#" + adminEmail + " size: " + size
				+ ", page: " + page);
		return this.userDao.readAllWithPaging("key", size, page);
	}

	private UserEntity validate(UserEntity user) {
		logger.debug("Check if user is valid");
		if (!isValid(user)) {
			logger.debug("User isn't valid");
			throw new RuntimeException("one or more of the given users are invalid");
		}
		logger.debug("User is valid");
		return user;
	}

	private boolean isValid(UserEntity user) {
		return user.getUserSmartspace() != null && !user.getUserSmartspace().equals(appSmartspace)
				&& user.getUsername() != null && !user.getUsername().trim().isEmpty() && user.getAvatar() != null
				&& !user.getAvatar().trim().isEmpty() && user.getRole() != null && user.getKey() != null
				&& user.getUserEmail() != null && !user.getUserEmail().trim().isEmpty()
				&& user.getUserSmartspace() != null && !user.getUserSmartspace().isEmpty()
				&& user.getKey().getUserEmail() != null && !user.getKey().getUserEmail().isEmpty()
				&& user.getKey().getUserSmartspace() != null && !user.getKey().getUserSmartspace().isEmpty();
	}
}
