package top.swiftx.framework.problem;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.web.ErrorResponse;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.swiftx.framework.problem.document.DocumentRegistry;
import top.swiftx.framework.problem.markdown.MarkdownParser;
import top.swiftx.framework.problem.render.PageRenderer;
import top.swiftx.framework.problem.uri.TypeUriResolver;
import top.swiftx.framework.problem.web.DocumentController;
import top.swiftx.framework.problem.web.ErrorResponseInterceptor;

import java.util.List;

/**
 * 问题类型文档核心自动配置。
 */
@AutoConfiguration
@EnableConfigurationProperties(ProblemProperties.class)
@ConditionalOnProperty(
        name = "framework.problem.enabled",
        matchIfMissing = true
)
public class ProblemAutoConfiguration {
    /**
     * 创建问题类型 Markdown 解析器。
     *
     * @return Markdown 解析器
     */
    @Bean
    @ConditionalOnMissingBean
    public MarkdownParser problemMarkdownParser() {
        return new MarkdownParser();
    }

    /**
     * 创建问题类型文档注册表。
     *
     * @param context    应用上下文
     * @param properties 问题类型文档配置
     * @param parser     Markdown 解析器
     * @return 问题类型文档注册表
     */
    @Bean
    @ConditionalOnMissingBean
    public DocumentRegistry problemDocumentRegistry(
            ApplicationContext context,
            ProblemProperties properties,
            MarkdownParser parser
    ) {
        return new DocumentRegistry(
                new PathMatchingResourcePatternResolver(context),
                properties,
                parser
        );
    }

    /**
     * 创建问题类型 URI 解析器。
     *
     * @param properties 问题类型文档配置
     * @return 问题类型 URI 解析器
     */
    @Bean
    @ConditionalOnMissingBean
    public TypeUriResolver problemTypeUriResolver(
            ProblemProperties properties
    ) {
        return new TypeUriResolver(properties);
    }

    /**
     * 创建问题类型页面渲染器。
     *
     * @param uriResolver 问题类型 URI 解析器
     * @return 问题类型页面渲染器
     */
    @Bean
    @ConditionalOnMissingBean
    public PageRenderer problemPageRenderer(
            TypeUriResolver uriResolver
    ) {
        return new PageRenderer(uriResolver);
    }

    /**
     * 问题类型文档 Web 支持。
     */
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnWebApplication
    @ConditionalOnClass(name = "org.springframework.web.bind.annotation.GetMapping")
    static class WebSupport {
        /**
         * 创建问题类型文档控制器。
         *
         * @param registry 文档注册表
         * @param renderer 页面渲染器
         * @return 问题类型文档控制器
         */
        @Bean
        @ConditionalOnMissingBean
        public DocumentController problemDocumentController(
                DocumentRegistry registry,
                PageRenderer renderer
        ) {
            return new DocumentController(
                    registry,
                    renderer
            );
        }

        /**
         * 创建错误响应拦截器。
         *
         * @param registry    文档注册表
         * @param uriResolver URI 解析器
         * @return 错误响应拦截器
         */
        @Bean
        @ConditionalOnMissingBean
        public ErrorResponseInterceptor problemErrorResponseInterceptor(
                DocumentRegistry registry,
                TypeUriResolver uriResolver
        ) {
            return new ErrorResponseInterceptor(registry, uriResolver);
        }
    }

    /**
     * 问题类型文档 MVC 拦截器注册器。
     */
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(WebMvcConfigurer.class)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    static class MvcSupport {
        /**
         * 创建 MVC 错误响应拦截器注册器。
         *
         * @param interceptor 错误响应拦截器
         * @return MVC 配置器
         */
        @Bean
        public WebMvcConfigurer problemMvcConfigurer(
                ErrorResponseInterceptor interceptor
        ) {
            return new MvcInterceptorRegistrar(interceptor);
        }
    }

    /**
     * 问题类型文档 MVC 拦截器注册器。
     */
    private record MvcInterceptorRegistrar(
            ErrorResponseInterceptor interceptor
    ) implements WebMvcConfigurer {
        /**
         * 创建 MVC 拦截器注册器。
         *
         * @param interceptor 错误响应拦截器
         */
        private MvcInterceptorRegistrar {
        }

        /**
         * 注册错误响应拦截器。
         *
         * @param interceptors 错误响应拦截器列表
         */
        @Override
        public void addErrorResponseInterceptors(
                List<ErrorResponse.Interceptor> interceptors
        ) {
            interceptors.add(interceptor);
        }
    }

    /**
     * 问题类型文档 WebFlux 拦截器注册器。
     */
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(WebFluxConfigurer.class)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    static class WebFluxSupport {
        /**
         * 创建 WebFlux 错误响应拦截器注册器。
         *
         * @param interceptor 错误响应拦截器
         * @return WebFlux 配置器
         */
        @Bean
        public WebFluxConfigurer problemWebFluxConfigurer(
                ErrorResponseInterceptor interceptor
        ) {
            return new WebFluxInterceptorRegistrar(interceptor);
        }
    }

    /**
     * 问题类型文档 WebFlux 拦截器注册器。
     */
    private record WebFluxInterceptorRegistrar(
            ErrorResponseInterceptor interceptor
    ) implements WebFluxConfigurer {
        /**
         * 创建 WebFlux 拦截器注册器。
         *
         * @param interceptor 错误响应拦截器
         */
        private WebFluxInterceptorRegistrar {
        }

        /**
         * 注册错误响应拦截器。
         *
         * @param interceptors 错误响应拦截器列表
         */
        @Override
        public void addErrorResponseInterceptors(
                List<ErrorResponse.Interceptor> interceptors
        ) {
            interceptors.add(interceptor);
        }
    }
}
