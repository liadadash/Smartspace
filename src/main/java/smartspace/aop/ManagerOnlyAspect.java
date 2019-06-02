package smartspace.aop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import smartspace.dao.EnhancedUserDao;
import smartspace.data.UserEntity;
import smartspace.data.UserKey;
import smartspace.data.UserRole;

@Component
@Aspect
public class ManagerOnlyAspect {
	
	
	private EnhancedUserDao<UserKey> userDao;
	private Log logger = LogFactory.getLog(AdminOnlyAspect.class);

	@Autowired
	public void setUserDao(EnhancedUserDao<UserKey> userDao) {
		this.userDao = userDao;
	}
	
	@Before("@annotation(smartspace.aop.ManagerOnly) && args(smartspace, email, ..)")
	public void checkUserIsManager(JoinPoint jp, String smartspace, String email) {
		String method = jp.getSignature().getName();
		String fullyQualifiedClassName = jp.getTarget().getClass().getName();
		logger.debug(fullyQualifiedClassName + "." + method + "() - " + " using ManagerOnly check");
				
		UserEntity user = this.userDao.readById(new UserKey(smartspace, email))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The given user doesn't exist"));

		if (!(user.getRole() == UserRole.MANAGER)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User must be a manager to access this resource");
		}
	}

}
