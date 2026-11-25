package top.swiftx.framework.rest;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.HandlerInterceptor;

import static org.junit.jupiter.api.Assertions.*;

class InterceptorRegisterTest {

    // 简单的测试拦截器实现
    private record TestInterceptor(String name) implements HandlerInterceptor {
    }

    @Test
    void testConstructor() {
        HandlerInterceptor interceptor = new TestInterceptor("test1");
        InterceptorRegister register = new InterceptorRegister(1, new String[]{"/test/**"}, interceptor);

        assertEquals(1, register.getOrder());
        assertArrayEquals(new String[]{"/test/**"}, register.getPath());
        assertEquals(interceptor, register.getHandler());
    }

    @Test
    void testConstructorWithEmptyPath() {
        HandlerInterceptor interceptor = new TestInterceptor("test2");
        InterceptorRegister register = new InterceptorRegister(0, new String[]{}, interceptor);

        assertEquals(0, register.getOrder());
        assertEquals(0, register.getPath().length);
        assertEquals(interceptor, register.getHandler());
    }

    @Test
    void testConstructorWithMultiplePaths() {
        HandlerInterceptor interceptor = new TestInterceptor("test3");
        String[] paths = {"/api/**", "/admin/**", "/public/**"};
        InterceptorRegister register = new InterceptorRegister(5, paths, interceptor);

        assertEquals(5, register.getOrder());
        assertArrayEquals(paths, register.getPath());
        assertEquals(interceptor, register.getHandler());
    }

    @Test
    void testSettersAndGetters() {
        HandlerInterceptor interceptor = new TestInterceptor("test4");
        InterceptorRegister register = new InterceptorRegister(0, new String[]{}, interceptor);

        register.setOrder(10);
        assertEquals(10, register.getOrder());

        String[] newPaths = {"/new/**"};
        register.setPath(newPaths);
        assertArrayEquals(newPaths, register.getPath());

        HandlerInterceptor newInterceptor = new TestInterceptor("test5");
        register.setHandler(newInterceptor);
        assertEquals(newInterceptor, register.getHandler());
    }

    @Test
    void testOrdering() {
        HandlerInterceptor interceptor1 = new TestInterceptor("test6");
        HandlerInterceptor interceptor2 = new TestInterceptor("test7");

        InterceptorRegister register1 = new InterceptorRegister(10, new String[]{"/a/**"}, interceptor1);
        InterceptorRegister register2 = new InterceptorRegister(5, new String[]{"/b/**"}, interceptor2);

        assertTrue(register1.getOrder() > register2.getOrder());
    }
}
