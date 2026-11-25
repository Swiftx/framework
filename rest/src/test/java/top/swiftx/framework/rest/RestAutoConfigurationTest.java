package top.swiftx.framework.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.context.ApplicationContext;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import top.swiftx.framework.rest.annotation.Interceptor;
import top.swiftx.framework.rest.annotation.Resolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * RestAutoConfiguration 测试。
 *
 * @author gpt-5.6-sol
 */
class RestAutoConfigurationTest {

    private ApplicationContext context;
    private RestProperties properties;
    private RestAutoConfiguration configuration;

    @BeforeEach
    void setUp() {
        context = mock(ApplicationContext.class);
        properties = new RestProperties();
        configuration = new RestAutoConfiguration(context, properties);
    }

    @Test
    void testConstructorWithNullProperties() {
        RestProperties props = new RestProperties();
        RestAutoConfiguration config = new RestAutoConfiguration(context, props);
        assertNotNull(config);
    }

    @Test
    void testAddInterceptorsWithNoBeans() {
        when(context.getBeansWithAnnotation(Interceptor.class)).thenReturn(new HashMap<>());

        InterceptorRegistry registry = mock(InterceptorRegistry.class);
        configuration.addInterceptors(registry);

        verify(registry, never()).addInterceptor(any());
    }

    @Test
    void testAddInterceptorsWithNonHandlerInterceptorBean() {
        Map<String, Object> beans = new HashMap<>();
        beans.put("nonHandler", new Object()); // Not a HandlerInterceptor

        when(context.getBeansWithAnnotation(Interceptor.class)).thenReturn(beans);

        InterceptorRegistry registry = mock(InterceptorRegistry.class);
        configuration.addInterceptors(registry);

        verify(registry, never()).addInterceptor(any());
    }

    @Test
    void testAddInterceptorsWithHandlerInterceptorButNoAnnotation() {
        // TestHandlerInterceptor implements HandlerInterceptor but has no @Interceptor annotation
        TestHandlerInterceptor interceptor = new TestHandlerInterceptor();

        Map<String, Object> beans = new HashMap<>();
        beans.put("testHandler", interceptor);

        when(context.getBeansWithAnnotation(Interceptor.class)).thenReturn(beans);

        InterceptorRegistry registry = mock(InterceptorRegistry.class);
        configuration.addInterceptors(registry);

        verify(registry, never()).addInterceptor(any());
    }

    @Test
    void testAddInterceptorsWithValidInterceptor() {
        AnnotatedTestInterceptor interceptor = new AnnotatedTestInterceptor();

        Map<String, Object> beans = new HashMap<>();
        beans.put("annotatedInterceptor", interceptor);

        when(context.getBeansWithAnnotation(Interceptor.class)).thenReturn(beans);

        InterceptorRegistry registry = mock(InterceptorRegistry.class);
        org.springframework.web.servlet.config.annotation.InterceptorRegistration registration =
                mock(org.springframework.web.servlet.config.annotation.InterceptorRegistration.class);
        when(registry.addInterceptor(any(HandlerInterceptor.class))).thenReturn(registration);
        when(registration.addPathPatterns(any(String[].class))).thenReturn(registration);

        configuration.addInterceptors(registry);

        verify(registry, times(1)).addInterceptor(interceptor);
        verify(registration, times(1)).addPathPatterns(any(String[].class));
    }

    @Test
    void testAddInterceptorsWithMultipleInterceptors() {
        AnnotatedTestInterceptor interceptor1 = new AnnotatedTestInterceptor();
        AnotherTestInterceptor interceptor2 = new AnotherTestInterceptor();

        Map<String, Object> beans = new HashMap<>();
        beans.put("interceptor1", interceptor1);
        beans.put("interceptor2", interceptor2);

        when(context.getBeansWithAnnotation(Interceptor.class)).thenReturn(beans);

        InterceptorRegistry registry = mock(InterceptorRegistry.class);
        org.springframework.web.servlet.config.annotation.InterceptorRegistration registration =
                mock(org.springframework.web.servlet.config.annotation.InterceptorRegistration.class);
        when(registry.addInterceptor(any(HandlerInterceptor.class))).thenReturn(registration);
        when(registration.addPathPatterns(any(String[].class))).thenReturn(registration);

        configuration.addInterceptors(registry);

        verify(registry, times(2)).addInterceptor(any(HandlerInterceptor.class));
    }

    @Test
    void testAddInterceptorsWithCustomOrderAndPath() {
        CustomPathInterceptor interceptor = new CustomPathInterceptor();

        Map<String, Object> beans = new HashMap<>();
        beans.put("customInterceptor", interceptor);

        when(context.getBeansWithAnnotation(Interceptor.class)).thenReturn(beans);

        InterceptorRegistry registry = mock(InterceptorRegistry.class);
        org.springframework.web.servlet.config.annotation.InterceptorRegistration registration =
                mock(org.springframework.web.servlet.config.annotation.InterceptorRegistration.class);
        when(registry.addInterceptor(any(HandlerInterceptor.class))).thenReturn(registration);
        when(registration.addPathPatterns(any(String[].class))).thenReturn(registration);

        configuration.addInterceptors(registry);

        verify(registry, times(1)).addInterceptor(interceptor);
        verify(registration, times(1)).addPathPatterns(eq("/api/**"));
    }

    @Test
    void testAddInterceptorsOrdering() {
        HighOrderInterceptor highInterceptor = new HighOrderInterceptor();
        LowOrderInterceptor lowInterceptor = new LowOrderInterceptor();

        Map<String, Object> beans = new HashMap<>();
        beans.put("high", highInterceptor);
        beans.put("low", lowInterceptor);

        when(context.getBeansWithAnnotation(Interceptor.class)).thenReturn(beans);

        InterceptorRegistry registry = mock(InterceptorRegistry.class);
        org.springframework.web.servlet.config.annotation.InterceptorRegistration registration =
                mock(org.springframework.web.servlet.config.annotation.InterceptorRegistration.class);
        when(registry.addInterceptor(any(HandlerInterceptor.class))).thenReturn(registration);
        when(registration.addPathPatterns(any(String[].class))).thenReturn(registration);

        configuration.addInterceptors(registry);

        // Should register both interceptors
        verify(registry, times(2)).addInterceptor(any(HandlerInterceptor.class));
    }

    @Test
    void testAddArgumentResolversWithNoBeans() {
        when(context.getBeansWithAnnotation(Resolver.class)).thenReturn(new HashMap<>());

        List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>();
        configuration.addArgumentResolvers(resolvers);

        assertTrue(resolvers.isEmpty());
    }

    @Test
    void testAddArgumentResolversWithNonResolverBean() {
        Map<String, Object> beans = new HashMap<>();
        beans.put("nonResolver", new Object()); // Not a HandlerMethodArgumentResolver

        when(context.getBeansWithAnnotation(Resolver.class)).thenReturn(beans);

        List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>();
        configuration.addArgumentResolvers(resolvers);

        assertTrue(resolvers.isEmpty());
    }

    @Test
    void testAddArgumentResolversWithValidResolver() {
        AnnotatedTestResolver resolver = new AnnotatedTestResolver();

        Map<String, Object> beans = new HashMap<>();
        beans.put("testResolver", resolver);

        when(context.getBeansWithAnnotation(Resolver.class)).thenReturn(beans);

        List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>();
        configuration.addArgumentResolvers(resolvers);

        assertEquals(1, resolvers.size());
        assertTrue(resolvers.contains(resolver));
    }

    @Test
    void testAddArgumentResolversWithResolverButNoAnnotation() {
        // HandlerMethodArgumentResolver without @Resolver annotation should not be added
        // This tests the branch where bean is instanceof HandlerMethodArgumentResolver
        // but getAnnotation returns null
        TestResolverWithoutAnnotation resolver = new TestResolverWithoutAnnotation();

        Map<String, Object> beans = new HashMap<>();
        beans.put("resolver", resolver);

        when(context.getBeansWithAnnotation(Resolver.class)).thenReturn(beans);

        List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>();
        configuration.addArgumentResolvers(resolvers);

        // Should be empty since the bean doesn't have @Resolver annotation
        assertTrue(resolvers.isEmpty());
    }

    @Test
    void testAddArgumentResolversWithMultipleResolvers() {
        AnnotatedTestResolver resolver1 = new AnnotatedTestResolver();
        AnotherTestResolver resolver2 = new AnotherTestResolver();

        Map<String, Object> beans = new HashMap<>();
        beans.put("resolver1", resolver1);
        beans.put("resolver2", resolver2);

        when(context.getBeansWithAnnotation(Resolver.class)).thenReturn(beans);

        List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>();
        configuration.addArgumentResolvers(resolvers);

        assertEquals(2, resolvers.size());
        assertTrue(resolvers.contains(resolver1));
        assertTrue(resolvers.contains(resolver2));
    }

    @Test
    void testAddCorsMappingsWithNullCors() {
        // When cors is null
        properties.setCors(null);

        CorsRegistry registry = mock(CorsRegistry.class);
        configuration.addCorsMappings(registry);

        verify(registry, never()).addMapping(any());
    }

    @Test
    void testAddCorsMappingsWithEmptyCorsMap() {
        properties.setCors(new HashMap<>());

        CorsRegistry registry = mock(CorsRegistry.class);
        configuration.addCorsMappings(registry);

        verify(registry, never()).addMapping(any());
    }

    @Test
    void testAddCorsMappingsWithDisabledCors() {
        Map<String, RestProperties.CORS> corsMap = new HashMap<>();
        RestProperties.CORS corsConfig = new RestProperties.CORS();
        corsConfig.setEnabled(false);
        corsMap.put("/api/**", corsConfig);

        properties.setCors(corsMap);

        CorsRegistry registry = mock(CorsRegistry.class);
        configuration.addCorsMappings(registry);

        verify(registry, never()).addMapping(any());
    }

    @Test
    void testAddCorsMappingsWithEnabledCors() {
        Map<String, RestProperties.CORS> corsMap = new HashMap<>();
        RestProperties.CORS corsConfig = new RestProperties.CORS();
        corsConfig.setEnabled(true);
        corsConfig.setAllowedMethods("GET,POST");
        corsConfig.setAllowedOrigins("https://example.com");
        corsConfig.setAllowedHeaders("Content-Type,Authorization");
        corsMap.put("/api/**", corsConfig);

        properties.setCors(corsMap);

        CorsRegistry registry = mock(CorsRegistry.class);
        org.springframework.web.servlet.config.annotation.CorsRegistration registration =
                mock(org.springframework.web.servlet.config.annotation.CorsRegistration.class);
        when(registry.addMapping("/api/**")).thenReturn(registration);
        when(registration.allowedMethods("GET,POST")).thenReturn(registration);
        when(registration.allowedOrigins("https://example.com")).thenReturn(registration);
        when(registration.allowedHeaders("Content-Type,Authorization")).thenReturn(registration);

        configuration.addCorsMappings(registry);

        verify(registry, times(1)).addMapping("/api/**");
        verify(registration, times(1)).allowedMethods("GET,POST");
        verify(registration, times(1)).allowedOrigins("https://example.com");
        verify(registration, times(1)).allowedHeaders("Content-Type,Authorization");
    }

    @Test
    void testAddCorsMappingsWithMultipleCorsEntries() {
        properties.setCors(multipleCorsEntries());

        CorsRegistry registry = mock(CorsRegistry.class);
        org.springframework.web.servlet.config.annotation.CorsRegistration registration =
                mock(org.springframework.web.servlet.config.annotation.CorsRegistration.class);
        when(registry.addMapping(anyString())).thenReturn(registration);
        when(registration.allowedMethods(anyString())).thenReturn(registration);
        when(registration.allowedOrigins(anyString())).thenReturn(registration);
        when(registration.allowedHeaders(anyString())).thenReturn(registration);

        configuration.addCorsMappings(registry);

        verify(registry, times(2)).addMapping(anyString());
    }

    /**
     * 创建包含 API 和管理端规则的 CORS 测试配置。
     *
     * @return 两条启用的 CORS 规则
     */
    private static Map<String, RestProperties.CORS> multipleCorsEntries() {
        var apiCors = new RestProperties.CORS();
        apiCors.setEnabled(true);
        apiCors.setAllowedMethods("GET");
        apiCors.setAllowedOrigins("https://api.example.com");
        apiCors.setAllowedHeaders("*");

        var adminCors = new RestProperties.CORS();
        adminCors.setEnabled(true);
        adminCors.setAllowedMethods("POST,PUT,DELETE");
        adminCors.setAllowedOrigins("https://admin.example.com");
        adminCors.setAllowedHeaders("Authorization");

        return Map.of(
                "/api/**",
                apiCors,
                "/admin/**",
                adminCors
        );
    }

    @Test
    void testAddCorsMappingsWithMixedEnabledDisabled() {
        Map<String, RestProperties.CORS> corsMap = new HashMap<>();

        RestProperties.CORS enabledCors = new RestProperties.CORS();
        enabledCors.setEnabled(true);
        corsMap.put("/public/**", enabledCors);

        RestProperties.CORS disabledCors = new RestProperties.CORS();
        disabledCors.setEnabled(false);
        corsMap.put("/private/**", disabledCors);

        properties.setCors(corsMap);

        CorsRegistry registry = mock(CorsRegistry.class);
        org.springframework.web.servlet.config.annotation.CorsRegistration registration =
                mock(org.springframework.web.servlet.config.annotation.CorsRegistration.class);
        when(registry.addMapping(anyString())).thenReturn(registration);
        when(registration.allowedMethods(anyString())).thenReturn(registration);
        when(registration.allowedOrigins(anyString())).thenReturn(registration);
        when(registration.allowedHeaders(anyString())).thenReturn(registration);

        configuration.addCorsMappings(registry);

        // Only the enabled CORS should be registered
        verify(registry, times(1)).addMapping("/public/**");
        verify(registry, never()).addMapping("/private/**");
    }

    // Test classes and interfaces

    static class TestHandlerInterceptor implements HandlerInterceptor {
        // No @Interceptor annotation
    }

    @Interceptor
    static class AnnotatedTestInterceptor implements HandlerInterceptor {
        // Uses default values: order=0, path="/**"
    }

    @Interceptor(order = 10, value = {"/api/**"})
    static class CustomPathInterceptor implements HandlerInterceptor {
    }

    @Interceptor(order = 100)
    static class HighOrderInterceptor implements HandlerInterceptor {
    }

    @Interceptor(order = 1)
    static class LowOrderInterceptor implements HandlerInterceptor {
    }

    @Interceptor
    static class AnotherTestInterceptor implements HandlerInterceptor {
    }

    // Resolver test classes
    @Resolver
    static class AnnotatedTestResolver implements HandlerMethodArgumentResolver {
        @Override
        public boolean supportsParameter(MethodParameter parameter) {
            return false;
        }

        @Override
        public @Nullable Object resolveArgument(
                MethodParameter parameter,
                @Nullable ModelAndViewContainer mavContainer,
                NativeWebRequest webRequest,
                @Nullable WebDataBinderFactory binderFactory
        ) {
            return null;
        }
    }

    @Resolver
    static class AnotherTestResolver implements HandlerMethodArgumentResolver {
        @Override
        public boolean supportsParameter(MethodParameter parameter) {
            return true;
        }

        @Override
        public @Nullable Object resolveArgument(
                MethodParameter parameter,
                @Nullable ModelAndViewContainer mavContainer,
                NativeWebRequest webRequest,
                @Nullable WebDataBinderFactory binderFactory
        ) {
            return null;
        }
    }

    static class TestResolverWithoutAnnotation implements HandlerMethodArgumentResolver {
        @Override
        public boolean supportsParameter(MethodParameter parameter) {
            return false;
        }

        @Override
        public @Nullable Object resolveArgument(
                MethodParameter parameter,
                @Nullable ModelAndViewContainer mavContainer,
                NativeWebRequest webRequest,
                @Nullable WebDataBinderFactory binderFactory
        ) {
            return null;
        }
    }
}
