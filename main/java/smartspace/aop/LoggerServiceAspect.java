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

/**
 * The Class LoggerServiceAspect.
 * 
 * @author liadkh
 */
@Component
@Aspect
public class LoggerServiceAspect {

	/** The logger. */
	private Log logger = LogFactory.getLog(LoggerServiceAspect.class);

	/**
	 * Import data.
	 *
	 * @param pjp             the pjp
	 * @param adminSmartspace the admin smartspace
	 * @param adminEmail      the admin email
	 * @return the list
	 * @throws Throwable the throwable
	 */
	@Around("execution(* smartspace.infra.*Servicelmpl.import*(..)) && args(adminSmartspace,adminEmail,..)")
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
			logger.debug("*** Import data" + (success ? "success" : "failed") + " " + fullMessage);
		}
	}

	/**
	 * Gets the data.
	 *
	 * @param pjp             the pjp
	 * @param adminSmartspace the admin smartspace
	 * @param adminEmail      the admin email
	 * @return the data
	 * @throws Throwable the throwable
	 */
	@Around("execution(* smartspace.infra.*Servicelmpl.get*(..)) && args(adminSmartspace,adminEmail,..)")
	public List<?> getData(ProceedingJoinPoint pjp, String adminSmartspace, String adminEmail) throws Throwable {

		String method = pjp.getSignature().getName();
		String fullyQualifiedClassName = pjp.getTarget().getClass().getName();

		String fullMessage = fullyQualifiedClassName + "." + method + "()" + " by: " + adminSmartspace + "#"
				+ adminEmail;

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
			logger.debug("*** Get data" + (success ? "success" : "failed") + " " + fullMessage);
		}
	}
}
