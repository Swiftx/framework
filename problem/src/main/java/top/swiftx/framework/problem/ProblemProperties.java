package top.swiftx.framework.problem;

import lombok.Data;
import org.jspecify.annotations.Nullable;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 问题类型文档配置。
 *
 * @author gpt-5.6-sol
 */
@Data
@ConfigurationProperties(prefix = "framework.problem")
public class ProblemProperties {
    /**
     * 是否启用问题类型文档页面。
     */
    private boolean enabled = true;

    /**
     * 需要加载的问题类型文档资源位置。
     */
    private String[] locations = new String[]{"classpath*:/problems/*.md"};

    /**
     * 当前服务本地暴露的路径前缀。
     */
    private String pathPrefix = "/problems";

    /**
     * 改写 ProblemDetail.type 时使用的公开访问域名。
     */
    private String publicBaseUrl = "";

    /**
     * 改写 ProblemDetail.type 时使用的公开路径前缀。
     */
    private @Nullable String publicPathPrefix;

    /**
     * 文档无效时是否让应用启动失败。
     */
    private boolean failOnInvalid = true;

    /**
     * 获取公开路径前缀，未配置时使用本地路径前缀。
     *
     * @return 公开路径前缀
     */
    public String getPublicPathPrefix() {
        if (publicPathPrefix == null || publicPathPrefix.isBlank()) {
            return pathPrefix;
        }
        return publicPathPrefix;
    }
}
