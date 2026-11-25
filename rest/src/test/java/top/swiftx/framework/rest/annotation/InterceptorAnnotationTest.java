package top.swiftx.framework.rest.annotation;

import org.junit.jupiter.api.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static org.junit.jupiter.api.Assertions.*;

class InterceptorAnnotationTest {

    @Test
    void testAnnotationExists() {
        Interceptor interceptor = TestInterceptor.class.getAnnotation(Interceptor.class);
        assertNotNull(interceptor);
    }

    @Test
    void testDefaultValues() {
        Interceptor interceptor = DefaultInterceptor.class.getAnnotation(Interceptor.class);
        assertEquals(0, interceptor.order());
        assertArrayEquals(new String[]{"/**"}, interceptor.value());
    }

    @Test
    void testCustomOrder() {
        Interceptor interceptor = CustomOrderInterceptor.class.getAnnotation(Interceptor.class);
        assertEquals(10, interceptor.order());
    }

    @Test
    void testCustomPath() {
        Interceptor interceptor = CustomPathInterceptor.class.getAnnotation(Interceptor.class);
        assertArrayEquals(new String[]{"/api/**", "/admin/**"}, interceptor.value());
    }

    @Test
    void testCustomOrderAndPath() {
        Interceptor interceptor = CustomInterceptor.class.getAnnotation(Interceptor.class);
        assertEquals(100, interceptor.order());
        assertArrayEquals(new String[]{"/public/**"}, interceptor.value());
    }

    @Test
    void testAnnotationRetention() {
        Retention retention = Interceptor.class.getAnnotation(Retention.class);
        assertNotNull(retention);
        assertEquals(java.lang.annotation.RetentionPolicy.RUNTIME, retention.value());
    }

    @Test
    void testAnnotationTarget() {
        Target target = Interceptor.class.getAnnotation(Target.class);
        assertNotNull(target);
        assertEquals(1, target.value().length);
        assertEquals(java.lang.annotation.ElementType.TYPE, target.value()[0]);
    }

    // Test classes
    @Interceptor
    static class TestInterceptor {
    }

    @Interceptor
    static class DefaultInterceptor {
    }

    @Interceptor(order = 10)
    static class CustomOrderInterceptor {
    }

    @Interceptor(value = {"/api/**", "/admin/**"})
    static class CustomPathInterceptor {
    }

    @Interceptor(order = 100, value = "/public/**")
    static class CustomInterceptor {
    }
}
