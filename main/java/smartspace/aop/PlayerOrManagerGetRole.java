/**
 * @author liadkh	11-05-2019
 */
package smartspace.aop;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * The Interface PlayerOrManagerGetRole.
 * 
 * @author liadkh
 */
@Retention(RUNTIME)
@Target(ElementType.METHOD)
public @interface PlayerOrManagerGetRole {

}
