package top.swiftx.framework.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.ErrorResponse;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import top.swiftx.framework.rest.advice.ReactiveProblemDetailsAdvice;

import java.util.List;

/**
 * REST 框架 WebFlux（Reactive）自动配置。
 *
 * <p>为网关等响应式应用注册 RFC 9457 全局异常处理器，使网关抛出的
 * {@link org.springframework.web.ErrorResponse} 异常按问题详情规范响应，
 * 与 MVC 服务统一。
 *
 * @author 胡永强
 */
@Slf4j
@AutoConfiguration
@ConditionalOnClass(WebFluxConfigurer.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@ConditionalOnProperty(
        name = "framework.rest.enabled",
        matchIfMissing = true
)
public class ReactiveRestAutoConfiguration {
    /**
     * 注册 WebFlux（Reactive）全局异常处理器。
     *
     * @return WebFlux 问题详情异常处理器
     */
    @Bean
    @ConditionalOnMissingBean
    public ReactiveProblemDetailsAdvice reactiveProblemDetailsAdvice(
            List<ErrorResponse.Interceptor> interceptors
    ) {
        log.info("load problem details advice: reactive");
        return new ReactiveProblemDetailsAdvice(interceptors);
    }
}
