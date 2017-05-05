package ru.sendto.logger;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;

@InterceptorBinding
@Inherited
@Target({ TYPE, METHOD })
@Retention(RUNTIME)
@Documented
public @interface Log {
	/**
	 * Max lenth of result string
	 * -1 for unlimited
	 * default 32
	 * @return
	 */
	@Nonbinding int maxResultLength() default 32;
	/**
	 * Lenth of param strings
	 * -1 for unlimited
	 * default 16
	 * @return
	 */
	@Nonbinding int maxParamLength() default 16;
	/**
	 * Enable logging time
	 * enabled by default.
	 * @return
	 */
	@Nonbinding boolean logTime() default true;
}
