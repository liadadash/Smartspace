package smartspace.aop;

/**
 * @author liadkh	08-05-2019
 */

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import smartspace.data.UserRole;

/**
 * The Class LoggerServiceAspect.
 */
@Component
@Aspect
public class LoggerServiceAspect {

	/** The logger. */
	private Log logger = LogFactory.getLog(LoggerServiceAspect.class);

	/**
	 * Import data.
	 *
	 * @param pjp the pjp
	 * @param adminSmartspace the admin smartspace
	 * @param adminEmail the admin email
	 * @return the list
	 * @throws Throwable the throwable
	 */
	@Around("execution(* smartspace.infra.*ServiceImpl.import*(..)) && args(adminSmartspace,adminEmail,..)")
	public List<?> importData(ProceedingJoinPoint pjp, String adminSmartspace, String adminEmail) throws Throwable {

		String method = pjp.getSignature().getName();
		String fullyQualifiedClassName = pjp.getTarget().getClass().getName();

		String fullMessage = fullyQualifiedClassName + "." + method + "()" + " by: " + adminSmartspace + "#"
				+ adminEmail;

		logger.debug("*** Try to import data: " + fullMessage);

		List<?> rv;
		boolean success = true;
		try {
			rv = (List<?>) pjp.proceed();
			return rv;
		} catch (Exception e) {
			success = false;
			throw e;
		} finally {
			logger.debug("*** Import data " + (success ? "success" : "failed") + " " + fullMessage);
		}
	}

	/**
	 * Export data.
	 *
	 * @param pjp the pjp
	 * @param adminSmartspace the admin smartspace
	 * @param adminEmail the admin email
	 * @return the list
	 * @throws Throwable the throwable
	 */
	@Around("execution(* smartspace.infra.*ServiceImpl.get*(..)) && args(adminSmartspace,adminEmail,..)")
	public List<?> exportData(ProceedingJoinPoint pjp, String adminSmartspace, String adminEmail) throws Throwable {

		String method = pjp.getSignature().getName();
		String fullyQualifiedClassName = pjp.getTarget().getClass().getName();

		String fullMessage = fullyQualifiedClassName + "." + method + "()" + " by: " + adminSmartspace + "#"
				+ adminEmail;

		logger.debug("*** Try to export data: " + fullMessage);

		List<?> rv;
		boolean success = true;
		try {
			rv = (List<?>) pjp.proceed();
			return rv;
		} catch (Exception e) {
			success = false;
			throw e;
		} finally {
			logger.debug("*** Export data " + (success ? "success" : "failed") + " " + fullMessage);
		}
	}

	/**
	 * Gets the data.
	 *
	 * @param pjp the pjp
	 * @param role the role
	 * @return the data
	 * @throws Throwable the throwable
	 */
	@Around("execution(* smartspace.infra.*Service*Impl.get*(..)) && args(role,..)")
	public List<?> getData(ProceedingJoinPoint pjp, UserRole role) throws Throwable {

		String method = pjp.getSignature().getName();
		String fullyQualifiedClassName = pjp.getTarget().getClass().getName();

		String fullMessage = fullyQualifiedClassName + "." + method + "()" + " by: "
				+ ((role == null) ? null : role.name());

		logger.debug("*** Try to get data: " + fullMessage);

		List<?> rv;
		boolean success = true;
		try {
			rv = (List<?>) pjp.proceed();
			return rv;
		} catch (Exception e) {
			success = false;
			throw e;
		} finally {
			logger.debug("*** Get data " + (success ? "success" : "failed") + " " + fullMessage);
		}
	}

	/**
	 * Register.
	 *
	 * @param pjp the pjp
	 * @return the list
	 * @throws Throwable the throwable
	 */
	@Around("execution(* smartspace.infra.*Register*Service*Impl.register*(..))")
	public Object register(ProceedingJoinPoint pjp) throws Throwable {

		String method = pjp.getSignature().getName();
		String fullyQualifiedClassName = pjp.getTarget().getClass().getName();

		String fullMessage = fullyQualifiedClassName + "." + method + "()";

		logger.debug("*** Try to register: " + fullMessage);

		Object rv;
		boolean success = true;
		try {
			rv = pjp.proceed();
			return rv;
		} catch (Exception e) {
			success = false;
			throw e;
		} finally {
			logger.debug("*** Register " + (success ? "success" : "failed") + " " + fullMessage);
		}
	}
}
