package smartspace.aop;

/**
 * @author liadkh	08-05-2019
 */

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * The Interface LoggerService.
 * @author liadkh
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface LoggerService {

}
