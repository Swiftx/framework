package top.swiftx.framework.rest.annotation;

import org.junit.jupiter.api.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static org.junit.jupiter.api.Assertions.*;

class ResolverAnnotationTest {

    @Test
    void testAnnotationExists() {
        Resolver resolver = TestResolver.class.getAnnotation(Resolver.class);
        assertNotNull(resolver);
    }

    @Test
    void testAnnotationRetention() {
        Retention retention = Resolver.class.getAnnotation(Retention.class);
        assertNotNull(retention);
        assertEquals(java.lang.annotation.RetentionPolicy.RUNTIME, retention.value());
    }

    @Test
    void testAnnotationTarget() {
        Target target = Resolver.class.getAnnotation(Target.class);
        assertNotNull(target);
        assertEquals(1, target.value().length);
        assertEquals(java.lang.annotation.ElementType.TYPE, target.value()[0]);
    }

    @Test
    void testNoAttributes() {
        Resolver resolver = TestResolver.class.getAnnotation(Resolver.class);
        assertNotNull(resolver);
    }

    @Test
    void testMultipleClassesWithResolver() {
        Resolver resolver1 = TestResolver.class.getAnnotation(Resolver.class);
        Resolver resolver2 = AnotherResolver.class.getAnnotation(Resolver.class);

        assertNotNull(resolver1);
        assertNotNull(resolver2);
        assertEquals(resolver1.annotationType(), resolver2.annotationType());
    }

    @Test
    void testAnnotationType() {
        assertEquals(Resolver.class, TestResolver.class.getAnnotation(Resolver.class).annotationType());
    }

    // Test classes
    @Resolver
    static class TestResolver {
    }

    @Resolver
    static class AnotherResolver {
    }
}
