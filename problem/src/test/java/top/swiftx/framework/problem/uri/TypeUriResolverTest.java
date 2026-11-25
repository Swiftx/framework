package top.swiftx.framework.problem.uri;

import org.junit.jupiter.api.Test;
import top.swiftx.framework.problem.ProblemProperties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 问题类型 URI 解析器测试。
 */
class TypeUriResolverTest {
    /**
     * 测试配置公开访问域名时生成完整类型 URI。
     */
    @Test
    void resolvesPublicTypeUriWhenBaseUrlIsConfigured() {
        ProblemProperties properties = new ProblemProperties();
        properties.setPublicBaseUrl("https://api.example.com/");
        properties.setPublicPathPrefix("/problems/users/");

        TypeUriResolver resolver = new TypeUriResolver(properties);

        assertEquals("/problems/users/sample-error", resolver.resolvePublicPath("sample_error"));
        assertEquals(
                "https://api.example.com/problems/users/sample-error",
                resolver.resolvePublicType("sample_error").orElseThrow().toString()
        );
    }

    /**
     * 测试未配置公开访问域名时不生成类型 URI。
     */
    @Test
    void doesNotResolveTypeUriWithoutPublicBaseUrl() {
        ProblemProperties properties = new ProblemProperties();
        TypeUriResolver resolver = new TypeUriResolver(properties);

        assertTrue(resolver.resolvePublicType("sample-error").isEmpty());
    }
}
