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
public class ManagerOrPlayerOnlyAspect {

	private EnhancedUserDao<UserKey> userDao;
	private Log logger = LogFactory.getLog(AdminOnlyAspect.class);

	@Autowired
	public void setUserDao(EnhancedUserDao<UserKey> userDao) {
		this.userDao = userDao;
	}

	@Before("@annotation(smartspace.aop.ManagerOrPlayerOnly) && args(smartspace, email, ..)")
	public void checkUserIsPlayerOrMangerOnly(JoinPoint jp, String smartspace, String email) {
		String method = jp.getSignature().getName();
		String fullyQualifiedClassName = jp.getTarget().getClass().getName();
		logger.debug(fullyQualifiedClassName + "." + method + "() - " + " using AdminOnly check");

		UserEntity user = this.userDao.readById(new UserKey(smartspace, email))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User Not in DB"));

		if (user.getRole() != UserRole.MANAGER && user.getRole() != UserRole.PLAYER) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User must be a manager or a player to access this resource");
		}

	}

}
