package smartspace.aop;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * The Interface LoggerService.
 * 
 * @author liadkh
 */
@Retention(RUNTIME)
@Target(ElementType.METHOD)
public @interface LoggerService {

}
