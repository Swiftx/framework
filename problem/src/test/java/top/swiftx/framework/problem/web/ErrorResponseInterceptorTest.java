package top.swiftx.framework.problem.web;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import top.swiftx.framework.problem.ProblemProperties;
import top.swiftx.framework.problem.document.DocumentRegistry;
import top.swiftx.framework.problem.markdown.MarkdownParser;
import top.swiftx.framework.problem.uri.TypeUriResolver;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 问题类型错误响应拦截器测试。
 */
class ErrorResponseInterceptorTest {
    /**
     * 测试已注册的相对类型会改写为公开 URI。
     */
    @Test
    void rewritesRegisteredRelativeTypeToPublicUri() {
        ProblemProperties properties = properties();
        ErrorResponseInterceptor interceptor = new ErrorResponseInterceptor(
                registry(properties),
                new TypeUriResolver(properties)
        );
        ProblemDetail body = problemDetail();
        body.setType(URI.create("sample-error"));

        interceptor.handleError(body, null);

        assertEquals(
                URI.create("https://api.example.com/problems/users/sample-error"),
                body.getType()
        );
    }

    /**
     * 测试完整类型 URI 会保持不变。
     */
    @Test
    void keepsAbsoluteTypeUri() {
        ProblemProperties properties = properties();
        ErrorResponseInterceptor interceptor = new ErrorResponseInterceptor(
                registry(properties),
                new TypeUriResolver(properties)
        );
        ProblemDetail body = problemDetail();
        body.setType(URI.create("https://docs.example.com/custom"));

        interceptor.handleError(body, null);

        assertEquals(URI.create("https://docs.example.com/custom"), body.getType());
    }

    /**
     * 创建测试配置。
     *
     * @return 问题类型文档配置
     */
    private ProblemProperties properties() {
        ProblemProperties properties = new ProblemProperties();
        properties.setPublicBaseUrl("https://api.example.com");
        properties.setPublicPathPrefix("/problems/users");
        return properties;
    }

    /**
     * 创建测试问题详情。
     *
     * @return 问题详情
     */
    private ProblemDetail problemDetail() {
        return ProblemDetail.forStatus(HttpStatusCode.valueOf(422));
    }

    /**
     * 创建测试文档注册表。
     *
     * @param properties 问题类型文档配置
     * @return 文档注册表
     */
    private DocumentRegistry registry(ProblemProperties properties) {
        return new DocumentRegistry(
                new PathMatchingResourcePatternResolver(),
                properties,
                new MarkdownParser()
        );
    }
}
