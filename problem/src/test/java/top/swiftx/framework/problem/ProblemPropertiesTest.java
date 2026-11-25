package top.swiftx.framework.problem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 问题类型文档配置测试。
 */
class ProblemPropertiesTest {
    /**
     * 测试默认配置。
     */
    @Test
    void testDefaultValues() {
        ProblemProperties properties = new ProblemProperties();

        assertTrue(properties.isEnabled());
        assertArrayEquals(new String[]{"classpath*:/problems/*.md"}, properties.getLocations());
        assertEquals("/problems", properties.getPathPrefix());
        assertEquals("/problems", properties.getPublicPathPrefix());
        assertEquals("", properties.getPublicBaseUrl());
        assertTrue(properties.isFailOnInvalid());
    }

    /**
     * 测试公开路径前缀会覆盖本地路径前缀。
     */
    @Test
    void testPublicPathPrefixOverride() {
        ProblemProperties properties = new ProblemProperties();

        properties.setPathPrefix("/internal/problems");
        properties.setPublicPathPrefix("/problems/users");

        assertEquals("/problems/users", properties.getPublicPathPrefix());
    }
}
