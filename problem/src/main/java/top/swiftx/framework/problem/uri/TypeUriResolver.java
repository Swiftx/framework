package top.swiftx.framework.problem.uri;

import org.jspecify.annotations.Nullable;
import top.swiftx.framework.problem.ProblemProperties;
import top.swiftx.framework.problem.document.Slug;

import java.net.URI;
import java.util.Optional;

/**
 * 问题类型 URI 解析器。
 *
 * @author gpt-5.6-sol
 */
public record TypeUriResolver(ProblemProperties properties) {
    /**
     * 创建问题类型 URI 解析器。
     *
     * @param properties 问题类型文档配置
     */
    public TypeUriResolver {
    }

    /**
     * 解析公开类型 URI。
     *
     * @param slug 问题类型标识
     * @return 公开类型 URI
     */
    public Optional<URI> resolvePublicType(String slug) {
        String baseUrl = normalizeBaseUrl(properties.getPublicBaseUrl());
        if (baseUrl.isBlank()) {
            return Optional.empty();
        }
        return Optional.of(URI.create(baseUrl + resolvePublicPath(slug)));
    }

    /**
     * 解析公开路径。
     *
     * @param slug 问题类型标识
     * @return 公开路径
     */
    public String resolvePublicPath(String slug) {
        return join(normalizePathPrefix(properties.getPublicPathPrefix()), slug);
    }

    /**
     * 规范化路径前缀。
     *
     * @param prefix 路径前缀
     * @return 规范化后的路径前缀
     */
    public static String normalizePathPrefix(@Nullable String prefix) {
        if (prefix == null || prefix.isBlank() || "/".equals(prefix.trim())) {
            return "";
        }
        String normalized = prefix.trim();
        if (!normalized.startsWith("/")) {
            normalized = "/" + normalized;
        }
        return normalized.replaceAll("/+$", "");
    }

    /**
     * 拼接路径前缀与问题类型标识。
     *
     * @param prefix 路径前缀
     * @param slug   问题类型标识
     * @return 完整路径
     */
    private String join(String prefix, String slug) {
        String normalizedSlug = Slug.normalize(slug);
        if (normalizedSlug.isBlank()) {
            return prefix.isBlank() ? "/" : prefix;
        }
        return prefix + "/" + normalizedSlug;
    }

    /**
     * 规范化公开访问域名。
     *
     * @param baseUrl 公开访问域名
     * @return 规范化后的公开访问域名
     */
    private String normalizeBaseUrl(@Nullable String baseUrl) {
        if (baseUrl == null || baseUrl.isBlank()) {
            return "";
        }
        return baseUrl.trim().replaceAll("/+$", "");
    }
}
