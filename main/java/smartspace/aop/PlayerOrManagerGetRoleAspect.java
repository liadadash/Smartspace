/**
 * @author liadkh	11-05-2019
 */
package smartspace.aop;

import java.util.List;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import smartspace.dao.EnhancedUserDao;
import smartspace.data.UserEntity;
import smartspace.data.UserKey;
import smartspace.data.UserRole;

/**
 * The Class PlayerOrManagerGetRoleAspect.
 */
@Component
@Aspect
public class PlayerOrManagerGetRoleAspect {

	/** The logger. */
	private Log logger = LogFactory.getLog(PlayerOrManagerGetRoleAspect.class);

	/** The user dao. */
	private EnhancedUserDao<UserKey> userDao;

	/**
	 * Sets the user dao.
	 *
	 * @param userDao the new user dao
	 */
	@Autowired
	public void setUserDao(EnhancedUserDao<UserKey> userDao) {
		this.userDao = userDao;
	}

	/**
	 * Check if user is player or manager.
	 *
	 * @param pjp            the pjp
	 * @param role           the role
	 * @param userSmartspace the user smartspace
	 * @param userEmail      the user email
	 * @return the list
	 * @throws Throwable the throwable
	 */
	@Around("@annotation(smartspace.aop.PlayerOrManagerGetRole) && execution(* smartspace.infra.*Service*Impl.*(..)) && args(role,userSmartspace,userEmail,..)")
	public List<?> checkIfUserIsPlayerOrManager(ProceedingJoinPoint pjp, UserRole role, String userSmartspace,
			String userEmail) throws Throwable {

		String method = pjp.getSignature().getName();
		String fullyQualifiedClassName = pjp.getTarget().getClass().getName();

		String fullMessage = fullyQualifiedClassName + "." + method + "()";

		logger.debug(
				"*** Check if the user " + userSmartspace + "#" + userEmail + " is Player or Manager: " + fullMessage);

		Optional<UserEntity> userOp = this.userDao.readById(new UserKey(userSmartspace, userEmail));
		if (!userOp.isPresent())
			logger.debug("*** The given user " + userSmartspace + "#" + userEmail + " doesn't exist " + fullMessage);

		UserEntity user = userOp.orElseThrow(
				() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The given user doesn't exist"));
		role = user.getRole();

		// check that the user has Player or Manager privileges
		if (role != UserRole.MANAGER && role != UserRole.PLAYER) {
			logger.debug("*** This user isn't player or manager " + userSmartspace + "#" + userEmail + " - "
					+ role.name() + " " + fullMessage + " failed");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Only manager or player are allowed access this resource");
		}
		logger.debug("*** This user " + userSmartspace + "#" + userEmail + " is " + role.name() + " " + fullMessage);

		Object[] args = pjp.getArgs();
		for (int i = 0; i < args.length; i++) {
			if (args[i] == null)
				args[i] = role;
		}
		return (List<?>) pjp.proceed(args);
	}

}
