package top.swiftx.framework.problem.web;

import org.jspecify.annotations.Nullable;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;
import top.swiftx.framework.problem.document.DocumentRegistry;
import top.swiftx.framework.problem.document.Slug;
import top.swiftx.framework.problem.uri.TypeUriResolver;

import java.net.URI;

/**
 * 问题类型错误响应拦截器。
 *
 * @author gpt-5.6-sol
 */
public record ErrorResponseInterceptor(
        DocumentRegistry registry,
        TypeUriResolver uriResolver
) implements ErrorResponse.Interceptor {
    /**
     * 创建问题类型错误响应拦截器。
     *
     * @param registry    文档注册表
     * @param uriResolver URI 解析器
     */
    public ErrorResponseInterceptor {
    }

    /**
     * 处理错误响应。
     *
     * @param body          问题详情
     * @param errorResponse 错误响应
     */
    @Override
    public void handleError(
            ProblemDetail body,
            @Nullable ErrorResponse errorResponse
    ) {
        var type = body.getType();
        if (type == null || shouldKeep(type)) return;

        var slug = Slug.normalize(type.toString());
        if (!registry.contains(slug)) return;
        uriResolver.resolvePublicType(slug)
                .ifPresent(body::setType);
    }

    /**
     * 判断是否保留原始类型地址。
     *
     * @param type 类型地址
     * @return 是否保留
     */
    private boolean shouldKeep(URI type) {
        var value = type.toString();
        return type.isAbsolute()
                || value.startsWith("/")
                || "about:blank".equals(value);
    }
}
