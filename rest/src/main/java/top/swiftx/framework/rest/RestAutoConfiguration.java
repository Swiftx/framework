package top.swiftx.framework.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.ErrorResponse;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.swiftx.framework.rest.advice.ServletProblemDetailsAdvice;
import top.swiftx.framework.rest.annotation.Interceptor;
import top.swiftx.framework.rest.annotation.Resolver;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


/**
 * REST 框架 MVC 自动配置。
 *
 * @author gpt-5.6-sol
 */
@Slf4j
@AutoConfiguration
@RequiredArgsConstructor
@ConditionalOnClass(WebMvcConfigurer.class)
@EnableConfigurationProperties(RestProperties.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnProperty(
        name = "framework.rest.enabled",
        matchIfMissing = true
)
public class RestAutoConfiguration implements WebMvcConfigurer {
    private final ApplicationContext context;
    private final RestProperties properties;

    /**
     * 注册 MVC（Servlet）全局异常处理器。
     *
     * <p>将所有 {@link org.springframework.web.ErrorResponse} 异常按 RFC 9457
     * 渲染为 {@code application/problem+json}，取代 {@code spring.mvc.problemdetails.enabled}
     * 所提供的内置实现，使 MVC 服务与 WebFlux 网关走统一的问题详情渲染。
     *
     * @return MVC 问题详情异常处理器
     */
    @Bean
    @ConditionalOnMissingBean
    public ServletProblemDetailsAdvice servletProblemDetailsAdvice(
            List<ErrorResponse.Interceptor> interceptors
    ) {
        log.info("load problem details advice: servlet");
        return new ServletProblemDetailsAdvice(interceptors);
    }

    /**
     * 注册拦截器
     *
     * @param registry 注册器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        Map<String, Object> beans = context
                .getBeansWithAnnotation(Interceptor.class);
        var list = new ArrayList<InterceptorRegister>();

        // 查找拦截器
        for (Object bean : beans.values()) {
            if (!(bean instanceof HandlerInterceptor)) {
                continue;
            }
            var interceptor = bean.getClass()
                    .getAnnotation(Interceptor.class);
            if (interceptor == null) continue;
            list.add(new InterceptorRegister(
                    interceptor.order(),
                    interceptor.value(),
                    (HandlerInterceptor) bean
            ));
        }

        // 状态拦截器
        list.stream().sorted(
                Comparator.comparing(InterceptorRegister::getOrder)
        ).forEachOrdered(item -> {
            var handler = item.getHandler();
            log.info("load interceptor: {}", handler.getClass());
            registry.addInterceptor(item.getHandler())
                    .addPathPatterns(item.getPath());
        });
    }

    /**
     * 注册自定义注解
     *
     * @param resolvers 解析器
     */
    @Override
    public void addArgumentResolvers(
            List<HandlerMethodArgumentResolver> resolvers
    ) {
        var beans = context
                .getBeansWithAnnotation(Resolver.class);
        for (Object bean : beans.values()) {
            if (!(bean instanceof HandlerMethodArgumentResolver)) {
                continue;
            }
            Resolver resolver = bean
                    .getClass()
                    .getAnnotation(Resolver.class);
            if (resolver == null) {
                continue;
            }
            resolvers.add((HandlerMethodArgumentResolver) bean);
            log.info("load resolver:{}", bean.getClass());
        }
    }

    /**
     * CORS全局设置
     *
     * @param registry 注册器
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        var cors = properties.getCors();
        if (cors == null) return;
        for (var item : cors.entrySet()) {
            var key = item.getKey();
            var value = item.getValue();
            if (!value.isEnabled()) continue;
            registry.addMapping(key)
                    .allowedMethods(value.getAllowedMethods())
                    .allowedOrigins(value.getAllowedOrigins())
                    .allowedHeaders(value.getAllowedHeaders());
        }
    }
}
