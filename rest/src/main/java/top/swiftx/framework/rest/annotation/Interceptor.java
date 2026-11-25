package top.swiftx.framework.rest.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 拦截器注解
 *
 * @author 胡永强
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Interceptor {
    /**
     * 加载顺序
     */
    int order() default 0;

    /**
     * 授权目标
     */
    String[] value() default "/**";
}