package smartspace.aop;

import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import smartspace.dao.EnhancedUserDao;
import smartspace.data.UserEntity;
import smartspace.data.UserKey;
import smartspace.data.UserRole;

@Component
@Aspect
public class AdminOnlyAspect {
	
	private EnhancedUserDao<UserKey> userDao;
	private Log logger = LogFactory.getLog(AdminOnlyAspect.class);
	
	@Autowired
	public void setUserDao(EnhancedUserDao<UserKey> userDao) {
		this.userDao = userDao;
	}

	@Before("@annotation(smartspace.aop.AdminOnly) && args(smartspace, email, ..)")
	public void checkUserIsAdmin(JoinPoint jp, String smartspace, String email) {
		String method = jp.getSignature().getName();
		String fullyQualifiedClassName = jp.getTarget().getClass().getName();
		logger.debug(fullyQualifiedClassName + "." + method + "() - " + " using AdminOnly check");
				
		this.userDao.readById(new UserKey(smartspace, email)).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The given user doesn't exist"));
		
		// check that the user has ADMIN privileges 
		if (!userDao.userIsAdmin(new UserKey(smartspace, email))) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Only admins are allowed access this resource");
		}
	}
	
	public boolean userIsAdmin(UserKey userKey) {
		Optional<UserEntity> userData = this.userDao.readById(userKey);
		if (userData.isPresent()) {
			return (userData.get().getRole() == UserRole.ADMIN);
		}
		
		return false;
	}
}
